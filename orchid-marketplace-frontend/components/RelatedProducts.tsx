'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { apiClient } from '@/lib/api-client'
import { useCartStore } from '@/lib/cart-store'
import { useFavoritesStore } from '@/lib/favorites-store'

interface Product {
  id: number
  name: string
  price: number
  stock: number
  imageUrl: string | null
  rating: number
  seller: {
    id: number
    storeName: string
  }
}

interface RelatedProductsProps {
  productId: number
  category?: string
}

export default function RelatedProducts({ productId, category }: RelatedProductsProps) {
  const router = useRouter()
  const { addItem } = useCartStore()
  const { favorites, toggleFavorite } = useFavoritesStore()
  
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  
  useEffect(() => {
    fetchRelatedProducts()
  }, [productId])
  
  const fetchRelatedProducts = async () => {
    try {
      setLoading(true)
      
      // Fetch products from the same category
      const params: any = {
        size: 6,
        sort: 'rating'
      }
      
      if (category) {
        params.category = category
      }
      
      const response = await apiClient.getProducts(params)
      
      // Filter out the current product
      const filtered = (response.content || []).filter((p: Product) => p.id !== productId)
      
      setProducts(filtered.slice(0, 6))
    } catch (error) {
      console.error('Failed to fetch related products:', error)
    } finally {
      setLoading(false)
    }
  }
  
  const handleAddToCart = (product: Product) => {
    addItem({
      id: product.id.toString(),
      title: product.name,
      price: product.price,
      quantity: 1,
      imageUrl: product.imageUrl || '/images/placeholder.jpg'
    })
  }
  
  if (loading) {
    return (
      <div className="py-16">
        <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-8 text-center">
          Related Orchids
        </h2>
        <div className="flex justify-center">
          <span className="material-symbols-outlined text-6xl text-sage-green animate-pulse">
            potted_plant
          </span>
        </div>
      </div>
    )
  }
  
  if (products.length === 0) {
    return null
  }
  
  return (
    <div className="py-16 bg-soft-peach/30">
      <div className="max-w-[1400px] mx-auto px-4">
        <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-8 text-center">
          You May Also Like
        </h2>
        
        <div className="relative">
          <div className="flex gap-6 overflow-x-auto pb-4 scrollbar-hide snap-x snap-mandatory">
            {products.map(product => (
              <div 
                key={product.id} 
                className="flex-shrink-0 w-80 bg-white rounded-xl overflow-hidden shadow-lg hover:shadow-xl transition-shadow snap-start group"
              >
                <div className="relative h-64">
                  <img
                    src={product.imageUrl || '/images/placeholder.jpg'}
                    alt={product.name}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300 cursor-pointer"
                    onClick={() => router.push(`/products/${product.id}`)}
                  />
                  <button
                    onClick={() => toggleFavorite(product.id)}
                    className="absolute top-4 right-4 w-10 h-10 bg-white rounded-full flex items-center justify-center hover:bg-soft-peach transition-all"
                  >
                    <span className={`material-symbols-outlined ${favorites.includes(product.id) ? 'text-accent-peach' : 'text-sage-green'}`}>
                      {favorites.includes(product.id) ? 'favorite' : 'favorite_border'}
                    </span>
                  </button>
                  {product.stock === 0 && (
                    <div className="absolute inset-0 bg-black/50 flex items-center justify-center">
                      <span className="bg-white text-deep-sage px-4 py-2 rounded-full font-medium">
                        Out of Stock
                      </span>
                    </div>
                  )}
                </div>
                
                <div className="p-6">
                  <div className="flex items-start justify-between mb-2">
                    <h3 
                      className="font-playfair text-xl font-bold text-deep-sage flex-1 cursor-pointer hover:text-sage-green transition-colors"
                      onClick={() => router.push(`/products/${product.id}`)}
                    >
                      {product.name}
                    </h3>
                    <p className="text-accent-peach font-bold text-xl ml-2">
                      ${product.price.toFixed(2)}
                    </p>
                  </div>
                  
                  <div className="flex items-center gap-1 mb-3">
                    {[...Array(5)].map((_, i) => (
                      <span key={i} className={`material-symbols-outlined text-sm ${i < product.rating ? 'text-accent-peach' : 'text-gray-300'}`}>
                        star
                      </span>
                    ))}
                    <span className="text-sm text-sage-green ml-1">
                      ({product.rating.toFixed(1)})
                    </span>
                  </div>
                  
                  <p className="text-sage-green text-sm mb-4">
                    By {product.seller.storeName}
                  </p>
                  
                  <div className="flex gap-2">
                    <button
                      onClick={() => router.push(`/products/${product.id}`)}
                      className="flex-1 bg-soft-peach hover:bg-accent-peach text-deep-sage py-2 rounded-full font-medium transition-all"
                    >
                      View Details
                    </button>
                    <button
                      onClick={() => handleAddToCart(product)}
                      disabled={product.stock === 0}
                      className="w-12 h-12 bg-deep-sage hover:bg-sage-green text-white rounded-full flex items-center justify-center disabled:bg-gray-300 disabled:cursor-not-allowed transition-all"
                      title="Add to Cart"
                    >
                      <span className="material-symbols-outlined">shopping_cart</span>
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
        
        <style jsx>{`
          .scrollbar-hide::-webkit-scrollbar {
            display: none;
          }
          .scrollbar-hide {
            -ms-overflow-style: none;
            scrollbar-width: none;
          }
        `}</style>
      </div>
    </div>
  )
}
