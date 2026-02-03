'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import EmptyState from '@/components/EmptyState'
// import LoadingSkeleton from '@/components/LoadingSkeleton'

export default function OrdersPage() {
  const [orders, setOrders] = useState<any[]>([])
  const [filter, setFilter] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')
  
  useEffect(() => {
    // Mock orders data
    setOrders([
      {
        id: '1',
        orderNumber: 'ORD-12345',
        date: '2026-01-20',
        status: 'Delivered',
        total: 99.99,
        items: 3,
        itemsPreview: ['/placeholder.jpg'],
      },
      {
        id: '2',
        orderNumber: 'ORD-12344',
        date: '2026-01-18',
        status: 'In Transit',
        total: 145.50,
        items: 2,
        itemsPreview: ['/placeholder.jpg', '/placeholder.jpg'],
      },
      {
        id: '3',
        orderNumber: 'ORD-12343',
        date: '2026-01-15',
        status: 'Processing',
        total: 75.00,
        items: 1,
        itemsPreview: ['/placeholder.jpg'],
      },
    ])
  }, [])
  
  const filteredOrders = orders.filter(order => {
    const matchesFilter = filter === 'all' || order.status.toLowerCase().replace(' ', '_') === filter
    const matchesSearch = order.orderNumber.toLowerCase().includes(searchQuery.toLowerCase())
    return matchesFilter && matchesSearch
  })
  
  const statusOptions = [
    { value: 'all', label: 'All Orders' },
    { value: 'processing', label: 'Processing' },
    { value: 'in_transit', label: 'In Transit' },
    { value: 'delivered', label: 'Delivered' },
    { value: 'cancelled', label: 'Cancelled' },
  ]
  
  return (
    <div className="space-y-8">
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          My Orders
        </h1>
        <p className="text-sage-green">
          View and track your order history
        </p>
      </div>
      
      {/* Filters */}
      <div className="bg-white rounded-xl p-6">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Search by order number..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          <div className="flex gap-2 overflow-x-auto">
            {statusOptions.map(option => (
              <button
                key={option.value}
                onClick={() => setFilter(option.value)}
                className={`px-4 py-3 rounded-lg font-medium whitespace-nowrap transition-all ${
                  filter === option.value
                    ? 'bg-deep-sage text-white'
                    : 'bg-gray-100 text-sage-green hover:bg-soft-peach'
                }`}
              >
                {option.label}
              </button>
            ))}
          </div>
        </div>
      </div>
      
      {/* Orders List */}
      {filteredOrders.length === 0 ? (
        <EmptyState
          icon="shopping_bag"
          title="No orders found"
          description={searchQuery ? 'Try a different search term' : 'Start shopping to see your orders here'}
          actionLabel="Browse Products"
          actionHref="/products"
        />
      ) : (
        <div className="space-y-4">
          {filteredOrders.map(order => (
            <div
              key={order.id}
              className="bg-white rounded-xl p-6 hover:shadow-lg transition-all"
            >
              <div className="flex flex-col md:flex-row md:items-center gap-6">
                {/* Order Images */}
                <div className="flex gap-2">
                  {order.itemsPreview.map((img: string, idx: number) => (
                    <div
                      key={idx}
                      className="w-20 h-20 rounded-lg bg-gray-100 flex-shrink-0"
                      style={{ backgroundImage: `url(${img})`, backgroundSize: 'cover' }}
                    />
                  ))}
                  {order.items > order.itemsPreview.length && (
                    <div className="w-20 h-20 rounded-lg bg-soft-peach flex items-center justify-center text-deep-sage font-medium">
                      +{order.items - order.itemsPreview.length}
                    </div>
                  )}
                </div>
                
                {/* Order Info */}
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="font-medium text-deep-sage text-lg">
                      {order.orderNumber}
                    </h3>
                    <span className={`text-xs px-3 py-1 rounded-full ${
                      order.status === 'Delivered'
                        ? 'bg-green-100 text-green-700'
                        : order.status === 'In Transit'
                        ? 'bg-blue-100 text-blue-700'
                        : order.status === 'Processing'
                        ? 'bg-yellow-100 text-yellow-700'
                        : 'bg-red-100 text-red-700'
                    }`}>
                      {order.status}
                    </span>
                  </div>
                  <p className="text-sm text-sage-green mb-1">
                    Placed on {new Date(order.date).toLocaleDateString()}
                  </p>
                  <p className="text-sm text-sage-green">
                    {order.items} {order.items === 1 ? 'item' : 'items'}
                  </p>
                </div>
                
                {/* Total & Actions */}
                <div className="flex items-center gap-4">
                  <div className="text-right">
                    <p className="text-sm text-sage-green mb-1">Total</p>
                    <p className="font-playfair text-2xl font-bold text-deep-sage">
                      ${order.total.toFixed(2)}
                    </p>
                  </div>
                  <Link
                    href={`/account/orders/${order.id}`}
                    className="bg-deep-sage text-white px-6 py-3 rounded-full font-medium hover:bg-sage-green transition-all"
                  >
                    View Details
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
