"use client"

import { useEffect, useState } from 'react'
import ProductCard from '@/components/ProductCard'
import { useFavoritesClientStore } from '@/lib/favorites-client'
import { apiClient } from '@/lib/api-client'

export default function FavoritesPage() {
  const favoriteItems = useFavoritesClientStore((s: any) => s.items)
  const [products, setProducts] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  
  useEffect(() => {
    const fetchFavoriteProducts = async () => {
      const favoriteIds = Object.keys(favoriteItems || {})
      
      if (favoriteIds.length === 0) {
        setProducts([])
        setLoading(false)
        return
      }
      
      try {
        // Fetch all favorite products
        const productPromises = favoriteIds.map(id => 
          apiClient.getProduct(id).catch(() => null)
        )
        const results = await Promise.all(productPromises)
        const validProducts = results.filter(p => p !== null)
        setProducts(validProducts)
      } catch (error) {
        console.error('Error fetching favorite products:', error)
      } finally {
        setLoading(false)
      }
    }
    
    fetchFavoriteProducts()
  }, [favoriteItems])
  
  if (loading) {
    return (
      <div className="min-h-screen bg-warm-cream py-12">
        <div className="container mx-auto px-6">
          <h1 className="text-3xl font-serif font-bold text-deep-sage mb-8">My Favorites</h1>
          <div className="flex justify-center items-center py-20">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-deep-sage"></div>
          </div>
        </div>
      </div>
    )
  }
  
  return (
    <div className="min-h-screen bg-warm-cream py-12">
      <div className="container mx-auto px-6">
        <h1 className="text-3xl font-serif font-bold text-deep-sage mb-2">My Favorites</h1>
        <p className="text-sage-green mb-8">{products.length} {products.length === 1 ? 'item' : 'items'}</p>
        
        {products.length === 0 ? (
          <div className="text-center py-20">
            <span className="material-symbols-outlined text-[120px] text-gray-300">favorite</span>
            <h2 className="text-2xl font-serif text-deep-sage mt-4 mb-2">No favorites yet</h2>
            <p className="text-sage-green mb-6">Start exploring and save your favorite orchids!</p>
            <a href="/" className="inline-block bg-deep-sage text-white px-8 py-3 rounded-full hover:bg-sage-green transition-all">
              Browse Products
            </a>
          </div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
