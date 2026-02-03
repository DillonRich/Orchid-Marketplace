'use client'

import { useState } from 'react'
import Link from 'next/link'
import { apiClient } from '@/lib/api-client'

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [isSubmitted, setIsSubmitted] = useState(false)
  const [canResend, setCanResend] = useState(false)
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!email) {
      setError('Email is required')
      return
    }
    
    if (!/\S+@\S+\.\S+/.test(email)) {
      setError('Please enter a valid email address')
      return
    }
    
    setIsLoading(true)
    setError('')
    
    try {
      await apiClient.forgotPassword(email)
      setIsSubmitted(true)
      
      // Enable resend after 60 seconds
      setTimeout(() => {
        setCanResend(true)
      }, 60000)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to send reset link. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }
  
  const handleResend = async () => {
    setCanResend(false)
    setIsSubmitted(false)
    await handleSubmit(new Event('submit') as any)
    
    // Enable resend again after 60 seconds
    setTimeout(() => {
      setCanResend(true)
    }, 60000)
  }
  
  if (isSubmitted) {
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
            <div className="bg-white rounded-xl shadow-lg p-8 md:p-10 text-center">
              <div className="w-16 h-16 bg-soft-peach rounded-full flex items-center justify-center mx-auto mb-6">
                <span className="material-symbols-outlined text-4xl text-accent-peach">
                  mail
                </span>
              </div>
              
              <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-4">
                Check your email
              </h2>
              
              <p className="text-sage-green mb-6">
                We've sent a password reset link to <strong className="text-deep-sage">{email}</strong>
              </p>
              
              <p className="text-sm text-sage-green mb-8">
                Didn't receive the email? Check your spam folder or try resending.
              </p>
              
              <button
                onClick={handleResend}
                disabled={!canResend || isLoading}
                className="w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed mb-4"
              >
                {isLoading ? 'Sending...' : canResend ? 'Resend link' : 'Resend available in 60s'}
              </button>
              
              <Link 
                href="/login"
                className="inline-block text-sm text-accent-peach hover:text-deep-sage transition-colors"
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
              Forgot password?
            </h2>
            <p className="text-sage-green text-center mb-8">
              No worries, we'll send you reset instructions
            </p>
            
            {/* Error Message */}
            {error && (
              <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                <p className="text-sm text-red-600">{error}</p>
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
                  value={email}
                  onChange={(e) => {
                    setEmail(e.target.value)
                    setError('')
                  }}
                  className={`w-full px-4 py-3 rounded-lg border ${
                    error ? 'border-red-300' : 'border-gray-300'
                  } bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach focus:border-transparent transition-all`}
                  placeholder="you@example.com"
                  disabled={isLoading}
                  autoFocus
                />
              </div>
              
              {/* Submit Button */}
              <button
                type="submit"
                disabled={isLoading}
                className="w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? 'Sending...' : 'Send reset link'}
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
