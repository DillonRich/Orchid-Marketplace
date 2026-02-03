'use client'

import { useState, useEffect } from 'react'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'

interface Review {
  id: string
  rating: number
  title?: string
  content: string
  createdAt: string
  user: {
    id: string
    name: string
    avatarUrl?: string
  }
  verifiedPurchase: boolean
  helpfulCount: number
  sellerResponse?: {
    content: string
    createdAt: string
    sellerName: string
  }
}

interface ReviewListProps {
  productId: string
  refreshTrigger?: number
}

type SortOption = 'recent' | 'helpful' | 'highest' | 'lowest'

export default function ReviewList({ productId, refreshTrigger }: ReviewListProps) {
  const { isAuthenticated } = useAuthStore()
  const [reviews, setReviews] = useState<Review[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [sortBy, setSortBy] = useState<SortOption>('recent')
  const [page, setPage] = useState(1)
  const [hasMore, setHasMore] = useState(false)
  const reviewsPerPage = 5

  useEffect(() => {
    fetchReviews()
  }, [productId, sortBy, page, refreshTrigger])

  const fetchReviews = async () => {
    setIsLoading(true)
    try {
      const data = await apiClient.getProductReviews(productId)
      
      // Sort reviews
      let sorted = [...data]
      switch (sortBy) {
        case 'recent':
          sorted.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
          break
        case 'helpful':
          sorted.sort((a, b) => b.helpfulCount - a.helpfulCount)
          break
        case 'highest':
          sorted.sort((a, b) => b.rating - a.rating)
          break
        case 'lowest':
          sorted.sort((a, b) => a.rating - b.rating)
          break
      }
      
      // Paginate
      const startIndex = (page - 1) * reviewsPerPage
      const endIndex = startIndex + reviewsPerPage
      const paginatedReviews = sorted.slice(startIndex, endIndex)
      
      setReviews(paginatedReviews)
      setHasMore(endIndex < sorted.length)
    } catch (error) {
      console.error('Error fetching reviews:', error)
      setReviews([])
    } finally {
      setIsLoading(false)
    }
  }

  const handleHelpfulClick = async (reviewId: string) => {
    if (!isAuthenticated) return
    
    try {
      // TODO: Implement helpful vote API endpoint
      // await apiClient.markReviewHelpful(reviewId)
      
      // Optimistic update
      setReviews(reviews.map(review => 
        review.id === reviewId
          ? { ...review, helpfulCount: review.helpfulCount + 1 }
          : review
      ))
    } catch (error) {
      console.error('Error marking review as helpful:', error)
    }
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    const now = new Date()
    const diffInMs = now.getTime() - date.getTime()
    const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24))
    
    if (diffInDays === 0) return 'Today'
    if (diffInDays === 1) return 'Yesterday'
    if (diffInDays < 7) return `${diffInDays} days ago`
    if (diffInDays < 30) return `${Math.floor(diffInDays / 7)} weeks ago`
    if (diffInDays < 365) return `${Math.floor(diffInDays / 30)} months ago`
    return date.toLocaleDateString()
  }

  if (isLoading && page === 1) {
    return (
      <div className="space-y-6">
        {[1, 2, 3].map((i) => (
          <div key={i} className="bg-white rounded-xl p-6 animate-pulse">
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-gray-200"></div>
                <div>
                  <div className="h-4 bg-gray-200 rounded w-32 mb-2"></div>
                  <div className="h-3 bg-gray-200 rounded w-24"></div>
                </div>
              </div>
              <div className="h-3 bg-gray-200 rounded w-20"></div>
            </div>
            <div className="space-y-2">
              <div className="h-4 bg-gray-200 rounded"></div>
              <div className="h-4 bg-gray-200 rounded w-3/4"></div>
            </div>
          </div>
        ))}
      </div>
    )
  }

  if (reviews.length === 0) {
    return (
      <div className="bg-white rounded-xl p-12 text-center">
        <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
          rate_review
        </span>
        <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
          No reviews yet
        </h3>
        <p className="text-sage-green">
          Be the first to share your experience with this product!
        </p>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Sort Options */}
      <div className="flex items-center justify-between">
        <p className="text-sage-green">
          Showing {reviews.length} reviews
        </p>
        <div className="flex items-center gap-3">
          <span className="text-sm text-sage-green">Sort by:</span>
          <select
            value={sortBy}
            onChange={(e) => {
              setSortBy(e.target.value as SortOption)
              setPage(1)
            }}
            className="px-4 py-2 rounded-lg border border-sage-green/30 focus:ring-2 focus:ring-deep-sage focus:border-transparent outline-none text-sm"
          >
            <option value="recent">Most Recent</option>
            <option value="helpful">Most Helpful</option>
            <option value="highest">Highest Rating</option>
            <option value="lowest">Lowest Rating</option>
          </select>
        </div>
      </div>

      {/* Review Cards */}
      {reviews.map((review) => (
        <div key={review.id} className="bg-white rounded-xl p-6 shadow-sm">
          {/* Review Header */}
          <div className="flex items-start justify-between mb-4">
            <div>
              <div className="flex items-center gap-3 mb-2">
                <div className="w-10 h-10 rounded-full bg-soft-peach flex items-center justify-center overflow-hidden">
                  {review.user.avatarUrl ? (
                    <img src={review.user.avatarUrl} alt={review.user.name} className="w-full h-full object-cover" />
                  ) : (
                    <span className="material-symbols-outlined text-deep-sage">person</span>
                  )}
                </div>
                <div>
                  <p className="font-medium text-deep-sage">{review.user.name}</p>
                  {review.verifiedPurchase && (
                    <p className="text-sm text-sage-green flex items-center gap-1">
                      <span className="material-symbols-outlined text-sm">verified</span>
                      Verified Purchase
                    </p>
                  )}
                </div>
              </div>
              
              {/* Star Rating */}
              <div className="flex">
                {[...Array(5)].map((_, i) => (
                  <span 
                    key={i} 
                    className={`material-symbols-outlined text-sm ${
                      i < review.rating ? 'text-accent-peach' : 'text-gray-300'
                    }`}
                    style={{
                      fontVariationSettings: i < review.rating ? '"FILL" 1' : '"FILL" 0'
                    }}
                  >
                    star
                  </span>
                ))}
              </div>
            </div>
            
            <span className="text-sm text-sage-green">
              {formatDate(review.createdAt)}
            </span>
          </div>

          {/* Review Title */}
          {review.title && (
            <h4 className="font-medium text-deep-sage mb-2">
              {review.title}
            </h4>
          )}

          {/* Review Content */}
          <p className="text-sage-green mb-4 whitespace-pre-wrap">
            {review.content}
          </p>

          {/* Helpful Button */}
          <button
            onClick={() => handleHelpfulClick(review.id)}
            disabled={!isAuthenticated}
            className={`flex items-center gap-2 text-sm transition-colors ${
              isAuthenticated
                ? 'text-sage-green hover:text-deep-sage'
                : 'text-gray-400 cursor-not-allowed'
            }`}
          >
            <span className="material-symbols-outlined text-lg">thumb_up</span>
            <span>Helpful ({review.helpfulCount})</span>
          </button>

          {/* Seller Response */}
          {review.sellerResponse && (
            <div className="mt-4 pl-6 border-l-2 border-sage-green/30">
              <div className="flex items-center gap-2 mb-2">
                <span className="material-symbols-outlined text-deep-sage text-sm">
                  storefront
                </span>
                <p className="font-medium text-deep-sage text-sm">
                  Response from {review.sellerResponse.sellerName}
                </p>
                <span className="text-xs text-sage-green">
                  {formatDate(review.sellerResponse.createdAt)}
                </span>
              </div>
              <p className="text-sm text-sage-green">
                {review.sellerResponse.content}
              </p>
            </div>
          )}
        </div>
      ))}

      {/* Pagination */}
      {(page > 1 || hasMore) && (
        <div className="flex justify-center gap-4">
          <button
            onClick={() => setPage(page - 1)}
            disabled={page === 1}
            className="px-6 py-2 rounded-full border border-sage-green/30 text-sage-green hover:bg-sage-green/10 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Previous
          </button>
          <button
            onClick={() => setPage(page + 1)}
            disabled={!hasMore}
            className="px-6 py-2 rounded-full border border-sage-green/30 text-sage-green hover:bg-sage-green/10 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Next
          </button>
        </div>
      )}
    </div>
  )
}
