'use client'

import { useState, FormEvent } from 'react'
import { useStripe, useElements, PaymentElement, CardElement } from '@stripe/react-stripe-js'

interface StripePaymentFormProps {
  amount: number
  onSuccess?: (paymentIntentId: string) => void
  onError?: (error: string) => void
  usePaymentElement?: boolean // Use new PaymentElement (recommended) or legacy CardElement
}

export default function StripePaymentForm({ 
  amount, 
  onSuccess, 
  onError,
  usePaymentElement = true 
}: StripePaymentFormProps) {
  const stripe = useStripe()
  const elements = useElements()
  
  const [isProcessing, setIsProcessing] = useState(false)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)
  const [paymentComplete, setPaymentComplete] = useState(false)

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()

    if (!stripe || !elements) {
      setErrorMessage('Payment system not loaded. Please refresh the page.')
      return
    }

    setIsProcessing(true)
    setErrorMessage(null)

    try {
      // Confirm the payment
      const { error, paymentIntent } = await stripe.confirmPayment({
        elements,
        confirmParams: {
          return_url: `${window.location.origin}/checkout/success`,
        },
        redirect: 'if_required', // Only redirect if 3D Secure is needed
      })

      if (error) {
        // Payment failed
        setErrorMessage(error.message || 'Payment failed. Please try again.')
        onError?.(error.message || 'Payment failed')
      } else if (paymentIntent && paymentIntent.status === 'succeeded') {
        // Payment succeeded
        setPaymentComplete(true)
        onSuccess?.(paymentIntent.id)
      }
    } catch (err: any) {
      setErrorMessage(err.message || 'An unexpected error occurred.')
      onError?.(err.message)
    } finally {
      setIsProcessing(false)
    }
  }

  if (paymentComplete) {
    return (
      <div className="text-center py-8">
        <div className="w-16 h-16 bg-green-50 rounded-full flex items-center justify-center mx-auto mb-4">
          <span className="material-symbols-outlined text-4xl text-green-500">
            check_circle
          </span>
        </div>
        <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
          Payment Successful!
        </h3>
        <p className="text-sage-green">
          Redirecting to confirmation...
        </p>
      </div>
    )
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {/* Payment Element (includes all payment methods) */}
      <div className="bg-warm-cream p-4 rounded-lg border border-gray-200">
        {usePaymentElement ? (
          <PaymentElement />
        ) : (
          <CardElement
            options={{
              style: {
                base: {
                  fontSize: '16px',
                  color: '#4A5D48',
                  fontFamily: '"Plus Jakarta Sans", system-ui, sans-serif',
                  '::placeholder': {
                    color: '#8BA888',
                  },
                },
                invalid: {
                  color: '#E74C3C',
                },
              },
            }}
          />
        )}
      </div>

      {/* Error Message */}
      {errorMessage && (
        <div className="p-4 bg-red-50 border border-red-200 rounded-lg">
          <div className="flex items-start gap-3">
            <span className="material-symbols-outlined text-red-500">error</span>
            <p className="text-sm text-red-600">{errorMessage}</p>
          </div>
        </div>
      )}

      {/* Amount Display */}
      <div className="flex items-center justify-between p-4 bg-white rounded-lg border border-gray-200">
        <span className="text-sage-green">Total Amount:</span>
        <span className="font-playfair text-2xl font-bold text-deep-sage">
          ${amount.toFixed(2)}
        </span>
      </div>

      {/* Submit Button */}
      <button
        type="submit"
        disabled={!stripe || isProcessing}
        className="w-full bg-deep-sage text-white py-4 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
      >
        {isProcessing ? (
          <>
            <span className="material-symbols-outlined animate-spin">progress_activity</span>
            Processing...
          </>
        ) : (
          <>
            <span className="material-symbols-outlined">lock</span>
            Pay ${amount.toFixed(2)}
          </>
        )}
      </button>

      {/* Security Notice */}
      <div className="flex items-center justify-center gap-2 text-sm text-sage-green">
        <span className="material-symbols-outlined text-lg">verified_user</span>
        <span>Secured by Stripe • PCI DSS Compliant</span>
      </div>
    </form>
  )
}
