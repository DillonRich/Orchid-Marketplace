'use client'

import { useState } from 'react'
import { useRouter, useSearchParams } from 'next/navigation'
import Link from 'next/link'
import { useAuthStore } from '@/lib/auth-store'
import { useToast } from '@/lib/toast-context'
import { azureB2CLogin, isAzureB2CConfigured } from '@/lib/azure-b2c-config'

export default function LoginPage() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const redirect = searchParams.get('redirect') || '/'
  const toast = useToast()
  const resetSuccess = searchParams.get('reset') === 'success'
  
  const { login, loginWithAzure } = useAuthStore()
  
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    rememberMe: false,
  })
  
  const [errors, setErrors] = useState<{ email?: string; password?: string; general?: string }>({})
  const [isLoading, setIsLoading] = useState(false)
  const [isAzureLoading, setIsAzureLoading] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const azureB2CEnabled = isAzureB2CConfigured()
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }))
    // Clear error when user starts typing
    if (errors[name as keyof typeof errors]) {
      setErrors(prev => ({ ...prev, [name]: undefined }))
    }
  }
  
  const validate = () => {
    const newErrors: typeof errors = {}
    
    if (!formData.email) {
      newErrors.email = 'Email is required'
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Email is invalid'
    }
    
    if (!formData.password) {
      newErrors.password = 'Password is required'
    } else if (formData.password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters'
    }
    
    return newErrors
  }
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    const newErrors = validate()
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }
    
    setIsLoading(true)
    setErrors({})
    
    try {
      await login(formData.email, formData.password)
      toast.success('Login successful! Redirecting...')
      
      // Redirect
      setTimeout(() => {
        router.push(redirect)
      }, 500)
    } catch (error: any) {
      console.error('Login error:', error)
      const errorMessage = error.response?.data?.message || 'Invalid email or password. Please try again.'
      setErrors({ general: errorMessage })
      toast.error(errorMessage)
    } finally {
      setIsLoading(false)
    }
  }
  
  const handleAzureLogin = async () => {
    setIsAzureLoading(true)
    setErrors({})
    
    try {
      const response = await azureB2CLogin()
      
      // Send Azure token to backend for verification and JWT generation
      await loginWithAzure(response.idToken)
      
      toast.success('Azure login successful! Redirecting...')
      
      // Redirect after successful login
      setTimeout(() => {
        router.push(redirect)
      }, 500)
    } catch (error: any) {
      console.error('Azure login error:', error)
      const errorMessage = error.message || 'Azure login failed. Please try again.'
      setErrors({ general: errorMessage })
      toast.error(errorMessage)
    } finally {
      setIsAzureLoading(false)
    }
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
              Welcome back
            </h2>
            <p className="text-sage-green text-center mb-8">
              Sign in to your account to continue
            </p>
            
            {/* Reset Success Message */}
            {resetSuccess && (
              <div className="mb-6 p-4 bg-green-50 border border-green-200 rounded-lg">
                <div className="flex items-start gap-3">
                  <span className="material-symbols-outlined text-green-600">check_circle</span>
                  <div>
                    <p className="text-sm font-medium text-green-900">Password Reset Successful</p>
                    <p className="text-xs text-green-700 mt-1">
                      You can now log in with your new password.
                    </p>
                  </div>
                </div>
              </div>
            )}
            
            {/* Backend Notice */}
            <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
              <div className="flex items-start gap-3">
                <span className="material-symbols-outlined text-blue-600">info</span>
                <div>
                  <p className="text-sm font-medium text-blue-900">Backend Connection Required</p>
                  <p className="text-xs text-blue-700 mt-1">
                    Login functionality requires backend connectivity. Once the backend is running, you'll be able to sign in with your credentials.
                  </p>
                </div>
              </div>
            </div>
            
            {/* Error Message */}
            {errors.general && (
              <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                <p className="text-sm text-red-600">{errors.general}</p>
              </div>
            )}
            
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Email */}
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-deep-sage mb-2">
                  Email address
                </label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className={`w-full px-4 py-3 rounded-lg border ${
                    errors.email ? 'border-red-300' : 'border-gray-300'
                  } bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all`}
                  placeholder="you@example.com"
                  disabled={isLoading}
                />
                {errors.email && (
                  <p className="mt-1 text-sm text-red-600">{errors.email}</p>
                )}
              </div>
              
              {/* Password */}
              <div>
                <label htmlFor="password" className="block text-sm font-medium text-deep-sage mb-2">
                  Password
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
                {errors.password && (
                  <p className="mt-1 text-sm text-red-600">{errors.password}</p>
                )}
              </div>
              
              {/* Remember Me & Forgot Password */}
              <div className="flex items-center justify-between">
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    name="rememberMe"
                    checked={formData.rememberMe}
                    onChange={handleChange}
                    className="w-4 h-4 rounded border-gray-300 text-accent-peach focus:ring-accent-peach"
                    disabled={isLoading}
                  />
                  <span className="ml-2 text-sm text-sage-green">Remember me</span>
                </label>
                
                <Link 
                  href="/forgot-password" 
                  className="text-sm text-accent-peach hover:text-deep-sage transition-colors"
                >
                  Forgot password?
                </Link>
              </div>
              
              {/* Submit Button */}
              <button
                type="submit"
                disabled={isLoading}
                className="w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? 'Signing in...' : 'Sign in'}
              </button>
            </form>
            
            {/* Social Login */}
            <div className="mt-8">
              <div className="relative">
                <div className="absolute inset-0 flex items-center">
                  <div className="w-full border-t border-gray-300"></div>
                </div>
                <div className="relative flex justify-center text-sm">
                  <span className="px-4 bg-white text-sage-green">Or continue with</span>
                </div>
              </div>
              
              <div className="mt-6 grid grid-cols-2 gap-3">
                {/* Azure AD B2C Button */}
                {azureB2CEnabled ? (
                  <button
                    type="button"
                    onClick={handleAzureLogin}
                    disabled={isAzureLoading}
                    className="flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    <svg className="w-5 h-5 mr-2" viewBox="0 0 23 23" fill="none">
                      <path d="M0 0h10.92v10.92H0V0z" fill="#F25022"/>
                      <path d="M12.08 0H23v10.92H12.08V0z" fill="#7FBA00"/>
                      <path d="M0 12.08h10.92V23H0V12.08z" fill="#00A4EF"/>
                      <path d="M12.08 12.08H23V23H12.08V12.08z" fill="#FFB900"/>
                    </svg>
                    {isAzureLoading ? 'Connecting...' : 'Microsoft'}
                  </button>
                ) : (
                  <button
                    type="button"
                    disabled
                    className="flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    <svg className="w-5 h-5 mr-2" viewBox="0 0 24 24">
                      <path fill="currentColor" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                      <path fill="currentColor" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                      <path fill="currentColor" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                      <path fill="currentColor" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                    </svg>
                    Google
                  </button>
                )}
                
                <button
                  type="button"
                  disabled
                  className="flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                  </svg>
                  Facebook
                </button>
              </div>
              
              {!azureB2CEnabled && (
                <p className="mt-4 text-xs text-center text-sage-green">
                  Configure Azure AD B2C in .env.local to enable Microsoft login
                </p>
              )}
            </div>
            
            {/* Sign Up Link */}
            <p className="mt-8 text-center text-sm text-sage-green">
              Don't have an account?{' '}
              <Link href="/register" className="font-medium text-accent-peach hover:text-deep-sage transition-colors">
                Sign up
              </Link>
            </p>
          </div>
        </div>
      </main>
    </div>
  )
}
