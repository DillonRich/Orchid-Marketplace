'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { apiClient } from '@/lib/api-client'

interface AdminStats {
  totalUsers: number
  totalSellers: number
  totalProducts: number
  pendingProducts: number
  totalOrders: number
  totalRevenue: number
  activeUsers: number
  newUsersToday: number
}

interface RecentUser {
  id: string
  name: string
  email: string
  role: string
  createdAt: string
}

export default function AdminDashboard() {
  const [stats, setStats] = useState<AdminStats>({
    totalUsers: 0,
    totalSellers: 0,
    totalProducts: 0,
    pendingProducts: 0,
    totalOrders: 0,
    totalRevenue: 0,
    activeUsers: 0,
    newUsersToday: 0
  })
  const [recentUsers, setRecentUsers] = useState<RecentUser[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      setIsLoading(true)
      const statsData = await apiClient.getAdminStats()
      setStats(statsData)

      // Fetch recent users
      const usersData = await apiClient.getAllUsers(undefined, 1, 5)
      setRecentUsers(usersData.users || usersData)
    } catch (err) {
      console.error('Failed to fetch dashboard data:', err)
    } finally {
      setIsLoading(false)
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-8">
        <div className="animate-pulse">
          <div className="h-12 bg-gray-200 rounded w-1/4 mb-2"></div>
          <div className="h-6 bg-gray-200 rounded w-1/3"></div>
        </div>
        <div className="grid grid-cols-3 gap-6">
          <div className="h-32 bg-gray-200 rounded"></div>
          <div className="h-32 bg-gray-200 rounded"></div>
          <div className="h-32 bg-gray-200 rounded"></div>
        </div>
      </div>
    )
  }
  
  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Admin Dashboard
        </h1>
        <p className="text-sage-green">
          Manage and monitor the entire platform
        </p>
      </div>
      
      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4 mb-4">
            <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-blue-600">
                people
              </span>
            </div>
            <div>
              <p className="font-playfair text-3xl font-bold text-deep-sage">
                {stats.totalUsers}
              </p>
              <p className="text-sm text-sage-green">Total Users</p>
            </div>
          </div>
          <Link
            href="/admin/users"
            className="text-sm text-accent-peach hover:text-deep-sage"
          >
            Manage Users →
          </Link>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4 mb-4">
            <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-green-600">
                storefront
              </span>
            </div>
            <div>
              <p className="font-playfair text-3xl font-bold text-deep-sage">
                {stats.totalSellers}
              </p>
              <p className="text-sm text-sage-green">Active Sellers</p>
            </div>
          </div>
          <Link
            href="/admin/users?role=seller"
            className="text-sm text-accent-peach hover:text-deep-sage"
          >
            View Sellers →
          </Link>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4 mb-4">
            <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-purple-600">
                inventory_2
              </span>
            </div>
            <div>
              <p className="font-playfair text-3xl font-bold text-deep-sage">
                {stats.totalProducts}
              </p>
              <p className="text-sm text-sage-green">Total Products</p>
            </div>
          </div>
          <Link
            href="/admin/products"
            className="text-sm text-accent-peach hover:text-deep-sage"
          >
            Moderate Products →
          </Link>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4 mb-4">
            <div className="w-12 h-12 bg-orange-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-orange-600">
                pending_actions
              </span>
            </div>
            <div>
              <p className="font-playfair text-3xl font-bold text-deep-sage">
                {stats.pendingProducts}
              </p>
              <p className="text-sm text-sage-green">Pending Review</p>
            </div>
          </div>
          <Link
            href="/admin/products?status=pending"
            className="text-sm text-accent-peach hover:text-deep-sage"
          >
            Review Now →
          </Link>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center gap-4 mb-4">
            <div className="w-12 h-12 bg-yellow-100 rounded-full flex items-center justify-center">
              <span className="material-symbols-outlined text-yellow-600">
                shopping_cart
              </span>
            </div>
            <div>
              <p className="font-playfair text-3xl font-bold text-deep-sage">
                {stats.totalOrders}
              </p>
              <p className="text-sm text-sage-green">Total Orders</p>
            </div>
          </div>
          <Link
            href="/admin/orders"
            className="text-sm text-accent-peach hover:text-deep-sage"
          >
            View Orders →
          </Link>
        </div>
        
        <div className="bg-gradient-to-br from-deep-sage to-sage-green text-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="mb-4">
            <span className="material-symbols-outlined text-3xl">
              attach_money
            </span>
          </div>
          <p className="font-playfair text-3xl font-bold mb-1">
            ${stats.totalRevenue.toLocaleString()}
          </p>
          <p className="text-sm opacity-90">Platform Revenue</p>
        </div>
      </div>
      
      {/* Recent Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Recent Users */}
        <div className="bg-white rounded-xl p-8">
          <div className="flex items-center justify-between mb-6">
            <h2 className="font-medium text-deep-sage text-lg">Recent Users</h2>
            <Link
              href="/admin/users"
              className="text-accent-peach hover:text-deep-sage text-sm font-medium"
            >
              View All
            </Link>
          </div>
          
          <div className="space-y-3">
            {recentUsers.map(user => (
              <div key={user.id} className="flex items-center justify-between p-4 border border-gray-200 rounded-lg hover:bg-soft-peach transition-all">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-soft-peach flex items-center justify-center">
                    <span className="material-symbols-outlined text-deep-sage">
                      person
                    </span>
                  </div>
                  <div>
                    <p className="font-medium text-deep-sage">{user.name}</p>
                    <p className="text-xs text-sage-green">{user.email}</p>
                  </div>
                </div>
                <div className="text-right">
                  <span className={`text-xs px-2 py-1 rounded-full ${
                    user.role === 'SELLER' ? 'bg-green-100 text-green-700' : 'bg-blue-100 text-blue-700'
                  }`}>
                    {user.role}
                  </span>
                  <p className="text-xs text-sage-green mt-1">
                    {new Date(user.createdAt).toLocaleDateString()}
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
      
      {/* Quick Actions */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Quick Actions</h2>
        
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <Link
            href="/admin/categories"
            className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
          >
            <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
              category
            </span>
            <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
              Manage Categories
            </span>
          </Link>
          
          <Link
            href="/admin/users"
            className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
          >
            <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
              manage_accounts
            </span>
            <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
              User Management
            </span>
          </Link>
          
          <Link
            href="/admin/products"
            className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
          >
            <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
              inventory
            </span>
            <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
              Product Moderation
            </span>
          </Link>
          
          <Link
            href="/admin/settings"
            className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
          >
            <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
              settings
            </span>
            <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
              Platform Settings
            </span>
          </Link>
        </div>
      </div>
    </div>
  )
}
