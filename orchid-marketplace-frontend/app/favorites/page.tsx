'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
// import Link from 'next/link'
import Header from '@/components/Header'
import Footer from '@/components/Footer'
import ProductCard from '@/components/ProductCard'
import { useAuthStore } from '@/lib/auth-store'
import { useToast } from '@/lib/toast-context'
import { apiClient } from '@/lib/api-client'
import type { Product } from '@/lib/types'
import EmptyState from '@/components/EmptyState'
import LoadingSkeleton from '@/components/LoadingSkeleton'

export default function FavoritesPage() {
  const router = useRouter()
  const { isAuthenticated } = useAuthStore()
  const toast = useToast()
  
  const [favorites, setFavorites] = useState<Product[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    if (!isAuthenticated) {
      router.push('/login?redirect=/favorites')
      return
    }
    
    fetchFavorites()
  }, [isAuthenticated])

  const fetchFavorites = async () => {
    try {
      const data = await apiClient.getProducts({ limit: 100, offset: 0 })
      // TODO: Filter to only favorited products once favorites API is implemented
      setFavorites(data.slice(0, 12))
    } catch (error) {
      console.error('Error fetching favorites:', error)
      toast.error('Failed to load favorites')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-warm-cream flex flex-col">
      <Header />

      <main className="flex-1 py-12">
        <div className="container mx-auto px-4 max-w-7xl">
          {/* Page Header */}
          <div className="mb-8">
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
              My Favorites
            </h1>
            <p className="text-sage-green">
              Your saved orchids and plants
            </p>
          </div>

          {/* Loading State */}
          {isLoading && <LoadingSkeleton type="card" count={8} />}

          {/* Empty State */}
          {!isLoading && favorites.length === 0 && (
            <EmptyState
              icon="favorite_border"
              title="No favorites yet"
              description="Start adding orchids to your favorites to see them here"
              actionLabel="Browse Products"
              actionHref="/products"
            />
          )}

          {/* Favorites Grid */}
          {!isLoading && favorites.length > 0 && (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {favorites.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  )
}
