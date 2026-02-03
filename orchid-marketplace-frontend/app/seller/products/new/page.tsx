'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'
import ImageUpload from '@/components/ImageUpload'

interface Category {
  id: string
  name: string
  slug: string
}

export default function NewProductPage() {
  const router = useRouter()
  const { user } = useAuthStore()
  const [isSaving, setIsSaving] = useState(false)
  const [error, setError] = useState('')
  const [categories, setCategories] = useState<Category[]>([])
  const [images, setImages] = useState<File[]>([])
  
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    categoryId: '',
    basePrice: '',
    compareAtPrice: '',
    sku: '',
    stockQuantity: '',
    weight: '',
    dimensions: '', // Will combine length/width/height
    careInstructions: '',
    shippingNotes: '',
  })
  
  // Dimensional fields for UI
  const [dimensions, setDimensions] = useState({
    length: '',
    width: '',
    height: '',
  })
  
  useEffect(() => {
    fetchCategories()
  }, [])
  
  const fetchCategories = async () => {
    try {
      const data = await apiClient.getCategories()
      setCategories(data)
    } catch (error) {
      console.error('Error fetching categories:', error)
    }
  }
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }))
    setError('')
  }
  
  const handleDimensionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setDimensions(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }
  
  const validateForm = (): boolean => {
    if (!formData.title.trim()) {
      setError('Product title is required')
      return false
    }
    
    if (!formData.description.trim()) {
      setError('Product description is required')
      return false
    }
    
    if (!formData.categoryId) {
      setError('Please select a category')
      return false
    }
    
    if (!formData.basePrice || parseFloat(formData.basePrice) <= 0) {
      setError('Please enter a valid price')
      return false
    }
    
    if (!formData.sku.trim()) {
      setError('SKU is required')
      return false
    }
    
    if (!formData.stockQuantity || parseInt(formData.stockQuantity) < 0) {
      setError('Please enter a valid stock quantity')
      return false
    }
    
    if (images.length === 0) {
      setError('Please upload at least one product image')
      return false
    }
    
    return true
  }
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!validateForm()) {
      window.scrollTo({ top: 0, behavior: 'smooth' })
      return
    }
    
    setIsSaving(true)
    setError('')
    
    try {
      // Combine dimensions into string format
      const dimensionsStr = dimensions.length && dimensions.width && dimensions.height
        ? `${dimensions.length}x${dimensions.width}x${dimensions.height}`
        : ''
      
      // Create product
      const productData = {
        ...formData,
        basePrice: parseFloat(formData.basePrice),
        compareAtPrice: formData.compareAtPrice ? parseFloat(formData.compareAtPrice) : undefined,
        stockQuantity: parseInt(formData.stockQuantity),
        weight: formData.weight ? parseFloat(formData.weight) : undefined,
        dimensions: dimensionsStr || undefined,
        isActive: true,
        storeId: user?.storeId || '', // TODO: Get from user's store
      }
      
      const product = await apiClient.createProduct(productData)
      
      // Upload images
      if (images.length > 0) {
        for (const image of images) {
          await apiClient.uploadProductImage(product.id, image)
        }
      }
      
      // Success - redirect to products list
      router.push('/seller/products')
    } catch (err: any) {
      console.error('Error creating product:', err)
      setError(err.response?.data?.message || 'Failed to create product. Please try again.')
      window.scrollTo({ top: 0, behavior: 'smooth' })
    } finally {
      setIsSaving(false)
    }
  }
  
  return (
    <form onSubmit={handleSubmit} className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Add New Product
        </h1>
        <p className="text-sage-green">
          Create a new product listing for your store
        </p>
      </div>
      
      {/* Error Message */}
      {error && (
        <div className="bg-red-50 text-red-600 px-6 py-4 rounded-xl flex items-start gap-3">
          <span className="material-symbols-outlined text-2xl">error</span>
          <div>
            <p className="font-medium">Error</p>
            <p className="text-sm">{error}</p>
          </div>
        </div>
      )}
      
      {/* Basic Information */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Basic Information</h2>
        
        <div className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Product Title *
            </label>
            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="e.g., Beautiful Phalaenopsis Orchid"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Description *
            </label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
              rows={6}
              placeholder="Describe your product in detail..."
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Category *
              </label>
              <select
                name="categoryId"
                value={formData.categoryId}
                onChange={handleChange}
                required
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              >
                <option value="">Select Category</option>
                {categories.map(cat => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                SKU *
              </label>
              <input
                type="text"
                name="sku"
                value={formData.sku}
                onChange={handleChange}
                required
                placeholder="e.g., PHO-001"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
          </div>
        </div>
      </div>
      
      {/* Pricing */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Pricing</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Price *
            </label>
            <div className="relative">
              <span className="absolute left-4 top-1/2 -translate-y-1/2 text-sage-green">$</span>
              <input
                type="number"
                name="basePrice"
                value={formData.basePrice}
                onChange={handleChange}
                required
                min="0"
                step="0.01"
                placeholder="0.00"
                className="w-full pl-8 pr-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Compare at Price (Optional)
            </label>
            <div className="relative">
              <span className="absolute left-4 top-1/2 -translate-y-1/2 text-sage-green">$</span>
              <input
                type="number"
                name="compareAtPrice"
                value={formData.compareAtPrice}
                onChange={handleChange}
                min="0"
                step="0.01"
                placeholder="0.00"
                className="w-full pl-8 pr-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
            <p className="mt-1 text-xs text-sage-green">Show original price if on sale</p>
          </div>
        </div>
      </div>
      
      {/* Inventory */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Inventory</h2>
        
        <div>
          <label className="block text-sm font-medium text-deep-sage mb-2">
            Stock Quantity *
          </label>
          <input
            type="number"
            name="stockQuantity"
            value={formData.stockQuantity}
            onChange={handleChange}
            required
            min="0"
            placeholder="0"
            className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach max-w-xs"
          />
        </div>
      </div>
      
      {/* Shipping */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Shipping</h2>
        
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Weight (lbs)
              </label>
              <input
                type="number"
                name="weight"
                value={formData.weight}
                onChange={handleChange}
                min="0"
                step="0.01"
                placeholder="0.00"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Length (in)
              </label>
              <input
                type="number"
                name="length"
                value={dimensions.length}
                onChange={handleDimensionChange}
                min="0"
                step="0.01"
                placeholder="0.00"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Width (in)
              </label>
              <input
                type="number"
                name="width"
                value={dimensions.width}
                onChange={handleDimensionChange}
                min="0"
                step="0.01"
                placeholder="0.00"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Height (in)
              </label>
              <input
                type="number"
                name="height"
                value={dimensions.height}
                onChange={handleDimensionChange}
                min="0"
                step="0.01"
                placeholder="0.00"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Shipping Notes
            </label>
            <textarea
              name="shippingNotes"
              value={formData.shippingNotes}
              onChange={handleChange}
              rows={3}
              placeholder="Special handling instructions..."
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
        </div>
      </div>
      
      {/* Care Instructions */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Care Instructions</h2>
        
        <textarea
          name="careInstructions"
          value={formData.careInstructions}
          onChange={handleChange}
          rows={6}
          placeholder="Provide care instructions for this orchid..."
          className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
        />
      </div>
      
      {/* Images */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Product Images *</h2>
        
        <ImageUpload 
          images={images}
          onImagesChange={setImages}
          maxImages={10}
          maxSizeMB={5}
        />
      </div>
      
      {/* Actions */}
      <div className="flex gap-4">
        <button
          type="submit"
          disabled={isSaving}
          className="bg-deep-sage text-white py-3 px-12 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50"
        >
          {isSaving ? 'Saving...' : 'Create Product'}
        </button>
        <button
          type="button"
          onClick={() => router.back()}
          className="px-12 py-3 border border-gray-300 text-sage-green rounded-full font-medium hover:bg-soft-peach transition-all"
        >
          Cancel
        </button>
      </div>
    </form>
  )
}
