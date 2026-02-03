'use client'

import { useState, useEffect } from 'react'
import { useAuthStore } from '@/lib/auth-store'
import RevenueChart from '@/components/RevenueChart'
import OrdersChart from '@/components/OrdersChart'

interface AnalyticsData {
  revenue: {
    current: number
    previous: number
    change: number
  }
  orders: {
    current: number
    previous: number
    change: number
  }
  avgOrderValue: {
    current: number
    previous: number
    change: number
  }
  conversion: {
    current: number
    previous: number
    change: number
  }
  revenueData: Array<{ date: string; revenue: number; orders: number }>
  ordersData: Array<{ date: string; orders: number; completed: number; cancelled: number }>
  topProducts: Array<{ name: string; sales: number; revenue: number }>
}

export default function SellerAnalyticsPage() {
  const { user } = useAuthStore()
  const [timeRange, setTimeRange] = useState('7d')
  const [isLoading, setIsLoading] = useState(true)
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null)
  const [error, setError] = useState('')
  
  useEffect(() => {
    if (user?.role === 'SELLER') {
      fetchAnalytics()
    }
  }, [timeRange, user])
  
  const fetchAnalytics = async () => {
    setIsLoading(true)
    setError('')
    
    try {
      // Calculate date range
      const now = new Date()
      const daysMap: Record<string, number> = { '7d': 7, '30d': 30, '90d': 90, '1y': 365 }
      const days = daysMap[timeRange] || 7
      
      // For now, use mock data structure
      // TODO: Replace with actual API call when backend supports analytics endpoints
      // Calculate dates for future API integration:
      // const toDate = now.toISOString().split('T')[0]
      // const fromDate = new Date(now.getTime() - days * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
      // const data = await apiClient.getSellerAnalytics(user.storeId, fromDate, toDate)
      
      // Mock data for demonstration
      const mockData: AnalyticsData = {
        revenue: {
          current: 3450.25 + Math.random() * 1000,
          previous: 2890.50,
          change: 15 + Math.random() * 10,
        },
        orders: {
          current: 47 + Math.floor(Math.random() * 20),
          previous: 42,
          change: 10 + Math.random() * 10,
        },
        avgOrderValue: {
          current: 70 + Math.random() * 20,
          previous: 68.82,
          change: 5 + Math.random() * 5,
        },
        conversion: {
          current: 3 + Math.random() * 2,
          previous: 2.8,
          change: 10 + Math.random() * 10,
        },
        revenueData: Array.from({ length: days }, (_, i) => {
          const date = new Date(now.getTime() - (days - i - 1) * 24 * 60 * 60 * 1000)
          return {
            date: date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
            revenue: 100 + Math.random() * 300,
            orders: 2 + Math.floor(Math.random() * 8),
          }
        }),
        ordersData: Array.from({ length: Math.min(days, 14) }, (_, i) => {
          const date = new Date(now.getTime() - (Math.min(days, 14) - i - 1) * 24 * 60 * 60 * 1000)
          const total = 5 + Math.floor(Math.random() * 15)
          const completed = Math.floor(total * (0.8 + Math.random() * 0.15))
          return {
            date: date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
            orders: total,
            completed,
            cancelled: total - completed,
          }
        }),
        topProducts: [
          { name: 'Phalaenopsis White', sales: 34 + Math.floor(Math.random() * 20), revenue: 1563.66 + Math.random() * 500 },
          { name: 'Cattleya Pink', sales: 28 + Math.floor(Math.random() * 15), revenue: 1890.00 + Math.random() * 400 },
          { name: 'Dendrobium Purple', sales: 23 + Math.floor(Math.random() * 10), revenue: 1196.00 + Math.random() * 300 },
          { name: 'Oncidium Yellow', sales: 18 + Math.floor(Math.random() * 10), revenue: 945.00 + Math.random() * 250 },
          { name: 'Miltonia Red', sales: 15 + Math.floor(Math.random() * 8), revenue: 720.00 + Math.random() * 200 },
        ],
      }
      
      setAnalytics(mockData)
    } catch (err: any) {
      console.error('Error fetching analytics:', err)
      setError('Failed to load analytics data')
    } finally {
      setIsLoading(false)
    }
  }
  
  const exportData = () => {
    if (!analytics) return
    
    // Create CSV content
    const csvContent = [
      ['Metric', 'Current', 'Previous', 'Change'],
      ['Revenue', analytics.revenue.current.toFixed(2), analytics.revenue.previous.toFixed(2), analytics.revenue.change.toFixed(1) + '%'],
      ['Orders', analytics.orders.current, analytics.orders.previous, analytics.orders.change.toFixed(1) + '%'],
      ['Avg Order Value', analytics.avgOrderValue.current.toFixed(2), analytics.avgOrderValue.previous.toFixed(2), analytics.avgOrderValue.change.toFixed(1) + '%'],
      ['Conversion Rate', analytics.conversion.current.toFixed(1) + '%', analytics.conversion.previous.toFixed(1) + '%', analytics.conversion.change.toFixed(1) + '%'],
      [],
      ['Top Products'],
      ['Product', 'Sales', 'Revenue'],
      ...analytics.topProducts.map(p => [p.name, p.sales.toString(), '$' + p.revenue.toFixed(2)]),
    ].map(row => row.join(',')).join('\n')
    
    // Download CSV
    const blob = new Blob([csvContent], { type: 'text/csv' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `analytics-${timeRange}-${new Date().toISOString().split('T')[0]}.csv`
    link.click()
    window.URL.revokeObjectURL(url)
  }
  
  if (isLoading) {
    return (
      <div className="space-y-8">
        <div className="animate-pulse">
          <div className="h-12 bg-gray-200 rounded w-1/3 mb-4"></div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            {[1, 2, 3, 4].map((i) => (
              <div key={i} className="bg-white rounded-xl p-6 h-32"></div>
            ))}
          </div>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <div className="bg-white rounded-xl p-8 h-96"></div>
            <div className="bg-white rounded-xl p-8 h-96"></div>
          </div>
        </div>
      </div>
    )
  }
  
  if (error || !analytics) {
    return (
      <div className="bg-red-50 text-red-600 p-8 rounded-xl">
        <span className="material-symbols-outlined text-4xl mb-2">error</span>
        <p>{error || 'Failed to load analytics'}</p>
      </div>
    )
  }
  
  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
            Analytics
          </h1>
          <p className="text-sage-green">
            Track your store's performance
          </p>
        </div>
        
        <div className="flex gap-3">
          <button
            onClick={exportData}
            className="flex items-center gap-2 px-4 py-2 bg-white border border-sage-green/30 rounded-lg hover:bg-soft-peach transition-all"
          >
            <span className="material-symbols-outlined">download</span>
            <span>Export Data</span>
          </button>
          
          <select
            value={timeRange}
            onChange={(e) => setTimeRange(e.target.value)}
            className="px-4 py-2 rounded-lg border border-sage-green/30 bg-white focus:outline-none focus:ring-2 focus:ring-deep-sage"
          >
            <option value="7d">Last 7 days</option>
            <option value="30d">Last 30 days</option>
            <option value="90d">Last 90 days</option>
            <option value="1y">Last year</option>
          </select>
        </div>
      </div>
      
      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl p-6 shadow-sm hover:shadow-md transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-green-600">
              attach_money
            </span>
            <span className={`text-xs px-2 py-1 rounded-full font-medium ${
              analytics.revenue.change >= 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
            }`}>
              {analytics.revenue.change >= 0 ? '+' : ''}{analytics.revenue.change.toFixed(1)}%
            </span>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            ${analytics.revenue.current.toFixed(2)}
          </p>
          <p className="text-sm text-sage-green">Revenue</p>
          <p className="text-xs text-sage-green mt-2">
            vs ${analytics.revenue.previous.toFixed(2)} last period
          </p>
        </div>
        
        <div className="bg-white rounded-xl p-6 shadow-sm hover:shadow-md transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-blue-600">
              shopping_cart
            </span>
            <span className={`text-xs px-2 py-1 rounded-full font-medium ${
              analytics.orders.change >= 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
            }`}>
              {analytics.orders.change >= 0 ? '+' : ''}{analytics.orders.change.toFixed(1)}%
            </span>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            {analytics.orders.current}
          </p>
          <p className="text-sm text-sage-green">Orders</p>
          <p className="text-xs text-sage-green mt-2">
            vs {analytics.orders.previous} last period
          </p>
        </div>
        
        <div className="bg-white rounded-xl p-6 shadow-sm hover:shadow-md transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-purple-600">
              trending_up
            </span>
            <span className={`text-xs px-2 py-1 rounded-full font-medium ${
              analytics.avgOrderValue.change >= 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
            }`}>
              {analytics.avgOrderValue.change >= 0 ? '+' : ''}{analytics.avgOrderValue.change.toFixed(1)}%
            </span>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            ${analytics.avgOrderValue.current.toFixed(2)}
          </p>
          <p className="text-sm text-sage-green">Avg Order Value</p>
          <p className="text-xs text-sage-green mt-2">
            vs ${analytics.avgOrderValue.previous.toFixed(2)} last period
          </p>
        </div>
        
        <div className="bg-white rounded-xl p-6 shadow-sm hover:shadow-md transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-orange-600">
              conversion_path
            </span>
            <span className={`text-xs px-2 py-1 rounded-full font-medium ${
              analytics.conversion.change >= 0 ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
            }`}>
              {analytics.conversion.change >= 0 ? '+' : ''}{analytics.conversion.change.toFixed(1)}%
            </span>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            {analytics.conversion.current.toFixed(1)}%
          </p>
          <p className="text-sm text-sage-green">Conversion Rate</p>
          <p className="text-xs text-sage-green mt-2">
            vs {analytics.conversion.previous.toFixed(1)}% last period
          </p>
        </div>
      </div>
      
      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Revenue Chart */}
        <div className="bg-white rounded-xl p-8 shadow-sm">
          <h2 className="font-medium text-deep-sage text-lg mb-6">Revenue Over Time</h2>
          <RevenueChart data={analytics.revenueData} />
        </div>
        
        {/* Orders Chart */}
        <div className="bg-white rounded-xl p-8 shadow-sm">
          <h2 className="font-medium text-deep-sage text-lg mb-6">Orders Status</h2>
          <OrdersChart data={analytics.ordersData} />
        </div>
      </div>
      
      {/* Top Products */}
      <div className="bg-white rounded-xl p-8 shadow-sm">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Top Selling Products</h2>
        <div className="space-y-4">
          {analytics.topProducts.map((product, idx) => (
            <div key={idx} className="flex items-center justify-between p-4 bg-soft-peach rounded-lg hover:bg-accent-peach/20 transition-all">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-deep-sage text-white flex items-center justify-center font-bold">
                  {idx + 1}
                </div>
                <div>
                  <p className="font-medium text-deep-sage">{product.name}</p>
                  <p className="text-sm text-sage-green">{product.sales} sales</p>
                </div>
              </div>
              <p className="font-playfair text-lg font-bold text-deep-sage">
                ${product.revenue.toFixed(2)}
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
