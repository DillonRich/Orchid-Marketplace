"use client"

import { useEffect, useState } from 'react'
import Link from 'next/link'
import { useAuthStore } from '@/lib/auth-store'

export default function SellerDashboard() {
  const { user, isAuthenticated } = useAuthStore()
  const [stats, setStats] = useState({
    totalViews: 0,
    clicks: 0,
    orders: 0,
    revenue: 0,
  })
  
  const [openOrders, setOpenOrders] = useState<any[]>([])
  
  useEffect(() => {
    // TODO: Fetch real data from API
    // Placeholder data for now
    setStats({
      totalViews: 1247,
      clicks: 523,
      orders: 48,
      revenue: 2156.50,
    })
    
    setOpenOrders([
      { id: '1001', customer: 'John Doe', product: 'Purple Phalaenopsis', amount: 45.00, date: '2026-01-28', status: 'pending' },
      { id: '1002', customer: 'Jane Smith', product: 'White Orchid Set', amount: 89.99, date: '2026-01-29', status: 'processing' },
      { id: '1003', customer: 'Mike Johnson', product: 'Pink Cattleya', amount: 65.00, date: '2026-01-30', status: 'pending' },
    ])
  }, [])
  
  return (
    <div className="space-y-8">
      {/* Debug Info */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
        <h3 className="font-bold text-blue-900 mb-2">🔍 Debug Info (remove this later)</h3>
        <p className="text-sm text-blue-800">Authenticated: {isAuthenticated ? 'Yes' : 'No'}</p>
        <p className="text-sm text-blue-800">User Role: {user?.role || 'Not set'}</p>
        <p className="text-sm text-blue-800">User Email: {user?.email || 'N/A'}</p>
        <p className="text-sm text-blue-800">User Name: {user?.fullName || 'N/A'}</p>
      </div>
      
      {/* Header */}
      <div>
        <h1 className="text-3xl font-serif font-bold text-deep-sage mb-2">Dashboard</h1>
        <p className="text-sage-green">Welcome back! Here's what's happening with your shop.</p>
      </div>
      
      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Total Views */}
        <div className="bg-white rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between mb-3">
            <span className="material-symbols-outlined text-3xl text-purple-500">visibility</span>
            <span className="text-xs text-green-600 font-medium">+12% this week</span>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-1">
            {stats.totalViews.toLocaleString()}
          </div>
          <div className="text-sm text-sage-green">Total Views</div>
        </div>
        
        {/* Clicks / Visits */}
        <div className="bg-white rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between mb-3">
            <span className="material-symbols-outlined text-3xl text-blue-500">touch_app</span>
            <span className="text-xs text-green-600 font-medium">+8% this week</span>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-1">
            {stats.clicks.toLocaleString()}
          </div>
          <div className="text-sm text-sage-green">Clicks / Visits</div>
        </div>
        
        {/* Orders */}
        <div className="bg-white rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between mb-3">
            <span className="material-symbols-outlined text-3xl text-orange-500">shopping_bag</span>
            <span className="text-xs text-green-600 font-medium">+15% this week</span>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-1">
            {stats.orders.toLocaleString()}
          </div>
          <div className="text-sm text-sage-green">Orders</div>
        </div>
        
        {/* Revenue */}
        <div className="bg-white rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between mb-3">
            <span className="material-symbols-outlined text-3xl text-green-500">payments</span>
            <span className="text-xs text-green-600 font-medium">+20% this week</span>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-1">
            ${stats.revenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </div>
          <div className="text-sm text-sage-green">Total Revenue</div>
        </div>
      </div>
      
      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Link
          href="/seller/products/new"
          className="bg-deep-sage text-white rounded-xl p-6 hover:bg-sage-green transition-all flex items-center gap-4"
        >
          <span className="material-symbols-outlined text-4xl">add_circle</span>
          <div>
            <div className="font-bold text-lg">Add Product</div>
            <div className="text-sm opacity-90">List a new item</div>
          </div>
        </Link>
        
        <Link
          href="/seller/orders"
          className="bg-accent-peach text-white rounded-xl p-6 hover:bg-primary-peach transition-all flex items-center gap-4"
        >
          <span className="material-symbols-outlined text-4xl">local_shipping</span>
          <div>
            <div className="font-bold text-lg">View Orders</div>
            <div className="text-sm opacity-90">Manage shipments</div>
          </div>
        </Link>
        
        <Link
          href="/seller/messages"
          className="bg-sage-green text-white rounded-xl p-6 hover:bg-deep-sage transition-all flex items-center gap-4"
        >
          <span className="material-symbols-outlined text-4xl">mail</span>
          <div>
            <div className="font-bold text-lg">Messages</div>
            <div className="text-sm opacity-90">Chat with customers</div>
          </div>
        </Link>
      </div>
      
      {/* Open Orders to Fulfill */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
          <h2 className="text-xl font-serif font-bold text-deep-sage">Open Orders to Fulfill</h2>
          <Link href="/seller/orders" className="text-accent-peach hover:text-deep-sage text-sm font-medium">
            View All →
          </Link>
        </div>
        
        {openOrders.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-soft-peach">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase tracking-wider">
                    Order ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase tracking-wider">
                    Customer
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase tracking-wider">
                    Product
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase tracking-wider">
                    Amount
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase tracking-wider">
                    Date
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {openOrders.map((order) => (
                  <tr key={order.id} className="hover:bg-warm-cream transition-colors">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-deep-sage">
                      #{order.id}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-sage-green">
                      {order.customer}
                    </td>
                    <td className="px-6 py-4 text-sm text-sage-green">
                      {order.product}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-deep-sage">
                      ${order.amount.toFixed(2)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-sage-green">
                      {order.date}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                        order.status === 'pending' 
                          ? 'bg-yellow-100 text-yellow-800'
                          : 'bg-blue-100 text-blue-800'
                      }`}>
                        {order.status.charAt(0).toUpperCase() + order.status.slice(1)}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <Link
                        href={`/seller/orders/${order.id}`}
                        className="text-accent-peach hover:text-deep-sage font-medium"
                      >
                        Fulfill →
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="px-6 py-12 text-center">
            <span className="material-symbols-outlined text-6xl text-gray-300">shopping_bag</span>
            <p className="text-sage-green mt-4">No open orders at the moment</p>
          </div>
        )}
      </div>
    </div>
  )
}
