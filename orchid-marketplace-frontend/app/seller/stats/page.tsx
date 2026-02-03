"use client"

import { useState } from 'react'

export default function SellerStatsPage() {
  const [timeRange, setTimeRange] = useState('week')
  
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-serif font-bold text-deep-sage mb-2">Stats</h1>
          <p className="text-sage-green">Detailed analytics for your shop</p>
        </div>
        
        {/* Time Range Selector */}
        <div className="flex gap-2">
          <button
            onClick={() => setTimeRange('day')}
            className={`px-4 py-2 rounded-full text-sm font-medium transition-all ${
              timeRange === 'day' ? 'bg-deep-sage text-white' : 'bg-soft-peach text-deep-sage hover:bg-primary-peach'
            }`}
          >
            Today
          </button>
          <button
            onClick={() => setTimeRange('week')}
            className={`px-4 py-2 rounded-full text-sm font-medium transition-all ${
              timeRange === 'week' ? 'bg-deep-sage text-white' : 'bg-soft-peach text-deep-sage hover:bg-primary-peach'
            }`}
          >
            This Week
          </button>
          <button
            onClick={() => setTimeRange('month')}
            className={`px-4 py-2 rounded-full text-sm font-medium transition-all ${
              timeRange === 'month' ? 'bg-deep-sage text-white' : 'bg-soft-peach text-deep-sage hover:bg-primary-peach'
            }`}
          >
            This Month
          </button>
          <button
            onClick={() => setTimeRange('year')}
            className={`px-4 py-2 rounded-full text-sm font-medium transition-all ${
              timeRange === 'year' ? 'bg-deep-sage text-white' : 'bg-soft-peach text-deep-sage hover:bg-primary-peach'
            }`}
          >
            This Year
          </button>
        </div>
      </div>
      
      {/* Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl p-6">
          <div className="flex items-center gap-3 mb-4">
            <span className="material-symbols-outlined text-3xl text-purple-500">visibility</span>
            <h3 className="text-lg font-medium text-deep-sage">Total Views</h3>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-2">1,247</div>
          <div className="text-sm text-green-600">↑ 12% vs last {timeRange}</div>
        </div>
        
        <div className="bg-white rounded-xl p-6">
          <div className="flex items-center gap-3 mb-4">
            <span className="material-symbols-outlined text-3xl text-blue-500">touch_app</span>
            <h3 className="text-lg font-medium text-deep-sage">Clicks</h3>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-2">523</div>
          <div className="text-sm text-green-600">↑ 8% vs last {timeRange}</div>
        </div>
        
        <div className="bg-white rounded-xl p-6">
          <div className="flex items-center gap-3 mb-4">
            <span className="material-symbols-outlined text-3xl text-orange-500">shopping_bag</span>
            <h3 className="text-lg font-medium text-deep-sage">Orders</h3>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-2">48</div>
          <div className="text-sm text-green-600">↑ 15% vs last {timeRange}</div>
        </div>
        
        <div className="bg-white rounded-xl p-6">
          <div className="flex items-center gap-3 mb-4">
            <span className="material-symbols-outlined text-3xl text-green-500">trending_up</span>
            <h3 className="text-lg font-medium text-deep-sage">Conversion</h3>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-2">9.2%</div>
          <div className="text-sm text-green-600">↑ 3% vs last {timeRange}</div>
        </div>
      </div>
      
      {/* Charts Placeholder */}
      <div className="bg-white rounded-xl p-6">
        <h3 className="text-xl font-serif font-bold text-deep-sage mb-4">Sales Over Time</h3>
        <div className="h-64 flex items-center justify-center border-2 border-dashed border-gray-300 rounded-lg">
          <div className="text-center">
            <span className="material-symbols-outlined text-6xl text-gray-300">show_chart</span>
            <p className="text-sage-green mt-4">Chart will be displayed here</p>
          </div>
        </div>
      </div>
      
      {/* Top Products */}
      <div className="bg-white rounded-xl p-6">
        <h3 className="text-xl font-serif font-bold text-deep-sage mb-4">Top Performing Products</h3>
        <div className="space-y-4">
          {[
            { name: 'Purple Phalaenopsis', views: 234, sales: 18, revenue: 810 },
            { name: 'White Orchid Set', views: 189, sales: 12, revenue: 1079 },
            { name: 'Pink Cattleya', views: 145, sales: 22, revenue: 1430 },
          ].map((product, idx) => (
            <div key={idx} className="flex items-center justify-between p-4 bg-warm-cream rounded-lg">
              <div className="flex items-center gap-4">
                <span className="text-2xl font-bold text-accent-peach">#{idx + 1}</span>
                <div>
                  <div className="font-medium text-deep-sage">{product.name}</div>
                  <div className="text-sm text-sage-green">{product.views} views • {product.sales} sales</div>
                </div>
              </div>
              <div className="text-right">
                <div className="text-lg font-bold text-deep-sage">${product.revenue}</div>
                <div className="text-sm text-sage-green">revenue</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
