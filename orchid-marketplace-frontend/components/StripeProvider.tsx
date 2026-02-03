'use client'

import { loadStripe, Stripe } from '@stripe/stripe-js'
import { Elements } from '@stripe/react-stripe-js'
import { ReactNode, useEffect, useState } from 'react'

// Initialize Stripe
let stripePromise: Promise<Stripe | null> | null = null

const getStripe = () => {
  if (!stripePromise) {
    const publishableKey = process.env.NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY
    if (!publishableKey) {
      console.error('Stripe publishable key not found in environment variables')
      return null
    }
    stripePromise = loadStripe(publishableKey)
  }
  return stripePromise
}

interface StripeProviderProps {
  children: ReactNode
  clientSecret?: string
}

export default function StripeProvider({ children, clientSecret }: StripeProviderProps) {
  const [stripe, setStripe] = useState<Promise<Stripe | null> | null>(null)

  useEffect(() => {
    const stripeInstance = getStripe()
    setStripe(stripeInstance)
  }, [])

  if (!stripe) {
    return <div className="text-center py-8">Loading payment system...</div>
  }

  const options = clientSecret
    ? {
        clientSecret,
        appearance: {
          theme: 'stripe' as const,
          variables: {
            colorPrimary: '#4A5D48', // deep-sage
            colorBackground: '#FFF9F3', // warm-cream
            colorText: '#4A5D48',
            colorDanger: '#E74C3C',
            fontFamily: '"Plus Jakarta Sans", system-ui, sans-serif',
            spacingUnit: '4px',
            borderRadius: '8px',
          },
        },
      }
    : {
        appearance: {
          theme: 'stripe' as const,
          variables: {
            colorPrimary: '#4A5D48',
            colorBackground: '#FFF9F3',
            colorText: '#4A5D48',
            colorDanger: '#E74C3C',
            fontFamily: '"Plus Jakarta Sans", system-ui, sans-serif',
            spacingUnit: '4px',
            borderRadius: '8px',
          },
        },
      }

  return (
    <Elements stripe={stripe} options={options}>
      {children}
    </Elements>
  )
}
