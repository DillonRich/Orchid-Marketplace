'use client'

import { useSearchParams } from 'next/navigation'
import Link from 'next/link'
import SimpleNav from '@/components/SimpleNav'

export default function CheckoutCancelPage() {
  const searchParams = useSearchParams()
  const errorMessage = searchParams.get('error') || 'Payment was cancelled'
  
  return (
    <div className="min-h-screen bg-warm-cream">
      <SimpleNav />
      
      <main className="container mx-auto px-4 py-16">
        <div className="max-w-2xl mx-auto">
          {/* Error/Cancel Card */}
          <div className="bg-white rounded-2xl shadow-xl p-8 md:p-12 text-center">
            <div className="inline-flex items-center justify-center w-20 h-20 bg-red-100 rounded-full mb-6">
              <span className="material-symbols-outlined text-5xl text-red-600">
                cancel
              </span>
            </div>
            
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-4">
              Payment Cancelled
            </h1>
            
            <p className="text-sage-green text-lg mb-4">
              {errorMessage === 'payment_failed' 
                ? 'Your payment could not be processed.' 
                : 'Your payment was cancelled.'}
            </p>
            
            <p className="text-sm text-sage-green mb-8">
              No charges were made to your account. Your cart items are still saved.
            </p>
            
            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-4 justify-center mb-8">
              <Link
                href="/cart"
                className="inline-flex items-center justify-center px-8 py-3 bg-deep-sage text-white rounded-full font-medium hover:bg-sage-green transition-all shadow-lg hover:shadow-xl"
              >
                <span className="material-symbols-outlined mr-2">shopping_cart</span>
                Return to Cart
              </Link>
              <Link
                href="/checkout"
                className="inline-flex items-center justify-center px-8 py-3 bg-white text-deep-sage rounded-full font-medium hover:bg-soft-peach transition-all border-2 border-sage-green"
              >
                <span className="material-symbols-outlined mr-2">refresh</span>
                Try Again
              </Link>
            </div>
            
            {/* Help Section */}
            <div className="mt-8 pt-8 border-t border-gray-200">
              <h3 className="font-medium text-deep-sage mb-4">Need help?</h3>
              <div className="flex flex-col sm:flex-row gap-4 justify-center text-sm">
                <Link href="/contact" className="text-accent-peach hover:text-deep-sage transition-colors">
                  Contact Support
                </Link>
                <span className="hidden sm:inline text-gray-300">|</span>
                <Link href="/shipping" className="text-accent-peach hover:text-deep-sage transition-colors">
                  Shipping Info
                </Link>
                <span className="hidden sm:inline text-gray-300">|</span>
                <Link href="/faq" className="text-accent-peach hover:text-deep-sage transition-colors">
                  FAQ
                </Link>
              </div>
            </div>
          </div>
          
          {/* Common Issues */}
          <div className="mt-8 bg-white rounded-xl p-6">
            <h3 className="font-medium text-deep-sage mb-4">Common payment issues:</h3>
            <ul className="space-y-3 text-sm text-sage-green">
              <li className="flex items-start gap-2">
                <span className="material-symbols-outlined text-accent-peach mt-0.5">info</span>
                <span>Insufficient funds or card limit reached</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="material-symbols-outlined text-accent-peach mt-0.5">info</span>
                <span>Incorrect card details or expired card</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="material-symbols-outlined text-accent-peach mt-0.5">info</span>
                <span>Payment declined by your bank - contact them for details</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="material-symbols-outlined text-accent-peach mt-0.5">info</span>
                <span>3D Secure verification failed or cancelled</span>
              </li>
            </ul>
          </div>
          
          {/* Continue Shopping */}
          <div className="mt-8 text-center">
            <Link 
              href="/products" 
              className="inline-flex items-center text-accent-peach hover:text-deep-sage transition-colors"
            >
              <span className="material-symbols-outlined mr-1">arrow_back</span>
              Continue Shopping
            </Link>
          </div>
        </div>
      </main>
    </div>
  )
}
