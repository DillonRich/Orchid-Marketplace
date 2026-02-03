'use client'

import { useEffect } from 'react'
import { useRouter } from 'next/navigation'

export default function SellerPage() {
  const router = useRouter()
  
  useEffect(() => {
    // Redirect to the new dashboard
    router.replace('/seller/dashboard')
  }, [router])
  
  return (
    <div className="min-h-screen bg-warm-cream flex items-center justify-center">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-deep-sage mx-auto mb-4"></div>
        <p className="text-sage-green">Redirecting to dashboard...</p>
      </div>
    </div>
  )
}
  
  const recentOrders = [
    {
      id: '1',
      orderNumber: 'ORD-98765',
      customer: 'John Doe',
      product: 'Beautiful Phalaenopsis Orchid',
      amount: 45.99,
      status: 'Pending',
      date: '2026-01-22',
    },
    {
      id: '2',
      orderNumber: 'ORD-98764',
      customer: 'Jane Smith',
      product: 'Cattleya Orchid',
      amount: 67.50,
      status: 'Processing',
      date: '2026-01-21',
    },
    {
      id: '3',
      orderNumber: 'ORD-98763',
      customer: 'Mike Johnson',
      product: 'Dendrobium Orchid',
      amount: 52.00,
      status: 'Shipped',
      date: '2026-01-20',
    },
  ]
  
  const lowStockProducts = [
    { id: '1', name: 'Phalaenopsis White', stock: 2, sku: 'PHO-001' },
    { id: '2', name: 'Cattleya Pink', stock: 1, sku: 'CAT-005' },
    { id: '3', name: 'Dendrobium Purple', stock: 3, sku: 'DEN-012' },
  ]
  
  return (
    <div className="space-y-8">
      {/* Welcome Section */}
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Seller Dashboard
        </h1>
        <p className="text-sage-green">
          Welcome back! Here's your store overview
        </p>
      </div>
      
      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-green-600">
              attach_money
            </span>
            <span className="text-xs text-sage-green bg-green-100 px-2 py-1 rounded-full">
              +12.5% this month
            </span>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            ${stats.totalSales.toFixed(2)}
          </p>
          <p className="text-sm text-sage-green">Total Sales</p>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-blue-600">
              inventory_2
            </span>
            <Link
              href="/seller/products"
              className="text-xs text-accent-peach hover:text-deep-sage"
            >
              View All
            </Link>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            {stats.activeListings}
          </p>
          <p className="text-sm text-sage-green">Active Listings</p>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-orange-600">
              shopping_cart
            </span>
            <Link
              href="/seller/orders"
              className="text-xs text-accent-peach hover:text-deep-sage"
            >
              View Orders
            </Link>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            {stats.pendingOrders}
          </p>
          <p className="text-sm text-sage-green">Pending Orders</p>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-purple-600">
              people
            </span>
            <Link
              href="/seller/customers"
              className="text-xs text-accent-peach hover:text-deep-sage"
            >
              View All
            </Link>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            {stats.totalCustomers}
          </p>
          <p className="text-sm text-sage-green">Total Customers</p>
        </div>
        
        <div className="bg-white rounded-xl p-6 hover:shadow-lg transition-all">
          <div className="flex items-center justify-between mb-4">
            <span className="material-symbols-outlined text-3xl text-yellow-600">
              star
            </span>
            <span className="text-xs text-sage-green">
              {stats.totalReviews} reviews
            </span>
          </div>
          <p className="font-playfair text-3xl font-bold text-deep-sage mb-1">
            {stats.avgRating}
          </p>
          <p className="text-sm text-sage-green">Average Rating</p>
        </div>
        
        <div className="bg-gradient-to-br from-deep-sage to-sage-green text-white rounded-xl p-6 hover:shadow-lg transition-all">
          <span className="material-symbols-outlined text-3xl mb-4 block">
            add_business
          </span>
          <p className="font-medium mb-2">Add New Product</p>
          <Link
            href="/seller/products/new"
            className="text-sm bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full inline-block hover:bg-white/30 transition-all"
          >
            Create Listing
          </Link>
        </div>
      </div>
      
      {/* Recent Orders */}
      <div className="bg-white rounded-xl p-8">
        <div className="flex items-center justify-between mb-6">
          <h2 className="font-medium text-deep-sage text-lg">Recent Orders</h2>
          <Link
            href="/seller/orders"
            className="text-accent-peach hover:text-deep-sage text-sm font-medium"
          >
            View All Orders
          </Link>
        </div>
        
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200 text-left text-sm text-sage-green">
                <th className="pb-3 font-medium">Order</th>
                <th className="pb-3 font-medium">Customer</th>
                <th className="pb-3 font-medium">Product</th>
                <th className="pb-3 font-medium">Amount</th>
                <th className="pb-3 font-medium">Status</th>
                <th className="pb-3 font-medium">Date</th>
                <th className="pb-3 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              {recentOrders.map(order => (
                <tr key={order.id} className="border-b border-gray-200 hover:bg-soft-peach transition-all">
                  <td className="py-4">
                    <Link
                      href={`/seller/orders/${order.id}`}
                      className="font-medium text-deep-sage hover:text-accent-peach"
                    >
                      {order.orderNumber}
                    </Link>
                  </td>
                  <td className="py-4 text-sage-green">{order.customer}</td>
                  <td className="py-4 text-sage-green text-sm">{order.product}</td>
                  <td className="py-4 font-medium text-deep-sage">${order.amount}</td>
                  <td className="py-4">
                    <span className={`text-xs px-3 py-1 rounded-full ${
                      order.status === 'Shipped'
                        ? 'bg-green-100 text-green-700'
                        : order.status === 'Processing'
                        ? 'bg-blue-100 text-blue-700'
                        : 'bg-yellow-100 text-yellow-700'
                    }`}>
                      {order.status}
                    </span>
                  </td>
                  <td className="py-4 text-sm text-sage-green">
                    {new Date(order.date).toLocaleDateString()}
                  </td>
                  <td className="py-4">
                    <Link
                      href={`/seller/orders/${order.id}`}
                      className="text-accent-peach hover:text-deep-sage"
                    >
                      <span className="material-symbols-outlined">arrow_forward</span>
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Low Stock Alert */}
        <div className="bg-white rounded-xl p-8">
          <div className="flex items-center gap-2 mb-6">
            <span className="material-symbols-outlined text-red-500">
              warning
            </span>
            <h2 className="font-medium text-deep-sage text-lg">Low Stock Alert</h2>
          </div>
          
          <div className="space-y-3">
            {lowStockProducts.map(product => (
              <div
                key={product.id}
                className="flex items-center justify-between p-4 border border-red-200 rounded-lg bg-red-50"
              >
                <div>
                  <p className="font-medium text-deep-sage">{product.name}</p>
                  <p className="text-sm text-sage-green">SKU: {product.sku}</p>
                </div>
                <div className="text-right">
                  <p className="text-red-600 font-bold">{product.stock} left</p>
                  <Link
                    href={`/seller/products/${product.id}/edit`}
                    className="text-xs text-accent-peach hover:text-deep-sage"
                  >
                    Restock
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>
        
        {/* Quick Actions */}
        <div className="bg-white rounded-xl p-8">
          <h2 className="font-medium text-deep-sage text-lg mb-6">Quick Actions</h2>
          
          <div className="grid grid-cols-2 gap-4">
            <Link
              href="/seller/products/new"
              className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
            >
              <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
                add_box
              </span>
              <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
                Add Product
              </span>
            </Link>
            
            <Link
              href="/seller/orders?filter=pending"
              className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
            >
              <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
                package
              </span>
              <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
                Process Orders
              </span>
            </Link>
            
            <Link
              href="/seller/messages"
              className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
            >
              <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
                mail
              </span>
              <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
                Messages
              </span>
            </Link>
            
            <Link
              href="/seller/analytics"
              className="flex flex-col items-center gap-3 p-6 border-2 border-gray-200 rounded-xl hover:border-accent-peach hover:bg-soft-peach transition-all group"
            >
              <span className="material-symbols-outlined text-4xl text-sage-green group-hover:text-deep-sage">
                analytics
              </span>
              <span className="text-sm font-medium text-sage-green group-hover:text-deep-sage text-center">
                View Analytics
              </span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  )
}
