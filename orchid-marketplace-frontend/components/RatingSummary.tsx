'use client'

interface RatingSummaryProps {
  averageRating: number
  reviewCount: number
  ratingDistribution: {
    5: number
    4: number
    3: number
    2: number
    1: number
  }
}

export default function RatingSummary({ 
  averageRating, 
  reviewCount, 
  ratingDistribution 
}: RatingSummaryProps) {
  const totalReviews = reviewCount || 0
  
  const getPercentage = (count: number) => {
    if (totalReviews === 0) return 0
    return (count / totalReviews) * 100
  }

  return (
    <div className="bg-white rounded-xl p-8 shadow-sm">
      <div className="flex items-start gap-8">
        {/* Average Rating */}
        <div className="text-center min-w-[140px]">
          <div className="font-playfair text-6xl font-bold text-deep-sage mb-2">
            {averageRating.toFixed(1)}
          </div>
          <div className="flex justify-center mb-2">
            {[...Array(5)].map((_, i) => (
              <span 
                key={i} 
                className={`material-symbols-outlined ${
                  i < Math.floor(averageRating) ? 'text-accent-peach' : 'text-gray-300'
                }`}
                style={{
                  fontVariationSettings: i < Math.floor(averageRating) ? '"FILL" 1' : '"FILL" 0'
                }}
              >
                star
              </span>
            ))}
          </div>
          <div className="text-sm text-sage-green">
            {totalReviews} {totalReviews === 1 ? 'review' : 'reviews'}
          </div>
        </div>
        
        {/* Rating Distribution */}
        <div className="flex-1 space-y-2">
          {[5, 4, 3, 2, 1].map(rating => {
            const count = ratingDistribution[rating as keyof typeof ratingDistribution] || 0
            const percentage = getPercentage(count)
            
            return (
              <div key={rating} className="flex items-center gap-3">
                <span className="text-sm text-sage-green w-8 flex items-center gap-1">
                  {rating}
                  <span className="material-symbols-outlined text-xs text-accent-peach">star</span>
                </span>
                <div className="flex-1 h-2 bg-gray-200 rounded-full overflow-hidden">
                  <div 
                    className="h-full bg-accent-peach transition-all duration-300"
                    style={{ width: `${percentage}%` }}
                  />
                </div>
                <span className="text-sm text-sage-green w-12 text-right">
                  {count}
                </span>
              </div>
            )
          })}
        </div>
      </div>
    </div>
  )
}
