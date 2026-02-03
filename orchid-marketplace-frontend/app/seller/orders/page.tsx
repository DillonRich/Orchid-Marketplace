'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'

interface OrderItem {
  id: string
  productId: string
  productTitle: string
  productImage: string
  quantity: number
  priceAtPurchase: number
}

interface Order {
  id: string
  orderNumber: string
  customerId: string
  customerName: string
  customerEmail: string
  status: string
  totalAmount: number
  createdAt: string
  items: OrderItem[]
}

export default function SellerOrdersPage() {
  const { user } = useAuthStore()
  const [orders, setOrders] = useState<Order[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [filter, setFilter] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')
  
  useEffect(() => {
    if (user?.storeId) {
      fetchOrders()
    }
  }, [user, filter])

  const fetchOrders = async () => {
    try {
      setIsLoading(true)
      const statusFilter = filter === 'all' ? undefined : filter
      const data = await apiClient.getSellerOrders(user?.storeId || '', statusFilter)
      setOrders(data)
    } catch (err) {
      console.error('Failed to fetch orders:', err)
    } finally {
      setIsLoading(false)
    }
  }
  
  const filteredOrders = orders.filter(order => {
    const matchesFilter = filter === 'all' || order.status === filter
    const matchesSearch = 
      order.orderNumber.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.customerName.toLowerCase().includes(searchQuery.toLowerCase())
    
    return matchesFilter && matchesSearch
  })
  
  const filterOptions = [
    { value: 'all', label: 'All Orders', count: orders.length },
    { value: 'pending', label: 'Pending', count: orders.filter(o => o.status === 'pending').length },
    { value: 'processing', label: 'Processing', count: orders.filter(o => o.status === 'processing').length },
    { value: 'shipped', label: 'Shipped', count: orders.filter(o => o.status === 'shipped').length },
    { value: 'delivered', label: 'Delivered', count: orders.filter(o => o.status === 'delivered').length },
    { value: 'cancelled', label: 'Cancelled', count: orders.filter(o => o.status === 'cancelled').length },
  ]
  
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'pending': return 'bg-yellow-100 text-yellow-700'
      case 'processing': return 'bg-blue-100 text-blue-700'
      case 'shipped': return 'bg-purple-100 text-purple-700'
      case 'delivered': return 'bg-green-100 text-green-700'
      case 'cancelled': return 'bg-red-100 text-red-700'
      default: return 'bg-gray-100 text-gray-700'
    }
  }

  const exportToCSV = () => {
    const headers = ['Order Number', 'Customer', 'Email', 'Products', 'Total', 'Status', 'Date']
    const csvData = filteredOrders.map(order => [
      order.orderNumber,
      order.customerName,
      order.customerEmail,
      order.items.map(item => `${item.productTitle} (${item.quantity})`).join('; '),
      order.totalAmount.toFixed(2),
      order.status,
      new Date(order.createdAt).toLocaleDateString()
    ])

    const csv = [
      headers.join(','),
      ...csvData.map(row => row.map(cell => `"${cell}"`).join(','))
    ].join('\n')

    const blob = new Blob([csv], { type: 'text/csv' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `orders-${new Date().toISOString().split('T')[0]}.csv`
    a.click()
    window.URL.revokeObjectURL(url)
  }

  if (isLoading) {
    return (
      <div className="space-y-8">
        <div className="animate-pulse">
          <div className="h-12 bg-gray-200 rounded w-1/4 mb-2"></div>
          <div className="h-6 bg-gray-200 rounded w-1/3"></div>
        </div>
        <div className="bg-white rounded-xl p-6 h-64 animate-pulse"></div>
      </div>
    )
  }
  
  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
            Orders
          </h1>
          <p className="text-sage-green">
            Manage and fulfill customer orders
          </p>
        </div>
        <button
          onClick={exportToCSV}
          className="flex items-center gap-2 px-4 py-2 bg-deep-sage text-white rounded-lg hover:bg-sage-green transition-all"
        >
          <span className="material-symbols-outlined text-sm">download</span>
          Export CSV
        </button>
      </div>
      
      {/* Filters */}
      <div className="bg-white rounded-xl p-6">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Search by order number or customer..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          <div className="flex gap-2 overflow-x-auto">
            {filterOptions.map(option => (
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
                <span className="ml-2 text-sm opacity-75">
                  {option.count}
                </span>
              </button>
            ))}
          </div>
        </div>
      </div>
      
      {/* Orders Table */}
      {filteredOrders.length > 0 ? (
        <div className="bg-white rounded-xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-soft-peach">
                <tr className="text-left text-sm text-deep-sage">
                  <th className="px-6 py-4 font-medium">Order</th>
                  <th className="px-6 py-4 font-medium">Customer</th>
                  <th className="px-6 py-4 font-medium">Products</th>
                  <th className="px-6 py-4 font-medium">Items</th>
                  <th className="px-6 py-4 font-medium">Total</th>
                  <th className="px-6 py-4 font-medium">Status</th>
                  <th className="px-6 py-4 font-medium">Date</th>
                  <th className="px-6 py-4 font-medium">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredOrders.map(order => (
                  <tr key={order.id} className="border-b border-gray-200 hover:bg-soft-peach transition-all">
                    <td className="px-6 py-4">
                      <Link
                        href={`/seller/orders/${order.id}`}
                        className="font-medium text-deep-sage hover:text-accent-peach"
                      >
                        {order.orderNumber}
                      </Link>
                    </td>
                    <td className="px-6 py-4">
                      <div>
                        <p className="text-deep-sage font-medium">{order.customerName}</p>
                        <p className="text-xs text-sage-green">{order.customerEmail}</p>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="text-sm">
                        {order.items.slice(0, 2).map((item, idx) => (
                          <p key={idx} className="text-sage-green">
                            {item.productTitle}
                          </p>
                        ))}
                        {order.items.length > 2 && (
                          <p className="text-xs text-accent-peach">+{order.items.length - 2} more</p>
                        )}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-sage-green">
                      {order.items.reduce((sum, item) => sum + item.quantity, 0)}
                    </td>
                    <td className="px-6 py-4 font-semibold text-deep-sage">
                      ${order.totalAmount.toFixed(2)}
                    </td>
                    <td className="px-6 py-4">
                      <span className={`text-xs px-3 py-1 rounded-full ${getStatusColor(order.status)}`}>
                        {order.status.charAt(0).toUpperCase() + order.status.slice(1)}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sage-green text-sm">
                      {new Date(order.createdAt).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4">
                      <Link
                        href={`/seller/orders/${order.id}`}
                        className="text-accent-peach hover:text-deep-sage"
                        title="View Details"
                      >
                        <span className="material-symbols-outlined">visibility</span>
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            shopping_cart
          </span>
          <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
            No orders found
          </h3>
          <p className="text-sage-green">
            {searchQuery ? 'Try a different search term' : 'Orders will appear here once customers make purchases'}
          </p>
        </div>
      )}
    </div>
  )
}
