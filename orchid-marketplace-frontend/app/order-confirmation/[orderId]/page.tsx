'use client'

import { useState, useEffect } from 'react'
import { useParams } from 'next/navigation'
import Link from 'next/link'
import Image from 'next/image'

export default function OrderConfirmationPage() {
  const params = useParams()
  const orderId = params.orderId as string
  
  const [order, setOrder] = useState<any>(null)
  const [isLoading, setIsLoading] = useState(true)
  
  useEffect(() => {
    // Fetch order details (mock for now)
    setTimeout(() => {
      setOrder({
        id: orderId,
        orderNumber: `ORD-${orderId.slice(0, 8).toUpperCase()}`,
        status: 'PAID',
        createdAt: new Date().toISOString(),
        estimatedDelivery: new Date(Date.now() + 5 * 24 * 60 * 60 * 1000).toLocaleDateString(),
        items: [
          {
            id: '1',
            title: 'Beautiful Phalaenopsis Orchid',
            imageUrl: '/placeholder.jpg',
            quantity: 2,
            price: 45.99,
          },
        ],
        shippingAddress: {
          firstName: 'John',
          lastName: 'Doe',
          street: '123 Main St',
          city: 'New York',
          state: 'NY',
          zip: '10001',
        },
        paymentMethod: {
          brand: 'Visa',
          last4: '4242',
        },
        subtotal: 91.98,
        tax: 7.36,
        shipping: 0,
        total: 99.34,
      })
      setIsLoading(false)
    }, 1000)
  }, [orderId])
  
  if (isLoading) {
    return (
      <div className="min-h-screen bg-warm-cream flex items-center justify-center">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-accent-peach border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-sage-green">Loading order details...</p>
        </div>
      </div>
    )
  }
  
  if (!order) {
    return (
      <div className="min-h-screen bg-warm-cream flex flex-col items-center justify-center p-4">
        <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
          receipt_long
        </span>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-4">
          Order not found
        </h1>
        <Link
          href="/"
          className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
        >
          Return to Home
        </Link>
      </div>
    )
  }
  
  return (
    <div className="min-h-screen bg-warm-cream">
      {/* Simplified Header */}
      <header className="bg-white border-b border-gray-200">
        <div className="container mx-auto px-4 lg:px-24 py-6">
          <Link href="/" className="font-playfair text-3xl font-bold text-deep-sage">
            Orchidillo
          </Link>
        </div>
      </header>
      
      <main className="container mx-auto px-4 lg:px-24 py-16">
        {/* Success Message */}
        <div className="text-center mb-12">
          <div className="w-24 h-24 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
            <span className="material-symbols-outlined text-6xl text-green-600">
              check_circle
            </span>
          </div>
          
          <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-4">
            Order Confirmed!
          </h1>
          
          <p className="text-sage-green text-lg mb-2">
            Thank you for your purchase
          </p>
          
          <p className="text-sage-green">
            Order number: <span className="font-medium text-deep-sage">{order.orderNumber}</span>
          </p>
          
          <p className="text-sage-green mt-4">
            Estimated delivery: <span className="font-medium text-deep-sage">{order.estimatedDelivery}</span>
          </p>
        </div>
        
        <div className="max-w-4xl mx-auto grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Order Details */}
          <div className="bg-white rounded-xl p-8">
            <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
              Order Details
            </h2>
            
            {/* Items */}
            <div className="space-y-4 mb-6 pb-6 border-b border-gray-200">
              {order.items.map((item: any) => (
                <div key={item.id} className="flex gap-4">
                  <div className="relative w-20 h-20 flex-shrink-0 rounded-lg overflow-hidden bg-gray-100">
                    <Image
                      src={item.imageUrl}
                      alt={item.title}
                      fill
                      className="object-cover"
                    />
                  </div>
                  <div className="flex-1">
                    <p className="font-medium text-deep-sage mb-1">{item.title}</p>
                    <p className="text-sm text-sage-green mb-1">Qty: {item.quantity}</p>
                    <p className="font-medium text-deep-sage">
                      ${(item.price * item.quantity).toFixed(2)}
                    </p>
                  </div>
                </div>
              ))}
            </div>
            
            {/* Summary */}
            <div className="space-y-2 mb-6 pb-6 border-b border-gray-200">
              <div className="flex justify-between text-sage-green">
                <span>Subtotal</span>
                <span>${order.subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-sage-green">
                <span>Tax</span>
                <span>${order.tax.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-sage-green">
                <span>Shipping</span>
                <span>{order.shipping === 0 ? 'FREE' : `$${order.shipping.toFixed(2)}`}</span>
              </div>
            </div>
            
            <div className="flex justify-between">
              <span className="font-playfair text-xl font-bold text-deep-sage">Total</span>
              <span className="font-playfair text-2xl font-bold text-deep-sage">
                ${order.total.toFixed(2)}
              </span>
            </div>
          </div>
          
          {/* Shipping & Payment Info */}
          <div className="space-y-6">
            {/* Shipping Address */}
            <div className="bg-white rounded-xl p-8">
              <h3 className="font-medium text-deep-sage mb-4 flex items-center gap-2">
                <span className="material-symbols-outlined">local_shipping</span>
                Shipping Address
              </h3>
              <div className="text-sage-green">
                <p className="font-medium text-deep-sage">
                  {order.shippingAddress.firstName} {order.shippingAddress.lastName}
                </p>
                <p>{order.shippingAddress.street}</p>
                <p>
                  {order.shippingAddress.city}, {order.shippingAddress.state} {order.shippingAddress.zip}
                </p>
              </div>
            </div>
            
            {/* Payment Method */}
            <div className="bg-white rounded-xl p-8">
              <h3 className="font-medium text-deep-sage mb-4 flex items-center gap-2">
                <span className="material-symbols-outlined">credit_card</span>
                Payment Method
              </h3>
              <div className="text-sage-green">
                <p>
                  {order.paymentMethod.brand} ending in {order.paymentMethod.last4}
                </p>
              </div>
            </div>
            
            {/* Order Status */}
            <div className="bg-soft-peach rounded-xl p-8">
              <h3 className="font-medium text-deep-sage mb-4 flex items-center gap-2">
                <span className="material-symbols-outlined">schedule</span>
                What's Next?
              </h3>
              <ul className="space-y-3 text-sm text-sage-green">
                <li className="flex items-start gap-2">
                  <span className="material-symbols-outlined text-accent-peach text-lg">check</span>
                  <span>Order confirmation sent to your email</span>
                </li>
                <li className="flex items-start gap-2">
                  <span className="material-symbols-outlined text-sage-green text-lg">schedule</span>
                  <span>We'll notify you when your order ships</span>
                </li>
                <li className="flex items-start gap-2">
                  <span className="material-symbols-outlined text-sage-green text-lg">local_shipping</span>
                  <span>Track your package in real-time</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
        
        {/* Action Buttons */}
        <div className="max-w-4xl mx-auto mt-12 flex flex-col sm:flex-row gap-4 justify-center">
          <Link
            href={`/account/orders/${order.id}`}
            className="bg-deep-sage text-white py-4 px-8 rounded-full font-medium hover:bg-sage-green transition-all text-center"
          >
            View Order Details
          </Link>
          
          <Link
            href="/products"
            className="bg-white border-2 border-deep-sage text-deep-sage py-4 px-8 rounded-full font-medium hover:bg-soft-peach transition-all text-center"
          >
            Continue Shopping
          </Link>
        </div>
      </main>
    </div>
  )
}
