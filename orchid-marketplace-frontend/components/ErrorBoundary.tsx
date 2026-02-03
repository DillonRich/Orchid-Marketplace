'use client'

import React, { Component, ErrorInfo, ReactNode } from 'react'
import Link from 'next/link'

interface Props {
  children: ReactNode
  fallback?: ReactNode
}

interface State {
  hasError: boolean
  error: Error | null
}

export class ErrorBoundary extends Component<Props, State> {
  constructor(props: Props) {
    super(props)
    this.state = { hasError: false, error: null }
  }

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error }
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Error Boundary caught an error:', error, errorInfo)
  }

  render() {
    if (this.state.hasError) {
      if (this.props.fallback) {
        return this.props.fallback
      }

      return (
        <div className="min-h-screen bg-warm-cream flex items-center justify-center px-4">
          <div className="max-w-2xl w-full bg-white rounded-2xl shadow-lg p-8 md:p-12 text-center">
            <div className="mb-6">
              <span className="material-symbols-outlined text-red-500 text-7xl">error</span>
            </div>
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-4">
              Oops! Something went wrong
            </h1>
            <p className="text-sage-green text-lg mb-2">
              We encountered an unexpected error. Our team has been notified.
            </p>
            {this.state.error && (
              <details className="mt-4 text-left bg-gray-50 rounded-lg p-4">
                <summary className="cursor-pointer text-sm font-medium text-gray-700 mb-2">
                  Technical Details
                </summary>
                <pre className="text-xs text-red-600 overflow-auto">
                  {this.state.error.message}
                  {'\n\n'}
                  {this.state.error.stack}
                </pre>
              </details>
            )}
            <div className="flex flex-col sm:flex-row gap-4 justify-center mt-8">
              <button
                onClick={() => this.setState({ hasError: false, error: null })}
                className="px-6 py-3 bg-accent-peach text-white rounded-full font-medium hover:opacity-90 transition-opacity"
              >
                Try Again
              </button>
              <Link
                href="/"
                className="px-6 py-3 bg-white border-2 border-sage-green text-sage-green rounded-full font-medium hover:bg-sage-green hover:text-white transition-colors"
              >
                Go to Homepage
              </Link>
            </div>
          </div>
        </div>
      )
    }

    return this.props.children
  }
}

export default ErrorBoundary
