'use client'

import Link from 'next/link'
import { useAuthStore } from '@/lib/auth-store'

export default function AccountPage() {
  const { user } = useAuthStore()
  
  // Mock data
  const stats = {
    totalOrders: 12,
    activeOrders: 2,
    wishlistItems: 8,
    unreadMessages: 3,
  }
  
  const recentOrders = [
    {
      id: '1',
      orderNumber: 'ORD-12345',
      date: '2026-01-20',
      status: 'Delivered',
      total: 99.99,
    },
    {
      id: '2',
      orderNumber: 'ORD-12344',
      date: '2026-01-18',
      status: 'In Transit',
      total: 145.50,
    },
    {
      id: '3',
      orderNumber: 'ORD-12343',
      date: '2026-01-15',
      status: 'Processing',
      total: 75.00,
    },
  ]
  
  const quickActions = [
    { icon: 'shopping_bag', label: 'Browse Products', href: '/products' },
    { icon: 'favorite', label: 'My Wishlist', href: '/account/wishlist' },
    { icon: 'local_shipping', label: 'Track Orders', href: '/account/orders' },
    { icon: 'mail', label: 'Messages', href: '/account/messages' },
  ]
  
  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Welcome back, {user?.fullName?.split(' ')[0] || 'Friend'}!
        </h1>
        <p className="text-sage-green">
          Manage your account, orders, and preferences
        </p>
      </div>
      
      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-blue-600">
                shopping_bag
              </span>
            </div>
            <div>
              <p className="text-2xl font-playfair font-bold text-deep-sage">
                {stats.totalOrders}
              </p>
              <p className="text-sm text-sage-green">Total Orders</p>
            </div>
          </div>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-green-600">
                local_shipping
              </span>
            </div>
            <div>
              <p className="text-2xl font-playfair font-bold text-deep-sage">
                {stats.activeOrders}
              </p>
              <p className="text-sm text-sage-green">Active Orders</p>
            </div>
          </div>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-pink-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-pink-600">
                favorite
              </span>
            </div>
            <div>
              <p className="text-2xl font-playfair font-bold text-deep-sage">
                {stats.wishlistItems}
              </p>
              <p className="text-sm text-sage-green">Wishlist Items</p>
            </div>
          </div>
        </div>
        
        <Link href="/account/messages" className="bg-white rounded-xl p-6 hover:shadow-lg transition-all block cursor-pointer">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-purple-600">
                mail
              </span>
            </div>
            <div>
              <p className="text-2xl font-playfair font-bold text-deep-sage">
                {stats.unreadMessages}
              </p>
              <p className="text-sm text-sage-green">Unread Messages</p>
            </div>
          </div>
        </Link>
      </div>
      
      {/* Recent Orders */}
      <div className="bg-white rounded-xl p-8">
        <div className="flex items-center justify-between mb-6">
          <h2 className="font-medium text-deep-sage text-lg">Recent Orders</h2>
          <Link
            href="/account/orders"
            className="text-accent-peach hover:text-deep-sage text-sm font-medium"
          >
            View All
          </Link>
        </div>
        
        <div className="space-y-4">
          {recentOrders.map(order => (
            <div
              key={order.id}
              className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 p-4 border border-gray-200 rounded-lg hover:border-accent-peach transition-all"
            >
              <div>
                <p className="font-medium text-deep-sage mb-1">
                  {order.orderNumber}
                </p>
                <p className="text-sm text-sage-green">
                  {new Date(order.date).toLocaleDateString()}
                </p>
              </div>
              
              <div className="flex items-center gap-4">
                <span className={`text-xs px-3 py-1 rounded-full ${
                  order.status === 'Delivered'
                    ? 'bg-green-100 text-green-700'
                    : order.status === 'In Transit'
                    ? 'bg-blue-100 text-blue-700'
                    : 'bg-yellow-100 text-yellow-700'
                }`}>
                  {order.status}
                </span>
                
                <p className="font-playfair text-lg font-bold text-deep-sage">
                  ${order.total.toFixed(2)}
                </p>
                
                <Link
                  href={`/account/orders/${order.id}`}
                  className="text-accent-peach hover:text-deep-sage"
                >
                  <span className="material-symbols-outlined">arrow_forward</span>
                </Link>
              </div>
            </div>
          ))}
        </div>
      </div>
      
      {/* Quick Actions */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Quick Actions</h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {quickActions.map(action => (
            <Link
              key={action.label}
              href={action.href}
              className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
            >
              <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
                {action.icon}
              </span>
              <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
                {action.label}
              </span>
            </Link>
          ))}
        </div>
      </div>
      
      {/* Account Status Banner */}
      <div className="bg-gradient-to-r from-deep-sage to-sage-green text-white rounded-xl p-8">
        <div className="flex flex-col md:flex-row items-center justify-between gap-4">
          <div>
            <h3 className="font-playfair text-2xl font-bold mb-2">
              Enjoying Orchidillo?
            </h3>
            <p className="opacity-90">
              Share your experience and help other plant lovers discover amazing orchids
            </p>
          </div>
          <button className="bg-white text-deep-sage px-8 py-3 rounded-full font-medium hover:bg-soft-peach transition-all whitespace-nowrap">
            Write a Review
          </button>
        </div>
      </div>
    </div>
  )
}
