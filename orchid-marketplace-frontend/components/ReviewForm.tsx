'use client'

import { useState } from 'react'
import { apiClient } from '@/lib/api-client'

interface ReviewFormProps {
  productId: string
  onSubmit: () => void
  onCancel: () => void
}

export default function ReviewForm({ productId, onSubmit, onCancel }: ReviewFormProps) {
  const [rating, setRating] = useState(0)
  const [hoveredRating, setHoveredRating] = useState(0)
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (rating === 0) {
      setError('Please select a rating')
      return
    }
    
    if (!content.trim()) {
      setError('Please write a review')
      return
    }

    setIsSubmitting(true)
    setError('')

    try {
      await apiClient.createReview(productId, {
        rating,
        title: title.trim(),
        content: content.trim(),
      })
      
      onSubmit()
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to submit review')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="bg-white rounded-xl p-8 shadow-sm">
      <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
        Write a Review
      </h3>
      
      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Rating Selection */}
        <div>
          <label className="block text-sm font-medium text-deep-sage mb-2">
            Your Rating <span className="text-red-500">*</span>
          </label>
          <div className="flex gap-2">
            {[1, 2, 3, 4, 5].map((star) => (
              <button
                key={star}
                type="button"
                onClick={() => setRating(star)}
                onMouseEnter={() => setHoveredRating(star)}
                onMouseLeave={() => setHoveredRating(0)}
                className="transition-transform hover:scale-110 focus:outline-none"
              >
                <span
                  className={`material-symbols-outlined text-4xl ${
                    star <= (hoveredRating || rating)
                      ? 'text-accent-peach'
                      : 'text-gray-300'
                  }`}
                  style={{
                    fontVariationSettings: star <= (hoveredRating || rating)
                      ? '"FILL" 1'
                      : '"FILL" 0'
                  }}
                >
                  star
                </span>
              </button>
            ))}
          </div>
        </div>

        {/* Review Title */}
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-deep-sage mb-2">
            Review Title (Optional)
          </label>
          <input
            id="title"
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            maxLength={100}
            placeholder="Summarize your experience"
            className="w-full px-4 py-3 rounded-lg border border-sage-green/30 focus:ring-2 focus:ring-deep-sage focus:border-transparent outline-none"
          />
          <p className="text-xs text-sage-green mt-1">
            {title.length}/100 characters
          </p>
        </div>

        {/* Review Content */}
        <div>
          <label htmlFor="content" className="block text-sm font-medium text-deep-sage mb-2">
            Your Review <span className="text-red-500">*</span>
          </label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            maxLength={1000}
            rows={6}
            placeholder="Share your experience with this product..."
            className="w-full px-4 py-3 rounded-lg border border-sage-green/30 focus:ring-2 focus:ring-deep-sage focus:border-transparent outline-none resize-none"
          />
          <p className="text-xs text-sage-green mt-1">
            {content.length}/1000 characters
          </p>
        </div>

        {/* Error Message */}
        {error && (
          <div className="bg-red-50 text-red-600 px-4 py-3 rounded-lg flex items-start gap-2">
            <span className="material-symbols-outlined text-xl">error</span>
            <p className="text-sm">{error}</p>
          </div>
        )}

        {/* Action Buttons */}
        <div className="flex gap-4">
          <button
            type="submit"
            disabled={isSubmitting}
            className="flex-1 bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isSubmitting ? 'Submitting...' : 'Submit Review'}
          </button>
          <button
            type="button"
            onClick={onCancel}
            disabled={isSubmitting}
            className="px-6 py-3 text-sage-green hover:text-deep-sage transition-colors disabled:opacity-50"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  )
}
