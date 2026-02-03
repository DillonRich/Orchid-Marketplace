'use client'

interface LoadingSkeletonProps {
  type?: 'card' | 'list' | 'detail' | 'table' | 'grid'
  count?: number
  className?: string
}

export default function LoadingSkeleton({ type = 'card', count = 3, className = '' }: LoadingSkeletonProps) {
  const skeletons = Array.from({ length: count }, (_, i) => i)

  if (type === 'card') {
    return (
      <div className={`grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 ${className}`}>
        {skeletons.map((i) => (
          <div key={i} className="animate-pulse">
            <div className="aspect-[4/5] bg-gray-200 rounded-2xl mb-3"></div>
            <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
            <div className="h-3 bg-gray-200 rounded w-1/2 mb-3"></div>
            <div className="h-5 bg-gray-200 rounded w-1/3"></div>
          </div>
        ))}
      </div>
    )
  }

  if (type === 'list') {
    return (
      <div className={`space-y-4 ${className}`}>
        {skeletons.map((i) => (
          <div key={i} className="bg-white rounded-xl p-6 animate-pulse">
            <div className="flex items-start gap-4">
              <div className="w-20 h-20 bg-gray-200 rounded-lg flex-shrink-0"></div>
              <div className="flex-1 space-y-3">
                <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                <div className="h-3 bg-gray-200 rounded w-full"></div>
                <div className="h-3 bg-gray-200 rounded w-5/6"></div>
              </div>
            </div>
          </div>
        ))}
      </div>
    )
  }

  if (type === 'detail') {
    return (
      <div className={`animate-pulse ${className}`}>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 mb-16">
          {/* Image skeleton */}
          <div>
            <div className="aspect-square bg-gray-200 rounded-xl mb-4"></div>
            <div className="grid grid-cols-4 gap-4">
              {[1, 2, 3, 4].map((i) => (
                <div key={i} className="aspect-square bg-gray-200 rounded-lg"></div>
              ))}
            </div>
          </div>
          
          {/* Info skeleton */}
          <div className="space-y-6">
            <div className="h-12 bg-gray-200 rounded w-3/4"></div>
            <div className="h-6 bg-gray-200 rounded w-1/2"></div>
            <div className="h-8 bg-gray-200 rounded w-1/4"></div>
            <div className="space-y-2">
              <div className="h-4 bg-gray-200 rounded w-full"></div>
              <div className="h-4 bg-gray-200 rounded w-full"></div>
              <div className="h-4 bg-gray-200 rounded w-3/4"></div>
            </div>
            <div className="h-12 bg-gray-200 rounded-full w-full"></div>
          </div>
        </div>
      </div>
    )
  }

  if (type === 'table') {
    return (
      <div className={`bg-white rounded-xl overflow-hidden ${className}`}>
        <div className="animate-pulse">
          {/* Table header */}
          <div className="flex items-center gap-4 p-4 border-b border-gray-200">
            {[1, 2, 3, 4].map((i) => (
              <div key={i} className="flex-1 h-4 bg-gray-200 rounded"></div>
            ))}
          </div>
          
          {/* Table rows */}
          {skeletons.map((i) => (
            <div key={i} className="flex items-center gap-4 p-4 border-b border-gray-100">
              <div className="w-10 h-10 bg-gray-200 rounded"></div>
              <div className="flex-1 h-4 bg-gray-200 rounded"></div>
              <div className="flex-1 h-4 bg-gray-200 rounded"></div>
              <div className="flex-1 h-4 bg-gray-200 rounded"></div>
              <div className="w-20 h-8 bg-gray-200 rounded-full"></div>
            </div>
          ))}
        </div>
      </div>
    )
  }

  if (type === 'grid') {
    return (
      <div className={`grid grid-cols-1 md:grid-cols-2 gap-6 ${className}`}>
        {skeletons.map((i) => (
          <div key={i} className="bg-white rounded-xl p-6 animate-pulse">
            <div className="flex items-center gap-4 mb-4">
              <div className="w-12 h-12 bg-gray-200 rounded-full"></div>
              <div className="flex-1">
                <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
                <div className="h-3 bg-gray-200 rounded w-1/2"></div>
              </div>
            </div>
            <div className="space-y-2">
              <div className="h-3 bg-gray-200 rounded w-full"></div>
              <div className="h-3 bg-gray-200 rounded w-5/6"></div>
              <div className="h-3 bg-gray-200 rounded w-4/6"></div>
            </div>
          </div>
        ))}
      </div>
    )
  }

  return null
}
