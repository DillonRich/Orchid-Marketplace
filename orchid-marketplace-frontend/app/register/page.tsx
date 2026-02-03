'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import Link from 'next/link'
import { useAuthStore } from '@/lib/auth-store'
import { useToast } from '@/lib/toast-context'

export default function RegisterPage() {
  const router = useRouter()
  const toast = useToast()
  const { register } = useAuthStore()
  
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    accountType: 'CUSTOMER' as 'CUSTOMER' | 'SELLER',
    storeName: '',
    storeDescription: '',
    acceptTerms: false,
  })
  
  const [errors, setErrors] = useState<Record<string, string>>({})
  const [isLoading, setIsLoading] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value, type } = e.target
    const checked = (e.target as HTMLInputElement).checked
    
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }))
    
    // Clear error when user starts typing
    if (errors[name]) {
      const newErrors = { ...errors }
      delete newErrors[name]
      setErrors(newErrors)
    }
  }
  
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
  
  const validate = () => {
    const newErrors: Record<string, string> = {}
    
    if (!formData.firstName.trim()) newErrors.firstName = 'First name is required'
    if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required'
    
    if (!formData.email) {
      newErrors.email = 'Email is required'
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Email is invalid'
    }
    
    if (!formData.password) {
      newErrors.password = 'Password is required'
    } else if (formData.password.length < 8) {
      newErrors.password = 'Password must be at least 8 characters'
    }
    
    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Please confirm your password'
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match'
    }
    
    if (formData.accountType === 'SELLER') {
      if (!formData.storeName.trim()) {
        newErrors.storeName = 'Store name is required for seller accounts'
      }
    }
    
    if (!formData.acceptTerms) {
      newErrors.acceptTerms = 'You must accept the terms and conditions'
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
    
    console.log('Attempting registration with:', {
      email: formData.email,
      fullName: `${formData.firstName} ${formData.lastName}`,
      accountType: formData.accountType
    })
    
    try {
      await register(
        formData.email,
        `${formData.firstName} ${formData.lastName}`,
        formData.password,
        formData.accountType
      )
      
      toast.success('Account created successfully! Welcome to Orchidillo!')
      
      // Redirect based on account type
      setTimeout(() => {
        if (formData.accountType === 'SELLER') {
          router.push('/seller/dashboard')
        } else {
          router.push('/')
        }
      }, 500)
    } catch (error: any) {
      console.error('Registration error:', error)
      console.error('Error response:', error.response)
      console.error('Error data:', error.response?.data)
      console.error('Error message:', error.message)
      
      const errorMessage = error.response?.data?.message || error.message || 'Registration failed. Please try again.'
      setErrors({ general: errorMessage })
      toast.error(errorMessage)
    } finally {
      setIsLoading(false)
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
        <div className="w-full max-w-2xl">
          <div className="bg-white rounded-xl shadow-lg p-8 md:p-10">
            <h2 className="font-playfair text-3xl font-bold text-deep-sage text-center mb-2">
              Create your account
            </h2>
            <p className="text-sage-green text-center mb-8">
              Join our community of orchid enthusiasts
            </p>
            
            {/* Error Message */}
            {errors.general && (
              <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                <p className="text-sm text-red-600">{errors.general}</p>
              </div>
            )}
            
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Account Type */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-3">
                  Account Type
                </label>
                <div className="grid grid-cols-2 gap-4">
                  <label className={`flex items-center justify-center p-4 border-2 rounded-lg cursor-pointer transition-all ${
                    formData.accountType === 'CUSTOMER' 
                      ? 'border-accent-peach bg-soft-peach' 
                      : 'border-gray-300 hover:border-sage-green'
                  }`}>
                    <input
                      type="radio"
                      name="accountType"
                      value="CUSTOMER"
                      checked={formData.accountType === 'CUSTOMER'}
                      onChange={handleChange}
                      className="sr-only"
                      disabled={isLoading}
                    />
                    <div className="text-center">
                      <span className="material-symbols-outlined text-3xl text-deep-sage mb-2">
                        shopping_bag
                      </span>
                      <p className="font-medium text-deep-sage">Customer</p>
                      <p className="text-xs text-sage-green mt-1">Browse and buy orchids</p>
                    </div>
                  </label>
                  
                  <label className={`flex items-center justify-center p-4 border-2 rounded-lg cursor-pointer transition-all ${
                    formData.accountType === 'SELLER' 
                      ? 'border-accent-peach bg-soft-peach' 
                      : 'border-gray-300 hover:border-sage-green'
                  }`}>
                    <input
                      type="radio"
                      name="accountType"
                      value="SELLER"
                      checked={formData.accountType === 'SELLER'}
                      onChange={handleChange}
                      className="sr-only"
                      disabled={isLoading}
                    />
                    <div className="text-center">
                      <span className="material-symbols-outlined text-3xl text-deep-sage mb-2">
                        storefront
                      </span>
                      <p className="font-medium text-deep-sage">Seller</p>
                      <p className="text-xs text-sage-green mt-1">Sell your orchids</p>
                    </div>
                  </label>
                </div>
              </div>
              
              {/* Name */}
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label htmlFor="firstName" className="block text-sm font-medium text-deep-sage mb-2">
                    First Name
                  </label>
                  <input
                    type="text"
                    id="firstName"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    className={`w-full px-4 py-3 rounded-lg border ${
                      errors.firstName ? 'border-red-300' : 'border-gray-300'
                    } bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all`}
                    placeholder="John"
                    disabled={isLoading}
                  />
                  {errors.firstName && (
                    <p className="mt-1 text-sm text-red-600">{errors.firstName}</p>
                  )}
                </div>
                
                <div>
                  <label htmlFor="lastName" className="block text-sm font-medium text-deep-sage mb-2">
                    Last Name
                  </label>
                  <input
                    type="text"
                    id="lastName"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    className={`w-full px-4 py-3 rounded-lg border ${
                      errors.lastName ? 'border-red-300' : 'border-gray-300'
                    } bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all`}
                    placeholder="Doe"
                    disabled={isLoading}
                  />
                  {errors.lastName && (
                    <p className="mt-1 text-sm text-red-600">{errors.lastName}</p>
                  )}
                </div>
              </div>
              
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
              
              {/* Seller-specific fields */}
              {formData.accountType === 'SELLER' && (
                <>
                  <div>
                    <label htmlFor="storeName" className="block text-sm font-medium text-deep-sage mb-2">
                      Store Name
                    </label>
                    <input
                      type="text"
                      id="storeName"
                      name="storeName"
                      value={formData.storeName}
                      onChange={handleChange}
                      className={`w-full px-4 py-3 rounded-lg border ${
                        errors.storeName ? 'border-red-300' : 'border-gray-300'
                      } bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all`}
                      placeholder="My Orchid Store"
                      disabled={isLoading}
                    />
                    {errors.storeName && (
                      <p className="mt-1 text-sm text-red-600">{errors.storeName}</p>
                    )}
                  </div>
                  
                  <div>
                    <label htmlFor="storeDescription" className="block text-sm font-medium text-deep-sage mb-2">
                      Store Description (Optional)
                    </label>
                    <textarea
                      id="storeDescription"
                      name="storeDescription"
                      value={formData.storeDescription}
                      onChange={handleChange}
                      rows={3}
                      className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all resize-none"
                      placeholder="Tell customers about your store..."
                      disabled={isLoading}
                    />
                  </div>
                </>
              )}
              
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
              
              {/* Terms */}
              <div>
                <label className="flex items-start">
                  <input
                    type="checkbox"
                    name="acceptTerms"
                    checked={formData.acceptTerms}
                    onChange={handleChange}
                    className={`mt-1 w-4 h-4 rounded border-gray-300 text-accent-peach focus:ring-accent-peach ${
                      errors.acceptTerms ? 'border-red-300' : ''
                    }`}
                    disabled={isLoading}
                  />
                  <span className="ml-2 text-sm text-sage-green">
                    I agree to the{' '}
                    <Link href="/terms" className="text-accent-peach hover:text-deep-sage transition-colors">
                      Terms of Service
                    </Link>{' '}
                    and{' '}
                    <Link href="/privacy" className="text-accent-peach hover:text-deep-sage transition-colors">
                      Privacy Policy
                    </Link>
                  </span>
                </label>
                {errors.acceptTerms && (
                  <p className="mt-1 text-sm text-red-600">{errors.acceptTerms}</p>
                )}
              </div>
              
              {/* Submit Button */}
              <button
                type="submit"
                disabled={isLoading}
                className="w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? 'Creating account...' : 'Create account'}
              </button>
            </form>
            
            {/* Login Link */}
            <p className="mt-8 text-center text-sm text-sage-green">
              Already have an account?{' '}
              <Link href="/login" className="font-medium text-accent-peach hover:text-deep-sage transition-colors">
                Sign in
              </Link>
            </p>
          </div>
        </div>
      </main>
    </div>
  )
}
