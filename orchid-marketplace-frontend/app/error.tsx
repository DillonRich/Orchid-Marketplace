'use client'

import { useEffect } from 'react'
import Link from 'next/link'
import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function GlobalError({
  error,
  reset,
}: {
  error: Error & { digest?: string }
  reset: () => void
}) {
  useEffect(() => {
    // Log the error to an error reporting service
    console.error('Global error:', error)
  }, [error])

  return (
    <html>
      <body className="min-h-screen bg-warm-cream">
        <Header />
        <div className="min-h-[60vh] flex items-center justify-center px-4 py-20">
          <div className="max-w-2xl w-full bg-white rounded-2xl shadow-lg p-8 md:p-12 text-center">
            <div className="mb-6">
              <span className="material-symbols-outlined text-red-500 text-8xl">
                error
              </span>
            </div>
            <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-4">
              500
            </h1>
            <h2 className="font-playfair text-2xl font-semibold text-deep-sage mb-4">
              Something Went Wrong
            </h2>
            <p className="text-sage-green text-lg mb-2">
              We encountered an unexpected error. Our team has been notified and is working on a fix.
            </p>
            <p className="text-sage-green text-sm mb-8">
              {error.digest && `Error ID: ${error.digest}`}
            </p>
            
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <button
                onClick={() => reset()}
                className="px-8 py-3 bg-accent-peach text-white rounded-full font-medium hover:opacity-90 transition-opacity inline-flex items-center justify-center gap-2"
              >
                <span className="material-symbols-outlined">refresh</span>
                Try Again
              </button>
              <Link
                href="/"
                className="px-8 py-3 bg-white border-2 border-sage-green text-sage-green rounded-full font-medium hover:bg-sage-green hover:text-white transition-colors inline-flex items-center justify-center gap-2"
              >
                <span className="material-symbols-outlined">home</span>
                Go to Homepage
              </Link>
            </div>
            
            {process.env.NODE_ENV === 'development' && error && (
              <details className="mt-8 text-left bg-gray-50 rounded-lg p-4">
                <summary className="cursor-pointer text-sm font-medium text-gray-700 mb-2">
                  Technical Details (Development Only)
                </summary>
                <pre className="text-xs text-red-600 overflow-auto">
                  {error.message}
                  {'\n\n'}
                  {error.stack}
                </pre>
              </details>
            )}
          </div>
        </div>
        <Footer />
      </body>
    </html>
  )
}
