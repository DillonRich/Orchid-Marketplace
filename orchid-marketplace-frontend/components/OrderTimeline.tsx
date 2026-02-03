'use client'

import React from 'react'

export interface OrderTimelineEvent {
  id: string
  title: string
  description: string
  timestamp: string
  status: 'completed' | 'current' | 'pending' | 'cancelled'
  icon?: string
}

interface OrderTimelineProps {
  events: OrderTimelineEvent[]
  className?: string
}

export default function OrderTimeline({ events, className = '' }: OrderTimelineProps) {
  const getStatusColor = (status: OrderTimelineEvent['status']) => {
    switch (status) {
      case 'completed':
        return 'bg-green-500'
      case 'current':
        return 'bg-blue-500 ring-4 ring-blue-100'
      case 'cancelled':
        return 'bg-red-500'
      default:
        return 'bg-gray-300'
    }
  }

  const getLineColor = (status: OrderTimelineEvent['status']) => {
    switch (status) {
      case 'completed':
        return 'bg-green-500'
      case 'current':
        return 'bg-gradient-to-b from-green-500 to-gray-300'
      default:
        return 'bg-gray-300'
    }
  }

  const getIcon = (event: OrderTimelineEvent, index: number) => {
    if (event.icon) return event.icon
    
    // Default icons based on common order statuses
    const defaultIcons = [
      'shopping_cart',
      'payment',
      'inventory_2',
      'local_shipping',
      'home',
    ]
    return defaultIcons[index] || 'circle'
  }

  const formatTimestamp = (timestamp: string) => {
    const date = new Date(timestamp)
    const now = new Date()
    const diffInHours = Math.floor((now.getTime() - date.getTime()) / (1000 * 60 * 60))

    if (diffInHours < 1) return 'Just now'
    if (diffInHours < 24) return `${diffInHours}h ago`
    
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: date.getFullYear() !== now.getFullYear() ? 'numeric' : undefined,
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  return (
    <div className={`relative ${className}`}>
      {events.map((event, index) => (
        <div key={event.id} className="relative pb-8 last:pb-0">
          {/* Connecting Line */}
          {index < events.length - 1 && (
            <div
              className={`absolute left-6 top-12 w-0.5 h-full -ml-px ${getLineColor(event.status)}`}
              aria-hidden="true"
            />
          )}

          <div className="relative flex items-start gap-4">
            {/* Icon Circle */}
            <div className="flex-shrink-0">
              <div
                className={`w-12 h-12 rounded-full flex items-center justify-center text-white transition-all ${getStatusColor(event.status)}`}
              >
                <span className="material-symbols-outlined text-xl">
                  {getIcon(event, index)}
                </span>
              </div>
            </div>

            {/* Content */}
            <div className="flex-1 min-w-0 pt-1">
              <div className="flex items-start justify-between gap-4 mb-1">
                <h4 className={`font-medium ${
                  event.status === 'completed' || event.status === 'current'
                    ? 'text-deep-sage'
                    : event.status === 'cancelled'
                    ? 'text-red-600'
                    : 'text-gray-400'
                }`}>
                  {event.title}
                  {event.status === 'current' && (
                    <span className="ml-2 text-xs font-normal text-blue-600 bg-blue-50 px-2 py-0.5 rounded-full">
                      In Progress
                    </span>
                  )}
                </h4>
                
                <time className="text-sm text-sage-green flex-shrink-0">
                  {event.status !== 'pending' && formatTimestamp(event.timestamp)}
                </time>
              </div>
              
              <p className={`text-sm ${
                event.status === 'pending' ? 'text-gray-400' : 'text-sage-green'
              }`}>
                {event.description}
              </p>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}

// Example order status presets
export const orderStatusPresets = {
  processing: [
    {
      id: '1',
      title: 'Order Placed',
      description: 'Your order has been received and is being processed',
      timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
      icon: 'check_circle',
    },
    {
      id: '2',
      title: 'Payment Confirmed',
      description: 'Payment has been successfully processed',
      timestamp: new Date(Date.now() - 1.5 * 60 * 60 * 1000).toISOString(),
      status: 'current' as const,
      icon: 'payment',
    },
    {
      id: '3',
      title: 'Preparing Shipment',
      description: 'Seller is preparing your items for shipment',
      timestamp: '',
      status: 'pending' as const,
      icon: 'inventory_2',
    },
    {
      id: '4',
      title: 'Shipped',
      description: 'Package is on its way to you',
      timestamp: '',
      status: 'pending' as const,
      icon: 'local_shipping',
    },
    {
      id: '5',
      title: 'Delivered',
      description: 'Package has been delivered',
      timestamp: '',
      status: 'pending' as const,
      icon: 'home',
    },
  ],
  
  shipped: (trackingNumber?: string) => [
    {
      id: '1',
      title: 'Order Placed',
      description: 'Your order has been received',
      timestamp: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
      icon: 'check_circle',
    },
    {
      id: '2',
      title: 'Payment Confirmed',
      description: 'Payment successfully processed',
      timestamp: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
      icon: 'payment',
    },
    {
      id: '3',
      title: 'Shipped',
      description: trackingNumber 
        ? `Tracking: ${trackingNumber}` 
        : 'Package is on its way',
      timestamp: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'current' as const,
      icon: 'local_shipping',
    },
    {
      id: '4',
      title: 'Out for Delivery',
      description: 'Package is out for delivery today',
      timestamp: '',
      status: 'pending' as const,
      icon: 'delivery_truck_speed',
    },
    {
      id: '5',
      title: 'Delivered',
      description: 'Package has been delivered',
      timestamp: '',
      status: 'pending' as const,
      icon: 'home',
    },
  ],
  
  delivered: [
    {
      id: '1',
      title: 'Order Placed',
      description: 'Order received',
      timestamp: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
    },
    {
      id: '2',
      title: 'Payment Confirmed',
      description: 'Payment processed',
      timestamp: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
    },
    {
      id: '3',
      title: 'Shipped',
      description: 'Package shipped',
      timestamp: new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
    },
    {
      id: '4',
      title: 'Out for Delivery',
      description: 'Package out for delivery',
      timestamp: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
    },
    {
      id: '5',
      title: 'Delivered',
      description: 'Package successfully delivered',
      timestamp: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
      status: 'completed' as const,
      icon: 'home',
    },
  ],
}
