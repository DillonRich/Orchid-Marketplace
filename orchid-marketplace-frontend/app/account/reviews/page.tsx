'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import Image from 'next/image'
// import { apiClient } from '@/lib/api-client'
// import { useAuthStore } from '@/lib/auth-store'

interface Review {
  id: string
  productId: string
  productTitle: string
  productImage: string
  rating: number
  title?: string
  content: string
  createdAt: string
  verifiedPurchase: boolean
  helpfulCount: number
  sellerResponse?: {
    content: string
    createdAt: string
    sellerName: string
  }
}

export default function MyReviewsPage() {
  // const { user } = useAuthStore()
  const [reviews, setReviews] = useState<Review[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [filter, setFilter] = useState<'all' | 'pending' | 'responded'>('all')
  
  useEffect(() => {
    fetchReviews()
  }, [])
  
  const fetchReviews = async () => {
    setIsLoading(true)
    try {
      // Mock data - replace with actual API call
      setReviews([
        {
          id: '1',
          productId: '1',
          productTitle: 'Phalaenopsis Orchid - Purple Beauty',
          productImage: '/images/orchid1.jpg',
          rating: 5,
          title: 'Beautiful orchid!',
          content: 'This orchid exceeded my expectations. It arrived in perfect condition and has been blooming beautifully for weeks. Highly recommend!',
          createdAt: '2024-01-15T10:30:00Z',
          verifiedPurchase: true,
          helpfulCount: 12,
          sellerResponse: {
            content: 'Thank you so much for your kind review! We\'re thrilled that you\'re enjoying your orchid. Happy growing!',
            createdAt: '2024-01-16T14:20:00Z',
            sellerName: 'Orchid Paradise'
          }
        }
      ])
    } catch (error) {
      console.error('Error fetching reviews:', error)
    } finally {
      setIsLoading(false)
    }
  }
  
  const filteredReviews = reviews.filter(review => {
    if (filter === 'pending') return !review.sellerResponse
    if (filter === 'responded') return !!review.sellerResponse
    return true
  })
  
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }
  
  if (isLoading) {
    return (
      <div className="space-y-8">
        <div className="h-8 bg-gray-200 rounded w-1/3 animate-pulse"></div>
        <div className="space-y-4">
          {[1, 2, 3].map((i) => (
            <div key={i} className="bg-white rounded-xl p-6 animate-pulse">
              <div className="flex gap-4">
                <div className="w-24 h-24 bg-gray-200 rounded-lg"></div>
                <div className="flex-1 space-y-3">
                  <div className="h-4 bg-gray-200 rounded w-1/2"></div>
                  <div className="h-4 bg-gray-200 rounded w-full"></div>
                  <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    )
  }
  
  return (
    <div className="space-y-8">
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          My Reviews
        </h1>
        <p className="text-sage-green">
          View and manage your product reviews
        </p>
      </div>
      
      {/* Filter Tabs */}
      <div className="flex items-center gap-2 border-b border-gray-200">
        <button
          onClick={() => setFilter('all')}
          className={`px-6 py-3 font-medium transition-all border-b-2 ${
            filter === 'all'
              ? 'border-accent-peach text-deep-sage'
              : 'border-transparent text-sage-green hover:text-deep-sage'
          }`}
        >
          All Reviews ({reviews.length})
        </button>
        <button
          onClick={() => setFilter('pending')}
          className={`px-6 py-3 font-medium transition-all border-b-2 ${
            filter === 'pending'
              ? 'border-accent-peach text-deep-sage'
              : 'border-transparent text-sage-green hover:text-deep-sage'
          }`}
        >
          Pending Response ({reviews.filter(r => !r.sellerResponse).length})
        </button>
        <button
          onClick={() => setFilter('responded')}
          className={`px-6 py-3 font-medium transition-all border-b-2 ${
            filter === 'responded'
              ? 'border-accent-peach text-deep-sage'
              : 'border-transparent text-sage-green hover:text-deep-sage'
          }`}
        >
          Responded ({reviews.filter(r => !!r.sellerResponse).length})
        </button>
      </div>
      
      {/* Reviews List */}
      {filteredReviews.length > 0 ? (
        <div className="space-y-6">
          {filteredReviews.map((review) => (
            <div key={review.id} className="bg-white rounded-xl p-6 shadow-sm">
              {/* Product Info */}
              <div className="flex items-start gap-4 mb-6 pb-6 border-b border-gray-100">
                <Link href={`/products/${review.productId}`} className="relative w-24 h-24 rounded-lg overflow-hidden flex-shrink-0">
                  <Image
                    src={review.productImage}
                    alt={review.productTitle}
                    fill
                    className="object-cover hover:scale-105 transition-transform"
                  />
                </Link>
                <div className="flex-1">
                  <Link 
                    href={`/products/${review.productId}`}
                    className="font-medium text-deep-sage hover:text-accent-peach transition-colors mb-1 block"
                  >
                    {review.productTitle}
                  </Link>
                  <div className="flex items-center gap-2 mb-2">
                    <div className="flex">
                      {[1, 2, 3, 4, 5].map((star) => (
                        <span
                          key={star}
                          className={`material-symbols-outlined text-xl ${
                            star <= review.rating ? 'text-accent-peach fill-current' : 'text-gray-300'
                          }`}
                        >
                          star
                        </span>
                      ))}
                    </div>
                    {review.verifiedPurchase && (
                      <span className="text-sm text-sage-green flex items-center gap-1">
                        <span className="material-symbols-outlined text-sm">verified</span>
                        Verified Purchase
                      </span>
                    )}
                  </div>
                  <p className="text-sm text-sage-green">{formatDate(review.createdAt)}</p>
                </div>
              </div>
              
              {/* Review Content */}
              <div className="mb-4">
                {review.title && (
                  <h3 className="font-medium text-deep-sage mb-2">{review.title}</h3>
                )}
                <p className="text-sage-green leading-relaxed">{review.content}</p>
              </div>
              
              {/* Review Stats */}
              <div className="flex items-center gap-4 text-sm text-sage-green mb-4">
                <span className="flex items-center gap-1">
                  <span className="material-symbols-outlined text-sm">thumb_up</span>
                  {review.helpfulCount} found helpful
                </span>
              </div>
              
              {/* Seller Response */}
              {review.sellerResponse && (
                <div className="bg-soft-peach/30 rounded-lg p-4 border-l-4 border-accent-peach">
                  <div className="flex items-center gap-2 mb-2">
                    <span className="material-symbols-outlined text-deep-sage">store</span>
                    <span className="font-medium text-deep-sage">{review.sellerResponse.sellerName}</span>
                    <span className="text-sm text-sage-green">
                      · {formatDate(review.sellerResponse.createdAt)}
                    </span>
                  </div>
                  <p className="text-sage-green">{review.sellerResponse.content}</p>
                </div>
              )}
            </div>
          ))}
        </div>
      ) : (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            rate_review
          </span>
          <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
            No reviews yet
          </h3>
          <p className="text-sage-green mb-6">
            Start reviewing products you've purchased to help other shoppers!
          </p>
          <Link
            href="/account/orders"
            className="inline-block bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
          >
            View Your Orders
          </Link>
        </div>
      )}
    </div>
  )
}
