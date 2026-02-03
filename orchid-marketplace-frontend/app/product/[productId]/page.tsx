'use client'

import { useState, useEffect } from 'react'
import { useParams, useRouter } from 'next/navigation'
import Link from 'next/link'
import Image from 'next/image'
import Header from '@/components/Header'
import Footer from '@/components/Footer'
import Heart from '@/components/Heart'
import ImageLightbox from '@/components/ImageLightbox'
import ReviewForm from '@/components/ReviewForm'
import ReviewList from '@/components/ReviewList'
import RatingSummary from '@/components/RatingSummary'
import RelatedProducts from '@/components/RelatedProducts'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'
import { useCartStore } from '@/lib/cart-store'
import type { Product } from '@/lib/types'

export default function ProductDetailPage() {
  const params = useParams()
  const router = useRouter()
  const productId = params.productId as string
  
  const { isAuthenticated } = useAuthStore()
  const addToCart = useCartStore((state: any) => state.addItem)
  
  const [product, setProduct] = useState<Product | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [quantity, setQuantity] = useState(1)
  const [selectedImage, setSelectedImage] = useState(0)
  const [lightboxOpen, setLightboxOpen] = useState(false)
  const [activeTab, setActiveTab] = useState('description')
  const [showSuccessToast, setShowSuccessToast] = useState(false)
  const [showReviewForm, setShowReviewForm] = useState(false)
  const [reviewRefreshTrigger, setReviewRefreshTrigger] = useState(0)
  
  // Mock images array (in production, come from product data)
  const images = [
    product?.imageUrl || '/placeholder.jpg',
    '/placeholder.jpg',
    '/placeholder.jpg',
    '/placeholder.jpg',
  ]
  
  useEffect(() => {
    fetchProduct()
  }, [productId])
  
  const fetchProduct = async () => {
    setIsLoading(true)
    try {
      const data = await apiClient.getProductById(productId)
      setProduct(data)
    } catch (error) {
      console.error('Error fetching product:', error)
    } finally {
      setIsLoading(false)
    }
  }
  
  const handleAddToCart = async () => {
    if (!product) return
    
    try {
      await apiClient.addToCart(product.id, quantity)
      
      addToCart({
        id: product.id,
        title: product.title,
        price: product.basePrice || 0,
        quantity,
        imageUrl: product.imageUrl,
      })
      
      setShowSuccessToast(true)
      setTimeout(() => setShowSuccessToast(false), 3000)
    } catch (error) {
      console.error('Error adding to cart:', error)
    }
  }
  
  const handleContactSeller = () => {
    if (!isAuthenticated) {
      router.push(`/login?redirect=/product/${productId}`)
      return
    }
    // Open contact modal (to be implemented)
    alert('Contact seller feature coming soon!')
  }
  
  if (isLoading) {
    return (
      <div className="min-h-screen bg-warm-cream flex flex-col">
        <Header />
        <main className="flex-1 container mx-auto px-4 lg:px-24 py-16">
          <div className="animate-pulse">
            <div className="h-8 bg-gray-200 rounded w-1/4 mb-8"></div>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 mb-16">
              <div className="h-96 bg-gray-200 rounded-xl"></div>
              <div className="space-y-4">
                <div className="h-12 bg-gray-200 rounded"></div>
                <div className="h-6 bg-gray-200 rounded w-3/4"></div>
                <div className="h-8 bg-gray-200 rounded w-1/2"></div>
              </div>
            </div>
          </div>
        </main>
        <Footer />
      </div>
    )
  }
  
  if (!product) {
    return (
      <div className="min-h-screen bg-warm-cream flex flex-col">
        <Header />
        <main className="flex-1 container mx-auto px-4 lg:px-24 py-16 text-center">
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            search_off
          </span>
          <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-4">
            Product not found
          </h1>
          <p className="text-sage-green mb-8">
            The product you're looking for doesn't exist or has been removed.
          </p>
          <Link
            href="/products"
            className="inline-block bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
          >
            Browse all products
          </Link>
        </main>
        <Footer />
      </div>
    )
  }
  
  const inStock = (product.stockQuantity || 0) > 0
  const lowStock = (product.stockQuantity || 0) <= 5 && inStock
  
  return (
    <div className="min-h-screen bg-warm-cream flex flex-col">
      <Header />
      
      {/* Success Toast */}
      {showSuccessToast && (
        <div className="fixed top-4 right-4 z-50 bg-deep-sage text-white px-6 py-4 rounded-lg shadow-lg flex items-center gap-3 animate-slide-in">
          <span className="material-symbols-outlined">check_circle</span>
          <span>Added to cart!</span>
          <Link href="/cart" className="underline hover:no-underline">
            View cart
          </Link>
        </div>
      )}
      
      <main className="flex-1 container mx-auto px-4 lg:px-24 py-16">
        {/* Breadcrumb */}
        <nav className="mb-8">
          <ol className="flex items-center space-x-2 text-sm text-sage-green">
            <li>
              <Link href="/" className="hover:text-deep-sage transition-colors">
                Home
              </Link>
            </li>
            <li>
              <span className="material-symbols-outlined text-sm">chevron_right</span>
            </li>
            <li>
              <Link href="/products" className="hover:text-deep-sage transition-colors">
                Products
              </Link>
            </li>
            <li>
              <span className="material-symbols-outlined text-sm">chevron_right</span>
            </li>
            <li className="text-deep-sage font-medium truncate max-w-xs">
              {product.title}
            </li>
          </ol>
        </nav>
        
        {/* Product Main Section */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 mb-16">
          {/* Image Gallery */}
          <div>
            {/* Main Image */}
            <div 
              className="relative bg-white rounded-xl overflow-hidden mb-4 aspect-square cursor-pointer group"
              onClick={() => setLightboxOpen(true)}
            >
              <Image
                src={images[selectedImage]}
                alt={product.title}
                fill
                className="object-cover transition-transform group-hover:scale-105"
              />
              {/* Zoom indicator */}
              <div className="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-all flex items-center justify-center">
                <span className="material-symbols-outlined text-white text-4xl opacity-0 group-hover:opacity-100 transition-opacity">
                  zoom_in
                </span>
              </div>
              {/* Wishlist Button */}
              <div className="absolute top-4 right-4 z-10">
                <Heart id={product.id} />
              </div>
            </div>
            
            {/* Thumbnail Images */}
            <div className="grid grid-cols-4 gap-4">
              {images.map((img, idx) => (
                <button
                  key={idx}
                  onClick={() => {
                    setSelectedImage(idx)
                    setLightboxOpen(true)
                  }}
                  className={`relative aspect-square rounded-lg overflow-hidden border-2 transition-all cursor-pointer hover:border-accent-peach ${
                    selectedImage === idx ? 'border-accent-peach' : 'border-transparent'
                  }`}
                >
                  <Image
                    src={img}
                    alt={`${product.title} ${idx + 1}`}
                    fill
                    className="object-cover"
                  />
                </button>
              ))}
            </div>
          </div>
          
          {/* Product Info */}
          <div>
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-4">
              {product.title}
            </h1>
            
            {/* Seller Info */}
            <div className="flex items-center gap-2 mb-4">
              <span className="text-sage-green">by</span>
              <Link 
                href={`/store/${product.storeId}`}
                className="text-accent-peach hover:text-deep-sage transition-colors font-medium"
              >
                Seller Name
              </Link>
            </div>
            
            {/* Rating */}
            <div className="flex items-center gap-2 mb-6">
              <div className="flex">
                {[...Array(5)].map((_, i) => (
                  <span 
                    key={i} 
                    className={`material-symbols-outlined text-xl ${
                      i < Math.floor(product.averageRating || 0) ? 'text-accent-peach' : 'text-gray-300'
                    }`}
                  >
                    star
                  </span>
                ))}
              </div>
              <span className="text-sage-green">
                {product.averageRating?.toFixed(1) || '0.0'} ({product.reviewCount || 0} reviews)
              </span>
            </div>
            
            {/* Price */}
            <div className="mb-6">
              <span className="font-playfair text-4xl font-bold text-deep-sage">
                ${product.basePrice?.toFixed(2)}
              </span>
            </div>
            
            {/* Stock Status */}
            <div className="mb-6">
              {inStock ? (
                <div className="flex items-center gap-2">
                  <span className="material-symbols-outlined text-sage-green">check_circle</span>
                  <span className="text-sage-green font-medium">
                    {lowStock ? `Only ${product.stockQuantity} left in stock` : 'In Stock'}
                  </span>
                </div>
              ) : (
                <div className="flex items-center gap-2">
                  <span className="material-symbols-outlined text-red-500">cancel</span>
                  <span className="text-red-500 font-medium">Out of Stock</span>
                </div>
              )}
            </div>
            
            {/* Quantity Selector */}
            {inStock && (
              <div className="mb-6">
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Quantity
                </label>
                <div className="flex items-center gap-3">
                  <button
                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                    className="w-10 h-10 rounded-lg border border-gray-300 bg-white flex items-center justify-center text-deep-sage hover:bg-soft-peach transition-all"
                  >
                    <span className="material-symbols-outlined">remove</span>
                  </button>
                  <input
                    type="number"
                    min="1"
                    max={product.stockQuantity || 1}
                    value={quantity}
                    onChange={(e) => setQuantity(Math.max(1, Math.min(parseInt(e.target.value) || 1, product.stockQuantity || 1)))}
                    className="w-20 h-10 text-center rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                  />
                  <button
                    onClick={() => setQuantity(Math.min((product.stockQuantity || 1), quantity + 1))}
                    className="w-10 h-10 rounded-lg border border-gray-300 bg-white flex items-center justify-center text-deep-sage hover:bg-soft-peach transition-all"
                  >
                    <span className="material-symbols-outlined">add</span>
                  </button>
                </div>
              </div>
            )}
            
            {/* Action Buttons */}
            <div className="flex gap-4 mb-8">
              <button
                onClick={handleAddToCart}
                disabled={!inStock}
                className="flex-1 bg-deep-sage text-white py-4 px-8 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
              >
                <span className="material-symbols-outlined">shopping_cart</span>
                {inStock ? 'Add to Cart' : 'Out of Stock'}
              </button>
              
              <button
                onClick={handleContactSeller}
                className="px-6 py-4 rounded-full border-2 border-deep-sage text-deep-sage font-medium hover:bg-soft-peach transition-all flex items-center justify-center gap-2"
              >
                <span className="material-symbols-outlined">mail</span>
                Contact
              </button>
            </div>
            
            {/* Shipping Info */}
            <div className="bg-soft-peach rounded-xl p-6 space-y-3">
              <div className="flex items-start gap-3">
                <span className="material-symbols-outlined text-deep-sage">local_shipping</span>
                <div>
                  <p className="font-medium text-deep-sage">Shipping Available</p>
                  <p className="text-sm text-sage-green">Rates set by seller</p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <span className="material-symbols-outlined text-deep-sage">schedule</span>
                <div>
                  <p className="font-medium text-deep-sage">Estimated Delivery</p>
                  <p className="text-sm text-sage-green">3-5 business days</p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <span className="material-symbols-outlined text-deep-sage">verified_user</span>
                <div>
                  <p className="font-medium text-deep-sage">Secure Payment</p>
                  <p className="text-sm text-sage-green">Your information is safe</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        {/* Tabs Section */}
        <div className="mb-16">
          <div className="flex gap-4 mb-8 border-b border-gray-200">
            {['description', 'specifications', 'care', 'shipping'].map(tab => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`pb-4 px-2 font-medium transition-all ${
                  activeTab === tab
                    ? 'text-deep-sage border-b-2 border-accent-peach'
                    : 'text-sage-green hover:text-deep-sage'
                }`}
              >
                {tab.charAt(0).toUpperCase() + tab.slice(1)}
              </button>
            ))}
          </div>
          
          <div className="bg-white rounded-xl p-8">
            {activeTab === 'description' && (
              <div>
                <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                  Product Description
                </h3>
                <p className="text-sage-green leading-relaxed">
                  {product.description || 'No description available.'}
                </p>
              </div>
            )}
            
            {activeTab === 'specifications' && (
              <div>
                <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                  Specifications
                </h3>
                <dl className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="flex">
                    <dt className="font-medium text-deep-sage w-32">Category:</dt>
                    <dd className="text-sage-green">{product.categoryId || 'N/A'}</dd>
                  </div>
                  <div className="flex">
                    <dt className="font-medium text-deep-sage w-32">Stock:</dt>
                    <dd className="text-sage-green">{product.stockQuantity || 0} available</dd>
                  </div>
                </dl>
              </div>
            )}
            
            {activeTab === 'care' && (
              <div>
                <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                  Care Instructions
                </h3>
                <ul className="space-y-3 text-sage-green">
                  <li className="flex items-start gap-2">
                    <span className="material-symbols-outlined text-accent-peach">wb_sunny</span>
                    <span>Place in bright, indirect sunlight</span>
                  </li>
                  <li className="flex items-start gap-2">
                    <span className="material-symbols-outlined text-accent-peach">water_drop</span>
                    <span>Water weekly, allow soil to dry between waterings</span>
                  </li>
                  <li className="flex items-start gap-2">
                    <span className="material-symbols-outlined text-accent-peach">thermostat</span>
                    <span>Keep in temperatures between 65-80°F</span>
                  </li>
                  <li className="flex items-start gap-2">
                    <span className="material-symbols-outlined text-accent-peach">spa</span>
                    <span>Maintain 40-70% humidity</span>
                  </li>
                </ul>
              </div>
            )}
            
            {activeTab === 'shipping' && (
              <div>
                <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                  Shipping & Returns
                </h3>
                <div className="space-y-4 text-sage-green">
                  <p>
                    <strong className="text-deep-sage">Shipping:</strong> We offer free standard shipping on all orders over $50. Orders typically arrive within 3-5 business days.
                  </p>
                  <p>
                    <strong className="text-deep-sage">Returns:</strong> We accept returns within 30 days of delivery. Plants must be in their original condition.
                  </p>
                  <p>
                    <strong className="text-deep-sage">Packaging:</strong> All plants are carefully packaged with protective materials to ensure they arrive in perfect condition.
                  </p>
                </div>
              </div>
            )}
          </div>
        </div>
        
        {/* Reviews Section */}
        <div className="mb-16">
          <div className="flex items-center justify-between mb-8">
            <h2 className="font-playfair text-3xl font-bold text-deep-sage">
              Customer Reviews
            </h2>
            {!showReviewForm && (
              <button 
                onClick={() => {
                  if (!isAuthenticated) {
                    router.push(`/login?redirect=/product/${productId}`)
                    return
                  }
                  setShowReviewForm(true)
                }}
                className="bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all"
              >
                Write a Review
              </button>
            )}
          </div>
          
          {/* Review Form */}
          {showReviewForm && (
            <div className="mb-8">
              <ReviewForm 
                productId={productId}
                onSubmit={() => {
                  setShowReviewForm(false)
                  setReviewRefreshTrigger(prev => prev + 1)
                  // Optionally refetch product to update rating
                  fetchProduct()
                }}
                onCancel={() => setShowReviewForm(false)}
              />
            </div>
          )}
          
          {/* Rating Summary */}
          <div className="mb-8">
            <RatingSummary 
              averageRating={product.averageRating || 0}
              reviewCount={product.reviewCount || 0}
              ratingDistribution={product.ratingDistribution || { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 }}
            />
          </div>
          
          {/* Review List */}
          <ReviewList 
            productId={productId}
            refreshTrigger={reviewRefreshTrigger}
          />
        </div>
        
        {/* Related Products */}
        <RelatedProducts productId={Number(productId)} category={product?.categoryId} />
      </main>
      
      {/* Image Lightbox */}
      <ImageLightbox
        isOpen={lightboxOpen}
        onClose={() => setLightboxOpen(false)}
        images={images.map((url, idx) => ({
          url,
          altText: `${product.title} - Image ${idx + 1}`
        }))}
        initialIndex={selectedImage}
      />
      
      <Footer />
    </div>
  )
}
