'use client'

import { useState, useEffect, Suspense } from 'react'
import { useRouter, useSearchParams } from 'next/navigation'
import Link from 'next/link'
import { apiClient } from '@/lib/api-client'
import { useToast } from '@/lib/toast-context'

function ResetPasswordForm() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const toast = useToast()
  const token = searchParams.get('token')
  
  const [formData, setFormData] = useState({
    password: '',
    confirmPassword: '',
  })
  
  const [errors, setErrors] = useState<{ password?: string; confirmPassword?: string; general?: string }>({})
  const [isLoading, setIsLoading] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [tokenValid, setTokenValid] = useState<boolean | null>(null)
  const [resetSuccess, setResetSuccess] = useState(false)
  
  useEffect(() => {
    // Validate token on mount
    if (!token) {
      setTokenValid(false)
      setErrors({ general: 'Invalid or missing reset token' })
      return
    }
    
    // Token validation is typically done on submission
    // But we can do a basic check here
    setTokenValid(true)
  }, [token])
  
  const getPasswordStrength = (password: string) => {
    let strength = 0
    if (password.length >= 8) strength++
    if (password.length >= 12) strength++
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength++
    if (/\d/.test(password)) strength++
    if (/[^a-zA-Z\d]/.test(password)) strength++
    return strength
  }
  
  const passwordStrength = getPasswordStrength(formData.password)
  const passwordStrengthLabel = ['Very Weak', 'Weak', 'Fair', 'Good', 'Strong'][passwordStrength] || ''
  const passwordStrengthColor = ['bg-red-500', 'bg-orange-500', 'bg-yellow-500', 'bg-lime-500', 'bg-green-500'][passwordStrength] || 'bg-gray-300'
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
    
    if (errors[name as keyof typeof errors]) {
      setErrors(prev => ({ ...prev, [name]: undefined }))
    }
  }
  
  const validate = () => {
    const newErrors: typeof errors = {}
    
    if (!formData.password) {
      newErrors.password = 'Password is required'
    } else if (formData.password.length < 8) {
      newErrors.password = 'Password must be at least 8 characters'
    } else if (passwordStrength < 2) {
      newErrors.password = 'Password is too weak. Please use a stronger password.'
    }
    
    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Please confirm your password'
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match'
    }
    
    return newErrors
  }
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!token) {
      setErrors({ general: 'Invalid reset token' })
      return
    }
    
    const newErrors = validate()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }
    
    setIsLoading(true)
    setErrors({})
    
    try {
      await apiClient.resetPassword(token, formData.password)
      setResetSuccess(true)
      toast.success('Password reset successful! Redirecting to login...')
      
      // Redirect to login after 2 seconds
      setTimeout(() => {
        router.push('/login?reset=success')
      }, 2000)
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || 'Failed to reset password. The link may have expired.'
      setErrors({ general: errorMessage })
      toast.error(errorMessage)
      
      // If token is expired, offer to request a new one
      if (errorMessage.includes('expired') || errorMessage.includes('invalid')) {
        setTokenValid(false)
      }
    } finally {
      setIsLoading(false)
    }
  }
  
  // Success state
  if (resetSuccess) {
    return (
      <div className="min-h-screen bg-warm-cream flex flex-col">
        <header className="bg-white border-b border-gray-200">
          <div className="container mx-auto px-4 py-6">
            <Link href="/" className="inline-block">
              <h1 className="font-playfair text-3xl font-bold text-deep-sage">Orchidillo</h1>
            </Link>
          </div>
        </header>
        
        <main className="flex-1 flex items-center justify-center px-4 py-12">
          <div className="w-full max-w-md">
            <div className="bg-white rounded-xl shadow-lg p-8 md:p-10 text-center">
              <div className="w-16 h-16 bg-green-50 rounded-full flex items-center justify-center mx-auto mb-6">
                <span className="material-symbols-outlined text-4xl text-green-500">
                  check_circle
                </span>
              </div>
              
              <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-4">
                Password reset successful!
              </h2>
              
              <p className="text-sage-green mb-8">
                Your password has been updated. You can now log in with your new password.
              </p>
              
              <Link
                href="/login"
                className="inline-block w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all text-center"
              >
                Continue to login
              </Link>
            </div>
          </div>
        </main>
      </div>
    )
  }
  
  // Loading state
  if (tokenValid === null) {
    return (
      <div className="min-h-screen bg-warm-cream flex items-center justify-center">
        <div className="text-center">
          <div className="w-12 h-12 border-4 border-accent-peach border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-sage-green">Validating reset link...</p>
        </div>
      </div>
    )
  }
  
  // Invalid token
  if (!tokenValid) {
    return (
      <div className="min-h-screen bg-warm-cream flex flex-col">
        <header className="bg-white border-b border-gray-200">
          <div className="container mx-auto px-4 py-6">
            <Link href="/" className="inline-block">
              <h1 className="font-playfair text-3xl font-bold text-deep-sage">Orchidillo</h1>
            </Link>
          </div>
        </header>
        
        <main className="flex-1 flex items-center justify-center px-4 py-12">
          <div className="w-full max-w-md">
            <div className="bg-white rounded-xl shadow-lg p-8 md:p-10 text-center">
              <div className="w-16 h-16 bg-red-50 rounded-full flex items-center justify-center mx-auto mb-6">
                <span className="material-symbols-outlined text-4xl text-red-500">
                  error
                </span>
              </div>
              
              <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-4">
                Invalid reset link
              </h2>
              
              <p className="text-sage-green mb-8">
                This password reset link is invalid or has expired. Please request a new one.
              </p>
              
              <Link
                href="/forgot-password"
                className="inline-block w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all text-center"
              >
                Request new link
              </Link>
              
              <Link 
                href="/login"
                className="inline-block mt-4 text-sm text-accent-peach hover:text-deep-sage transition-colors"
              >
                ← Back to login
              </Link>
            </div>
          </div>
        </main>
      </div>
    )
  }
  
  return (
    <div className="min-h-screen bg-warm-cream flex flex-col">
      {/* Header */}
      <header className="bg-white border-b border-gray-200">
        <div className="container mx-auto px-4 py-6">
          <Link href="/" className="inline-block">
            <h1 className="font-playfair text-3xl font-bold text-deep-sage">Orchidillo</h1>
          </Link>
        </div>
      </header>
      
      {/* Main Content */}
      <main className="flex-1 flex items-center justify-center px-4 py-12">
        <div className="w-full max-w-md">
          <div className="bg-white rounded-xl shadow-lg p-8 md:p-10">
            <h2 className="font-playfair text-3xl font-bold text-deep-sage text-center mb-2">
              Set new password
            </h2>
            <p className="text-sage-green text-center mb-8">
              Choose a strong password for your account
            </p>
            
            {/* Error Message */}
            {errors.general && (
              <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                <p className="text-sm text-red-600">{errors.general}</p>
              </div>
            )}
            
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* New Password */}
              <div>
                <label htmlFor="password" className="block text-sm font-medium text-deep-sage mb-2">
                  New Password
                </label>
                <div className="relative">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    id="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    className={`w-full px-4 py-3 rounded-lg border ${
                      errors.password ? 'border-red-300' : 'border-gray-300'
                    } bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all`}
                    placeholder="••••••••"
                    disabled={isLoading}
                    autoFocus
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-sage-green hover:text-deep-sage transition-colors"
                    disabled={isLoading}
                  >
                    <span className="material-symbols-outlined text-xl">
                      {showPassword ? 'visibility_off' : 'visibility'}
                    </span>
                  </button>
                </div>
                
                {formData.password && (
                  <div className="mt-2">
                    <div className="flex items-center justify-between mb-1">
                      <span className="text-xs text-sage-green">Password strength:</span>
                      <span className="text-xs font-medium text-deep-sage">{passwordStrengthLabel}</span>
                    </div>
                    <div className="h-1 bg-gray-200 rounded-full overflow-hidden">
                      <div 
                        className={`h-full ${passwordStrengthColor} transition-all duration-300`}
                        style={{ width: `${(passwordStrength / 5) * 100}%` }}
                      />
                    </div>
                  </div>
                )}
                
                {errors.password && (
                  <p className="mt-1 text-sm text-red-600">{errors.password}</p>
                )}
              </div>
              
              {/* Confirm Password */}
              <div>
                <label htmlFor="confirmPassword" className="block text-sm font-medium text-deep-sage mb-2">
                  Confirm Password
                </label>
                <div className="relative">
                  <input
                    type={showConfirmPassword ? 'text' : 'password'}
                    id="confirmPassword"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    className={`w-full px-4 py-3 rounded-lg border ${
                      errors.confirmPassword ? 'border-red-300' : 'border-gray-300'
                    } bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all`}
                    placeholder="••••••••"
                    disabled={isLoading}
                  />
                  <button
                    type="button"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-sage-green hover:text-deep-sage transition-colors"
                    disabled={isLoading}
                  >
                    <span className="material-symbols-outlined text-xl">
                      {showConfirmPassword ? 'visibility_off' : 'visibility'}
                    </span>
                  </button>
                </div>
                {errors.confirmPassword && (
                  <p className="mt-1 text-sm text-red-600">{errors.confirmPassword}</p>
                )}
              </div>
              
              {/* Submit Button */}
              <button
                type="submit"
                disabled={isLoading}
                className="w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? 'Resetting password...' : 'Reset password'}
              </button>
            </form>
            
            {/* Back to Login */}
            <div className="mt-8 text-center">
              <Link 
                href="/login"
                className="inline-flex items-center text-sm text-accent-peach hover:text-deep-sage transition-colors"
              >
                <span className="material-symbols-outlined text-lg mr-1">
                  arrow_back
                </span>
                Back to login
              </Link>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}

export default function ResetPasswordPage() {
  return (
    <Suspense fallback={
      <div className="min-h-screen bg-warm-cream flex items-center justify-center">
        <div className="text-center">
          <div className="w-12 h-12 border-4 border-accent-peach border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-sage-green">Loading...</p>
        </div>
      </div>
    }>
      <ResetPasswordForm />
    </Suspense>
  )
}
