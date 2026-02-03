'use client'

import { useEffect, useState } from 'react'
import { useSearchParams } from 'next/navigation'
import Link from 'next/link'
import SimpleNav from '@/components/SimpleNav'
import { apiClient } from '@/lib/api-client'

export default function CheckoutSuccessPage() {
  const searchParams = useSearchParams()
  const [orderDetails, setOrderDetails] = useState<any>(null)
  const [isLoading, setIsLoading] = useState(true)
  
  const orderId = searchParams.get('order_id')
  
  useEffect(() => {
    if (orderId) {
      fetchOrderDetails()
    } else {
      setIsLoading(false)
    }
  }, [orderId])
  
  const fetchOrderDetails = async () => {
    try {
      const order = await apiClient.getOrder(orderId!)
      setOrderDetails(order)
    } catch (error) {
      console.error('Error fetching order:', error)
    } finally {
      setIsLoading(false)
    }
  }
  
  return (
    <div className="min-h-screen bg-warm-cream">
      <SimpleNav />
      
      <main className="container mx-auto px-4 py-16">
        <div className="max-w-2xl mx-auto">
          {/* Success Icon */}
          <div className="bg-white rounded-2xl shadow-xl p-8 md:p-12 text-center">
            <div className="inline-flex items-center justify-center w-20 h-20 bg-green-100 rounded-full mb-6">
              <span className="material-symbols-outlined text-5xl text-green-600">
                check_circle
              </span>
            </div>
            
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-4">
              Order Confirmed!
            </h1>
            
            <p className="text-sage-green text-lg mb-8">
              Thank you for your purchase. Your order has been received and is being processed.
            </p>
            
            {isLoading ? (
              <div className="flex justify-center py-8">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-accent-peach"></div>
              </div>
            ) : orderDetails ? (
              <div className="bg-soft-peach rounded-xl p-6 mb-8 text-left">
                <div className="flex items-center justify-between mb-4 pb-4 border-b border-primary-peach">
                  <div>
                    <p className="text-sm text-sage-green mb-1">Order Number</p>
                    <p className="font-medium text-deep-sage text-lg">#{orderDetails.orderNumber || orderId}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm text-sage-green mb-1">Total</p>
                    <p className="font-bold text-deep-sage text-xl">${orderDetails.totalAmount?.toFixed(2)}</p>
                  </div>
                </div>
                
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-sage-green">Status:</span>
                    <span className="font-medium text-deep-sage capitalize">{orderDetails.status || 'Processing'}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-sage-green">Items:</span>
                    <span className="font-medium text-deep-sage">{orderDetails.items?.length || 0} items</span>
                  </div>
                  {orderDetails.shippingAddress && (
                    <div className="flex justify-between">
                      <span className="text-sage-green">Shipping to:</span>
                      <span className="font-medium text-deep-sage text-right">
                        {orderDetails.shippingAddress.city}, {orderDetails.shippingAddress.state}
                      </span>
                    </div>
                  )}
                </div>
              </div>
            ) : null}
            
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                href="/orders"
                className="inline-flex items-center justify-center px-8 py-3 bg-deep-sage text-white rounded-full font-medium hover:bg-sage-green transition-all shadow-lg hover:shadow-xl"
              >
                <span className="material-symbols-outlined mr-2">receipt_long</span>
                View Order Details
              </Link>
              <Link
                href="/"
                className="inline-flex items-center justify-center px-8 py-3 bg-white text-deep-sage rounded-full font-medium hover:bg-soft-peach transition-all border-2 border-sage-green"
              >
                <span className="material-symbols-outlined mr-2">home</span>
                Continue Shopping
              </Link>
            </div>
            
            {/* Order Confirmation Email Notice */}
            <div className="mt-8 pt-8 border-t border-gray-200">
              <div className="flex items-start gap-3 text-left">
                <span className="material-symbols-outlined text-accent-peach mt-1">mail</span>
                <div>
                  <p className="font-medium text-deep-sage mb-1">Check your email</p>
                  <p className="text-sm text-sage-green">
                    We've sent a confirmation email with your order details and tracking information to your registered email address.
                  </p>
                </div>
              </div>
            </div>
          </div>
          
          {/* What's Next Section */}
          <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="bg-white rounded-xl p-6 text-center">
              <span className="material-symbols-outlined text-4xl text-deep-sage mb-3">
                inventory_2
              </span>
              <h3 className="font-medium text-deep-sage mb-2">Processing</h3>
              <p className="text-sm text-sage-green">
                Your order is being prepared by our sellers
              </p>
            </div>
            
            <div className="bg-white rounded-xl p-6 text-center">
              <span className="material-symbols-outlined text-4xl text-deep-sage mb-3">
                local_shipping
              </span>
              <h3 className="font-medium text-deep-sage mb-2">Shipping</h3>
              <p className="text-sm text-sage-green">
                You'll receive tracking info once shipped
              </p>
            </div>
            
            <div className="bg-white rounded-xl p-6 text-center">
              <span className="material-symbols-outlined text-4xl text-deep-sage mb-3">
                deployed_code
              </span>
              <h3 className="font-medium text-deep-sage mb-2">Delivery</h3>
              <p className="text-sm text-sage-green">
                Estimated delivery in 5-7 business days
              </p>
            </div>
          </div>
        </div>
      </main>
    </div>
  )
}
