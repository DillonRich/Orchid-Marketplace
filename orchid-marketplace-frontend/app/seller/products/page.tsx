'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import Image from 'next/image'
import ConfirmDialog from '@/components/ConfirmDialog'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'

interface Product {
  id: string
  title: string
  sku: string
  basePrice: number
  stockQuantity: number
  isActive: boolean
  imageUrl: string
  categoryId: string
  averageRating: number
  reviewCount: number
}

export default function SellerProductsPage() {
  const { user } = useAuthStore()
  const [products, setProducts] = useState<Product[]>([])
  const [filter, setFilter] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [selectedProducts, setSelectedProducts] = useState<Set<string>>(new Set())
  const [showBulkActions, setShowBulkActions] = useState(false)
  const [deleteConfirmId, setDeleteConfirmId] = useState<string | null>(null)
  const [bulkDeleteConfirm, setBulkDeleteConfirm] = useState(false)
  
  useEffect(() => {
    fetchProducts()
  }, [user])
  
  const fetchProducts = async () => {
    setIsLoading(true)
    try {
      // Fetch products for the seller's store
      const data = await apiClient.getProductsByStore(user?.storeId || '')
      setProducts(data)
    } catch (error) {
      console.error('Error fetching products:', error)
    } finally {
      setIsLoading(false)
    }
  }
  
  const filteredProducts = products.filter(product => {
    const matchesFilter = 
      filter === 'all' ||
      (filter === 'active' && product.isActive && product.stockQuantity > 0) ||
      (filter === 'out_of_stock' && product.stockQuantity === 0) ||
      (filter === 'low_stock' && product.stockQuantity > 0 && product.stockQuantity <= 5)
    
    const matchesSearch = 
      product.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      product.sku.toLowerCase().includes(searchQuery.toLowerCase())
    
    return matchesFilter && matchesSearch
  })
  
  const filterOptions = [
    { value: 'all', label: 'All Products', count: products.length },
    { value: 'active', label: 'Active', count: products.filter(p => p.isActive && p.stockQuantity > 0).length },
    { value: 'low_stock', label: 'Low Stock', count: products.filter(p => p.stockQuantity > 0 && p.stockQuantity <= 5).length },
    { value: 'out_of_stock', label: 'Out of Stock', count: products.filter(p => p.stockQuantity === 0).length },
  ]
  
  const toggleProductSelection = (productId: string) => {
    const newSelection = new Set(selectedProducts)
    if (newSelection.has(productId)) {
      newSelection.delete(productId)
    } else {
      newSelection.add(productId)
    }
    setSelectedProducts(newSelection)
    setShowBulkActions(newSelection.size > 0)
  }
  
  const toggleSelectAll = () => {
    if (selectedProducts.size === filteredProducts.length) {
      setSelectedProducts(new Set())
      setShowBulkActions(false)
    } else {
      setSelectedProducts(new Set(filteredProducts.map(p => p.id)))
      setShowBulkActions(true)
    }
  }
  
  const handleBulkDelete = async () => {
    try {
      for (const productId of selectedProducts) {
        await apiClient.deleteProduct(productId)
      }
      await fetchProducts()
      setSelectedProducts(new Set())
      setShowBulkActions(false)
      setBulkDeleteConfirm(false)
    } catch (error) {
      console.error('Error deleting products:', error)
      alert('Failed to delete some products')
    }
  }
  
  const handleBulkStatusToggle = async () => {
    try {
      for (const productId of selectedProducts) {
        const product = products.find(p => p.id === productId)
        if (product) {
          await apiClient.updateProduct(productId, { isActive: !product.isActive })
        }
      }
      await fetchProducts()
      setSelectedProducts(new Set())
      setShowBulkActions(false)
    } catch (error) {
      console.error('Error updating products:', error)
      alert('Failed to update some products')
    }
  }
  
  const handleDelete = async (productId: string) => {
    try {
      await apiClient.deleteProduct(productId)
      await fetchProducts()
      setDeleteConfirmId(null)
    } catch (error) {
      console.error('Error deleting product:', error)
      alert('Failed to delete product')
    }
  }
  
  if (isLoading) {
    return (
      <div className="space-y-8">
        <div className="h-12 bg-gray-200 rounded w-1/3 animate-pulse"></div>
        <div className="bg-white rounded-xl p-6 h-96 animate-pulse"></div>
      </div>
    )
  }
  
  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
            Products
          </h1>
          <p className="text-sage-green">
            Manage your product listings
          </p>
        </div>
        
        <Link
          href="/seller/products/new"
          className="bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all flex items-center gap-2"
        >
          <span className="material-symbols-outlined">add</span>
          Add Product
        </Link>
      </div>
      
      {/* Bulk Actions Bar */}
      {showBulkActions && (
        <div className="bg-accent-peach/20 border border-accent-peach rounded-xl p-4 flex items-center justify-between">
          <p className="text-deep-sage font-medium">
            {selectedProducts.size} product(s) selected
          </p>
          <div className="flex gap-3">
            <button
              onClick={handleBulkStatusToggle}
              className="px-4 py-2 bg-deep-sage text-white rounded-lg hover:bg-sage-green transition-all flex items-center gap-2"
            >
              <span className="material-symbols-outlined text-sm">toggle_on</span>
              Toggle Status
            </button>
            <button
              onClick={() => setBulkDeleteConfirm(true)}
              className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-all flex items-center gap-2"
            >
              <span className="material-symbols-outlined text-sm">delete</span>
              Delete Selected
            </button>
            <button
              onClick={() => {
                setSelectedProducts(new Set())
                setShowBulkActions(false)
              }}
              className="px-4 py-2 text-sage-green hover:text-deep-sage transition-colors"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
      
      {/* Filters */}
      <div className="bg-white rounded-xl p-6">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Search by product name or SKU..."
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
      
      {/* Products List */}
      {filteredProducts.length > 0 ? (
        <div className="bg-white rounded-xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-soft-peach">
                <tr className="text-left text-sm text-deep-sage">
                  <th className="px-6 py-4">
                    <input
                      type="checkbox"
                      checked={selectedProducts.size === filteredProducts.length && filteredProducts.length > 0}
                      onChange={toggleSelectAll}
                      className="w-4 h-4 rounded accent-deep-sage cursor-pointer"
                    />
                  </th>
                  <th className="px-6 py-4 font-medium">Product</th>
                  <th className="px-6 py-4 font-medium">SKU</th>
                  <th className="px-6 py-4 font-medium">Price</th>
                  <th className="px-6 py-4 font-medium">Stock</th>
                  <th className="px-6 py-4 font-medium">Reviews</th>
                  <th className="px-6 py-4 font-medium">Status</th>
                  <th className="px-6 py-4 font-medium">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredProducts.map(product => (
                  <tr key={product.id} className="border-b border-gray-200 hover:bg-soft-peach transition-all">
                    <td className="px-6 py-4">
                      <input
                        type="checkbox"
                        checked={selectedProducts.has(product.id)}
                        onChange={() => toggleProductSelection(product.id)}
                        className="w-4 h-4 rounded accent-deep-sage cursor-pointer"
                      />
                    </td>
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
                    <td className="px-6 py-4 text-sage-green">
                      {product.averageRating > 0 ? (
                        <div className="flex items-center gap-1">
                          <span className="material-symbols-outlined text-sm text-accent-peach">star</span>
                          <span>{product.averageRating.toFixed(1)}</span>
                          <span className="text-xs">({product.reviewCount})</span>
                        </div>
                      ) : (
                        <span className="text-xs">No reviews</span>
                      )}
                    </td>
                    <td className="px-6 py-4">
                      <span className={`text-xs px-3 py-1 rounded-full ${
                        product.isActive && product.stockQuantity > 0
                          ? 'bg-green-100 text-green-700'
                          : product.stockQuantity === 0
                          ? 'bg-red-100 text-red-700'
                          : 'bg-gray-100 text-gray-700'
                      }`}>
                        {product.stockQuantity === 0 ? 'Out of Stock' : product.isActive ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Link
                          href={`/seller/products/${product.id}/edit`}
                          className="text-accent-peach hover:text-deep-sage"
                          title="Edit"
                        >
                          <span className="material-symbols-outlined">edit</span>
                        </Link>
                        <button
                          onClick={() => setDeleteConfirmId(product.id)}
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
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            inventory_2
          </span>
          <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
            No products found
          </h3>
          <p className="text-sage-green mb-6">
            {searchQuery ? 'Try a different search term' : 'Start adding products to your store'}
          </p>
          <Link
            href="/seller/products/new"
            className="inline-block bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
          >
            Add Your First Product
          </Link>
        </div>
      )}
      
      {/* Single Delete Confirmation */}
      <ConfirmDialog
        isOpen={!!deleteConfirmId}
        onClose={() => setDeleteConfirmId(null)}
        onConfirm={() => handleDelete(deleteConfirmId!)}
        title="Delete Product?"
        message="Are you sure you want to delete this product? This action cannot be undone and will remove all product data, images, and reviews."
        confirmText="Delete Product"
        variant="danger"
      />
      
      {/* Bulk Delete Confirmation */}
      <ConfirmDialog
        isOpen={bulkDeleteConfirm}
        onClose={() => setBulkDeleteConfirm(false)}
        onConfirm={handleBulkDelete}
        title="Delete Multiple Products?"
        message={`Are you sure you want to delete ${selectedProducts.size} product(s)? This action cannot be undone.`}
        confirmText={`Delete ${selectedProducts.size} Products`}
        variant="danger"
      />
    </div>
  )
}
