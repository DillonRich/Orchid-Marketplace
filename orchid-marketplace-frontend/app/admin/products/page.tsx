'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import Image from 'next/image'
import { apiClient } from '@/lib/api-client'

interface Product {
  id: string
  title: string
  sku: string
  basePrice: number
  stockQuantity: number
  isActive: boolean
  status: string
  imageUrl: string
  storeName: string
  storeId: string
  createdAt: string
}

export default function AdminProductsPage() {
  const [products, setProducts] = useState<Product[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [filter, setFilter] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')

  useEffect(() => {
    fetchProducts()
  }, [filter])

  const fetchProducts = async () => {
    try {
      setIsLoading(true)
      const statusFilter = filter === 'all' ? undefined : filter
      const data = await apiClient.getAllProductsAdmin(statusFilter)
      setProducts(data.products || data)
    } catch (err) {
      console.error('Failed to fetch products:', err)
    } finally {
      setIsLoading(false)
    }
  }

  const handleApprove = async (productId: string) => {
    try {
      await apiClient.approveProduct(productId)
      await fetchProducts()
      alert('Product approved successfully')
    } catch (err) {
      console.error('Failed to approve product:', err)
      alert('Failed to approve product')
    }
  }

  const handleReject = async (productId: string) => {
    const reason = prompt('Enter rejection reason:')
    if (!reason) return

    try {
      await apiClient.rejectProduct(productId, reason)
      await fetchProducts()
      alert('Product rejected')
    } catch (err) {
      console.error('Failed to reject product:', err)
      alert('Failed to reject product')
    }
  }

  const handleDelete = async (productId: string) => {
    if (!confirm('Are you sure you want to delete this product?')) return

    try {
      await apiClient.deleteProductAdmin(productId)
      await fetchProducts()
      alert('Product deleted successfully')
    } catch (err) {
      console.error('Failed to delete product:', err)
      alert('Failed to delete product')
    }
  }

  const filteredProducts = products.filter(product => {
    const matchesFilter = filter === 'all' || product.status === filter
    const matchesSearch =
      product.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      product.sku.toLowerCase().includes(searchQuery.toLowerCase()) ||
      product.storeName.toLowerCase().includes(searchQuery.toLowerCase())

    return matchesFilter && matchesSearch
  })

  const filterOptions = [
    { value: 'all', label: 'All Products', count: products.length },
    { value: 'pending', label: 'Pending', count: products.filter(p => p.status === 'pending').length },
    { value: 'approved', label: 'Approved', count: products.filter(p => p.status === 'approved').length },
    { value: 'rejected', label: 'Rejected', count: products.filter(p => p.status === 'rejected').length },
  ]

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'pending': return 'bg-yellow-100 text-yellow-700'
      case 'approved': return 'bg-green-100 text-green-700'
      case 'rejected': return 'bg-red-100 text-red-700'
      default: return 'bg-gray-100 text-gray-700'
    }
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
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Product Management
        </h1>
        <p className="text-sage-green">
          Review, approve, and manage all products on the platform
        </p>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl p-6">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Search by title, SKU, or store name..."
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

      {/* Products Table */}
      {filteredProducts.length > 0 ? (
        <div className="bg-white rounded-xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-soft-peach">
                <tr className="text-left text-sm text-deep-sage">
                  <th className="px-6 py-4 font-medium">Product</th>
                  <th className="px-6 py-4 font-medium">Store</th>
                  <th className="px-6 py-4 font-medium">SKU</th>
                  <th className="px-6 py-4 font-medium">Price</th>
                  <th className="px-6 py-4 font-medium">Stock</th>
                  <th className="px-6 py-4 font-medium">Status</th>
                  <th className="px-6 py-4 font-medium">Created</th>
                  <th className="px-6 py-4 font-medium">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredProducts.map(product => (
                  <tr key={product.id} className="border-b border-gray-200 hover:bg-soft-peach transition-all">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className="relative w-12 h-12 rounded-lg overflow-hidden bg-gray-100 flex-shrink-0">
                          <Image src={product.imageUrl} alt={product.title} fill className="object-cover" />
                        </div>
                        <div>
                          <Link
                            href={`/product/${product.id}`}
                            className="font-medium text-deep-sage hover:text-accent-peach"
                          >
                            {product.title}
                          </Link>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <Link
                        href={`/store/${product.storeId}`}
                        className="text-sage-green hover:text-accent-peach"
                      >
                        {product.storeName}
                      </Link>
                    </td>
                    <td className="px-6 py-4 text-sage-green">{product.sku}</td>
                    <td className="px-6 py-4 font-medium text-deep-sage">${product.basePrice}</td>
                    <td className="px-6 py-4">
                      <span className={`font-medium ${
                        product.stockQuantity === 0 ? 'text-red-600' :
                        product.stockQuantity <= 5 ? 'text-orange-600' :
                        'text-green-600'
                      }`}>
                        {product.stockQuantity}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <span className={`text-xs px-3 py-1 rounded-full ${getStatusColor(product.status)}`}>
                        {product.status.charAt(0).toUpperCase() + product.status.slice(1)}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sage-green text-sm">
                      {new Date(product.createdAt).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        {product.status === 'pending' && (
                          <>
                            <button
                              onClick={() => handleApprove(product.id)}
                              className="text-green-600 hover:text-green-700"
                              title="Approve"
                            >
                              <span className="material-symbols-outlined">check_circle</span>
                            </button>
                            <button
                              onClick={() => handleReject(product.id)}
                              className="text-red-600 hover:text-red-700"
                              title="Reject"
                            >
                              <span className="material-symbols-outlined">cancel</span>
                            </button>
                          </>
                        )}
                        <Link
                          href={`/product/${product.id}`}
                          className="text-accent-peach hover:text-deep-sage"
                          title="View"
                        >
                          <span className="material-symbols-outlined">visibility</span>
                        </Link>
                        <button
                          onClick={() => handleDelete(product.id)}
                          className="text-sage-green hover:text-red-500"
                          title="Delete"
                        >
                          <span className="material-symbols-outlined">delete</span>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-gray-300 mb-4">inventory_2</span>
          <p className="text-sage-green">No products found</p>
        </div>
      )}
    </div>
  )
}
