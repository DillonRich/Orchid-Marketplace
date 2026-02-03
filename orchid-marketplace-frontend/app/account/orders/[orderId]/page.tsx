'use client'

import { useState, useEffect } from 'react'
import { useParams } from 'next/navigation'
import Link from 'next/link'
import Image from 'next/image'
// import OrderTimeline, { orderStatusPresets, type OrderTimelineEvent } from '@/components/OrderTimeline'

export default function OrderDetailPage() {
  const params = useParams()
  const orderId = params.orderId as string
  
  const [order, setOrder] = useState<any>(null)
  
  useEffect(() => {
    // Mock order data
    setOrder({
      id: orderId,
      orderNumber: `ORD-${orderId.slice(0, 8).toUpperCase()}`,
      status: 'In Transit',
      date: '2026-01-18',
      items: [
        {
          id: '1',
          title: 'Beautiful Phalaenopsis Orchid',
          imageUrl: '/placeholder.jpg',
          quantity: 2,
          price: 45.99,
          seller: 'Orchid Paradise',
        },
      ],
      timeline: [
        { label: 'Order Placed', date: '2026-01-18, 10:30 AM', completed: true },
        { label: 'Payment Confirmed', date: '2026-01-18, 10:31 AM', completed: true },
        { label: 'Processing', date: '2026-01-18, 2:00 PM', completed: true },
        { label: 'Shipped', date: '2026-01-19, 9:00 AM', completed: true },
        { label: 'In Transit', date: 'Expected: 2026-01-23', completed: false },
        { label: 'Delivered', date: '', completed: false },
      ],
      shippingAddress: {
        name: 'John Doe',
        street: '123 Main St',
        city: 'New York',
        state: 'NY',
        zip: '10001',
      },
      paymentMethod: {
        brand: 'Visa',
        last4: '4242',
      },
      trackingNumber: '1Z999AA10123456784',
      subtotal: 91.98,
      tax: 7.36,
      shipping: 0,
      total: 99.34,
    })
  }, [orderId])
  
  if (!order) {
    return (
      <div className="bg-white rounded-xl p-12 text-center">
        <div className="w-12 h-12 border-4 border-accent-peach border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
        <p className="text-sage-green">Loading order...</p>
      </div>
    )
  }
  
  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <div className="flex items-center gap-2 text-sm text-sage-green mb-4">
          <Link href="/account/orders" className="hover:text-deep-sage">
            Orders
          </Link>
          <span className="material-symbols-outlined text-sm">chevron_right</span>
          <span className="text-deep-sage">{order.orderNumber}</span>
        </div>
        
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
              Order {order.orderNumber}
            </h1>
            <p className="text-sage-green">
              Placed on {new Date(order.date).toLocaleDateString()}
            </p>
          </div>
          
          <span className={`self-start px-4 py-2 rounded-full font-medium ${
            order.status === 'Delivered'
              ? 'bg-green-100 text-green-700'
              : order.status === 'In Transit'
              ? 'bg-blue-100 text-blue-700'
              : 'bg-yellow-100 text-yellow-700'
          }`}>
            {order.status}
          </span>
        </div>
      </div>
      
      {/* Order Timeline */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Order Progress</h2>
        <div className="relative">
          {order.timeline.map((step: any, idx: number) => (
            <div key={idx} className="flex gap-4 pb-8 last:pb-0">
              <div className="flex flex-col items-center">
                <div className={`w-10 h-10 rounded-full flex items-center justify-center ${
                  step.completed ? 'bg-deep-sage' : 'bg-gray-200'
                }`}>
                  {step.completed ? (
                    <span className="material-symbols-outlined text-white text-xl">
                      check
                    </span>
                  ) : (
                    <span className="material-symbols-outlined text-sage-green text-xl">
                      schedule
                    </span>
                  )}
                </div>
                {idx < order.timeline.length - 1 && (
                  <div className={`w-0.5 h-full my-2 ${
                    step.completed ? 'bg-deep-sage' : 'bg-gray-200'
                  }`} />
                )}
              </div>
              <div className="flex-1 pt-2">
                <p className={`font-medium mb-1 ${
                  step.completed ? 'text-deep-sage' : 'text-sage-green'
                }`}>
                  {step.label}
                </p>
                <p className="text-sm text-sage-green">{step.date}</p>
              </div>
            </div>
          ))}
        </div>
        
        {order.trackingNumber && (
          <div className="mt-6 pt-6 border-t border-gray-200">
            <p className="text-sm text-sage-green mb-2">Tracking Number</p>
            <p className="font-medium text-deep-sage mb-4">{order.trackingNumber}</p>
            <button className="bg-deep-sage text-white py-2 px-6 rounded-full font-medium hover:bg-sage-green transition-all">
              Track Package
            </button>
          </div>
        )}
      </div>
      
      {/* Order Items */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Order Items</h2>
        <div className="space-y-4">
          {order.items.map((item: any) => (
            <div key={item.id} className="flex gap-4 pb-4 border-b border-gray-200 last:border-0">
              <div className="relative w-24 h-24 rounded-lg overflow-hidden bg-gray-100 flex-shrink-0">
                <Image src={item.imageUrl} alt={item.title} fill className="object-cover" />
              </div>
              <div className="flex-1">
                <Link
                  href={`/product/${item.id}`}
                  className="font-medium text-deep-sage hover:text-accent-peach mb-1 block"
                >
                  {item.title}
                </Link>
                <p className="text-sm text-sage-green mb-2">by {item.seller}</p>
                <p className="text-sm text-sage-green">Qty: {item.quantity}</p>
              </div>
              <div className="text-right">
                <p className="font-playfair text-xl font-bold text-deep-sage">
                  ${(item.price * item.quantity).toFixed(2)}
                </p>
                <p className="text-sm text-sage-green">${item.price.toFixed(2)} each</p>
              </div>
            </div>
          ))}
        </div>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Shipping Address */}
        <div className="bg-white rounded-xl p-8">
          <h3 className="font-medium text-deep-sage mb-4 flex items-center gap-2">
            <span className="material-symbols-outlined">local_shipping</span>
            Shipping Address
          </h3>
          <div className="text-sage-green">
            <p className="font-medium text-deep-sage">{order.shippingAddress.name}</p>
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
          <p className="text-sage-green">
            {order.paymentMethod.brand} ending in {order.paymentMethod.last4}
          </p>
        </div>
      </div>
      
      {/* Order Summary */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Order Summary</h2>
        <div className="space-y-3 max-w-md">
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
          <div className="flex justify-between pt-3 border-t border-gray-200">
            <span className="font-playfair text-xl font-bold text-deep-sage">Total</span>
            <span className="font-playfair text-2xl font-bold text-deep-sage">
              ${order.total.toFixed(2)}
            </span>
          </div>
        </div>
      </div>
      
      {/* Actions */}
      <div className="flex flex-wrap gap-4">
        <button className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all">
          Contact Seller
        </button>
        <button className="border-2 border-deep-sage text-deep-sage py-3 px-8 rounded-full font-medium hover:bg-soft-peach transition-all">
          Download Invoice
        </button>
        {order.status === 'Delivered' && (
          <button className="border-2 border-deep-sage text-deep-sage py-3 px-8 rounded-full font-medium hover:bg-soft-peach transition-all">
            Write Review
          </button>
        )}
      </div>
    </div>
  )
}
