'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { useRouter, useParams } from 'next/navigation'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'
import ImageUpload from '@/components/ImageUpload'

interface Category {
  id: string
  name: string
  slug: string
}

interface ProductData {
  id: string
  title: string
  description: string
  categoryId: string
  basePrice: number
  compareAtPrice?: number
  sku: string
  stockQuantity: number
  weight?: string
  dimensions?: string
  careInstructions?: string
  shippingNotes?: string
  isActive: boolean
  storeId: string
}

export default function EditProductPage() {
  const router = useRouter()
  const params = useParams()
  const { user } = useAuthStore()
  const productId = params.id as string

  const [isLoading, setIsLoading] = useState(true)
  const [categories, setCategories] = useState<Category[]>([])
  const [error, setError] = useState('')
  
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    categoryId: '',
    basePrice: '',
    compareAtPrice: '',
    sku: '',
    stockQuantity: '',
    weight: '',
    careInstructions: '',
    shippingNotes: ''
  })

  const [dimensions, setDimensions] = useState({
    length: '',
    width: '',
    height: ''
  })

  const [images, setImages] = useState<File[]>([])
  const [existingImages, setExistingImages] = useState<Array<{ id: string; url: string; isPrimary: boolean }>>([])

  useEffect(() => {
    fetchCategories()
    fetchProduct()
  }, [productId])

  const fetchCategories = async () => {
    try {
      const data = await apiClient.getCategories()
      setCategories(data)
    } catch (err) {
      console.error('Failed to fetch categories:', err)
    }
  }

  const fetchProduct = async () => {
    try {
      setIsLoading(true)
      const product: ProductData = await apiClient.getProductById(productId)
      
      // Populate form with existing data
      setFormData({
        title: product.title || '',
        description: product.description || '',
        categoryId: product.categoryId || '',
        basePrice: product.basePrice?.toString() || '',
        compareAtPrice: product.compareAtPrice?.toString() || '',
        sku: product.sku || '',
        stockQuantity: product.stockQuantity?.toString() || '',
        weight: product.weight || '',
        careInstructions: product.careInstructions || '',
        shippingNotes: product.shippingNotes || ''
      })

      // Parse dimensions if available
      if (product.dimensions) {
        const dims = product.dimensions.split('x')
        if (dims.length === 3) {
          setDimensions({
            length: dims[0],
            width: dims[1],
            height: dims[2]
          })
        }
      }

      // Fetch existing images
      const productImages = await apiClient.getProductImages(productId)
      setExistingImages(productImages)
    } catch (err) {
      console.error('Failed to fetch product:', err)
      setError('Failed to load product')
    } finally {
      setIsLoading(false)
    }
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleDimensionChange = (field: 'length' | 'width' | 'height', value: string) => {
    setDimensions(prev => ({ ...prev, [field]: value }))
  }

  const removeExistingImage = (imageId: string) => {
    setExistingImages(prev => prev.filter(img => img.id !== imageId))
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
    
    return true
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')

    if (!validateForm()) return

    try {
      // Combine dimensions
      const dimensionsStr = `${dimensions.length || '0'}x${dimensions.width || '0'}x${dimensions.height || '0'}`

      // Update product
      await apiClient.updateProduct(productId, {
        ...formData,
        basePrice: parseFloat(formData.basePrice),
        compareAtPrice: formData.compareAtPrice ? parseFloat(formData.compareAtPrice) : undefined,
        stockQuantity: parseInt(formData.stockQuantity),
        dimensions: dimensionsStr,
        storeId: user?.storeId || ''
      })

      // Upload new images if any
      for (const image of images) {
        await apiClient.uploadProductImage(productId, image)
      }

      // Delete removed images (if API supports it)
      // This would require a deleteProductImage endpoint
      
      router.push('/seller/products')
    } catch (err) {
      console.error('Failed to update product:', err)
      setError('Failed to update product. Please try again.')
    }
  }

  if (isLoading) {
    return (
      <div className="max-w-5xl mx-auto p-6">
        <div className="animate-pulse space-y-6">
          <div className="h-8 bg-gray-200 rounded w-1/4"></div>
          <div className="h-64 bg-gray-200 rounded"></div>
          <div className="h-64 bg-gray-200 rounded"></div>
        </div>
      </div>
    )
  }

  return (
    <div className="max-w-5xl mx-auto p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-deep-sage">Edit Product</h1>
          <p className="text-sage-green mt-1">Update your product information</p>
        </div>
        <Link
          href="/seller/products"
          className="px-4 py-2 text-sage-green hover:text-deep-sage"
        >
          Cancel
        </Link>
      </div>

      {/* Error Message */}
      {error && (
        <div className="bg-red-50 border border-red-300 text-red-700 p-4 rounded-xl flex items-center gap-2">
          <span className="material-symbols-outlined">error</span>
          <span>{error}</span>
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Basic Information */}
        <div className="bg-white rounded-xl p-6 space-y-4">
          <h2 className="text-xl font-semibold text-deep-sage mb-4">Basic Information</h2>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">Product Title *</label>
            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
              placeholder="Handmade Ceramic Vase"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">Description *</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleInputChange}
              rows={4}
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent resize-none"
              placeholder="Describe your product in detail..."
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">Category *</label>
            <select
              name="categoryId"
              value={formData.categoryId}
              onChange={handleInputChange}
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
              required
            >
              <option value="">Select a category</option>
              {categories.map(cat => (
                <option key={cat.id} value={cat.id}>{cat.name}</option>
              ))}
            </select>
          </div>
        </div>

        {/* Pricing */}
        <div className="bg-white rounded-xl p-6 space-y-4">
          <h2 className="text-xl font-semibold text-deep-sage mb-4">Pricing</h2>
          
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">Base Price *</label>
              <div className="relative">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-sage-green">$</span>
                <input
                  type="number"
                  name="basePrice"
                  value={formData.basePrice}
                  onChange={handleInputChange}
                  step="0.01"
                  min="0"
                  className="w-full pl-8 pr-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  placeholder="29.99"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">Compare at Price</label>
              <div className="relative">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-sage-green">$</span>
                <input
                  type="number"
                  name="compareAtPrice"
                  value={formData.compareAtPrice}
                  onChange={handleInputChange}
                  step="0.01"
                  min="0"
                  className="w-full pl-8 pr-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  placeholder="39.99"
                />
              </div>
            </div>
          </div>
        </div>

        {/* Inventory */}
        <div className="bg-white rounded-xl p-6 space-y-4">
          <h2 className="text-xl font-semibold text-deep-sage mb-4">Inventory</h2>
          
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">SKU *</label>
              <input
                type="text"
                name="sku"
                value={formData.sku}
                onChange={handleInputChange}
                className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                placeholder="VASE-001"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">Stock Quantity *</label>
              <input
                type="number"
                name="stockQuantity"
                value={formData.stockQuantity}
                onChange={handleInputChange}
                min="0"
                className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                placeholder="10"
                required
              />
            </div>
          </div>
        </div>

        {/* Shipping */}
        <div className="bg-white rounded-xl p-6 space-y-4">
          <h2 className="text-xl font-semibold text-deep-sage mb-4">Shipping</h2>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">Weight (lbs)</label>
            <input
              type="text"
              name="weight"
              value={formData.weight}
              onChange={handleInputChange}
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
              placeholder="2.5"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">Dimensions (inches)</label>
            <div className="grid grid-cols-3 gap-4">
              <input
                type="text"
                value={dimensions.length}
                onChange={(e) => handleDimensionChange('length', e.target.value)}
                className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                placeholder="Length"
              />
              <input
                type="text"
                value={dimensions.width}
                onChange={(e) => handleDimensionChange('width', e.target.value)}
                className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                placeholder="Width"
              />
              <input
                type="text"
                value={dimensions.height}
                onChange={(e) => handleDimensionChange('height', e.target.value)}
                className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                placeholder="Height"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">Shipping Notes</label>
            <textarea
              name="shippingNotes"
              value={formData.shippingNotes}
              onChange={handleInputChange}
              rows={3}
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent resize-none"
              placeholder="Special shipping instructions..."
            />
          </div>
        </div>

        {/* Care Instructions */}
        <div className="bg-white rounded-xl p-6 space-y-4">
          <h2 className="text-xl font-semibold text-deep-sage mb-4">Care Instructions</h2>
          
          <div>
            <textarea
              name="careInstructions"
              value={formData.careInstructions}
              onChange={handleInputChange}
              rows={4}
              className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent resize-none"
              placeholder="How to care for this product..."
            />
          </div>
        </div>

        {/* Images */}
        <div className="bg-white rounded-xl p-6 space-y-4">
          <h2 className="text-xl font-semibold text-deep-sage mb-4">Product Images</h2>
          
          {/* Existing Images */}
          {existingImages.length > 0 && (
            <div>
              <p className="text-sm text-sage-green mb-3">Existing Images (click X to remove)</p>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-4">
                {existingImages.map((img) => (
                  <div key={img.id} className="relative group">
                    <img
                      src={img.url}
                      alt="Product"
                      className="w-full h-32 object-cover rounded-lg"
                    />
                    {img.isPrimary && (
                      <div className="absolute top-2 left-2 bg-accent-peach text-white text-xs px-2 py-1 rounded">
                        Primary
                      </div>
                    )}
                    <button
                      type="button"
                      onClick={() => removeExistingImage(img.id)}
                      className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1 opacity-0 group-hover:opacity-100 transition-opacity"
                    >
                      <span className="material-symbols-outlined text-sm">close</span>
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* New Images Upload */}
          <div>
            <p className="text-sm text-sage-green mb-3">Add New Images</p>
            <ImageUpload
              images={images}
              onImagesChange={setImages}
              maxImages={10}
              maxSizeMB={5}
            />
          </div>
        </div>

        {/* Submit Button */}
        <div className="flex justify-end gap-4">
          <Link
            href="/seller/products"
            className="px-6 py-3 rounded-lg border border-gray-300 text-sage-green hover:bg-gray-50 transition-all"
          >
            Cancel
          </Link>
          <button
            type="submit"
            className="px-6 py-3 rounded-lg bg-accent-peach text-white hover:bg-deep-sage transition-all"
          >
            Update Product
          </button>
        </div>
      </form>
    </div>
  )
}
