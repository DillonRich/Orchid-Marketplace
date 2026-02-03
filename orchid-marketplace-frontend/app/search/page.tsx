'use client'

import { useEffect, useState } from 'react'
import { useRouter, useSearchParams } from 'next/navigation'
import Header from '@/components/Header'
import Footer from '@/components/Footer'
import { apiClient } from '@/lib/api-client'
import { useCartStore } from '@/lib/cart-store'
import { useFavoritesStore } from '@/lib/favorites-store'

interface Product {
  id: number
  name: string
  description: string
  price: number
  stock: number
  imageUrl: string | null
  category: string
  rating: number
  seller: {
    id: number
    storeName: string
  }
}

interface SearchFilters {
  query: string
  category: string
  minPrice: number
  maxPrice: number
  minRating: number
  inStock: boolean
  sortBy: string
}

export default function SearchPage() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const { addItem } = useCartStore()
  const { favorites, toggleFavorite } = useFavoritesStore()
  
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [totalPages, setTotalPages] = useState(1)
  const [currentPage, setCurrentPage] = useState(1)
  const [totalResults, setTotalResults] = useState(0)
  
  const [filters, setFilters] = useState<SearchFilters>({
    query: searchParams.get('q') || '',
    category: searchParams.get('category') || '',
    minPrice: Number(searchParams.get('minPrice')) || 0,
    maxPrice: Number(searchParams.get('maxPrice')) || 1000,
    minRating: Number(searchParams.get('minRating')) || 0,
    inStock: searchParams.get('inStock') === 'true',
    sortBy: searchParams.get('sort') || 'relevance'
  })
  
  const [showFilters, setShowFilters] = useState(false)
  
  const categories = [
    'Phalaenopsis',
    'Cattleya',
    'Dendrobium',
    'Oncidium',
    'Paphiopedilum',
    'Cymbidium',
    'Vanda',
    'Other'
  ]
  
  const sortOptions = [
    { value: 'relevance', label: 'Relevance' },
    { value: 'price-asc', label: 'Price: Low to High' },
    { value: 'price-desc', label: 'Price: High to Low' },
    { value: 'rating', label: 'Highest Rated' },
    { value: 'newest', label: 'Newest Arrivals' }
  ]
  
  useEffect(() => {
    fetchProducts()
  }, [currentPage, filters])
  
  const fetchProducts = async () => {
    try {
      setLoading(true)
      const params: any = {
        page: currentPage,
        size: 12
      }
      
      if (filters.query) params.search = filters.query
      if (filters.category) params.category = filters.category
      if (filters.minPrice > 0) params.minPrice = filters.minPrice
      if (filters.maxPrice < 1000) params.maxPrice = filters.maxPrice
      if (filters.minRating > 0) params.minRating = filters.minRating
      if (filters.inStock) params.inStock = 'true'
      if (filters.sortBy !== 'relevance') params.sort = filters.sortBy
      
      const response = await apiClient.getProducts(params)
      setProducts(response.content || [])
      setTotalPages(response.totalPages || 1)
      setTotalResults(response.totalElements || 0)
    } catch (error) {
      console.error('Failed to fetch products:', error)
    } finally {
      setLoading(false)
    }
  }
  
  const updateFilters = (newFilters: Partial<SearchFilters>) => {
    const updated = { ...filters, ...newFilters }
    setFilters(updated)
    setCurrentPage(1)
    
    // Update URL parameters
    const params = new URLSearchParams()
    if (updated.query) params.set('q', updated.query)
    if (updated.category) params.set('category', updated.category)
    if (updated.minPrice > 0) params.set('minPrice', updated.minPrice.toString())
    if (updated.maxPrice < 1000) params.set('maxPrice', updated.maxPrice.toString())
    if (updated.minRating > 0) params.set('minRating', updated.minRating.toString())
    if (updated.inStock) params.set('inStock', 'true')
    if (updated.sortBy !== 'relevance') params.set('sort', updated.sortBy)
    
    router.push(`/search?${params.toString()}`)
  }
  
  const clearFilters = () => {
    setFilters({
      query: '',
      category: '',
      minPrice: 0,
      maxPrice: 1000,
      minRating: 0,
      inStock: false,
      sortBy: 'relevance'
    })
    setCurrentPage(1)
    router.push('/search')
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
  
  const activeFilterCount = () => {
    let count = 0
    if (filters.category) count++
    if (filters.minPrice > 0) count++
    if (filters.maxPrice < 1000) count++
    if (filters.minRating > 0) count++
    if (filters.inStock) count++
    return count
  }
  
  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1400px] mx-auto px-4 py-8">
          
          {/* Search Header */}
          <div className="mb-8">
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-4">
              Search Orchids
            </h1>
            
            {/* Search Bar */}
            <div className="flex gap-4 mb-6">
              <div className="flex-1 relative">
                <span className="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-sage-green">
                  search
                </span>
                <input
                  type="text"
                  placeholder="Search by name, description, seller..."
                  value={filters.query}
                  onChange={(e) => updateFilters({ query: e.target.value })}
                  className="w-full pl-12 pr-4 py-3 border-2 border-sage-green rounded-full focus:outline-none focus:border-deep-sage"
                />
              </div>
              <button
                onClick={() => setShowFilters(!showFilters)}
                className="bg-soft-peach hover:bg-accent-peach px-6 py-3 rounded-full font-medium text-deep-sage transition-all flex items-center gap-2"
              >
                <span className="material-symbols-outlined">tune</span>
                Filters {activeFilterCount() > 0 && `(${activeFilterCount()})`}
              </button>
            </div>
            
            {/* Active Filter Chips */}
            {activeFilterCount() > 0 && (
              <div className="flex flex-wrap gap-2 mb-4">
                {filters.category && (
                  <div className="bg-white border-2 border-accent-peach rounded-full px-4 py-2 flex items-center gap-2">
                    <span className="text-sm font-medium text-deep-sage">{filters.category}</span>
                    <button onClick={() => updateFilters({ category: '' })} className="hover:text-accent-peach">
                      <span className="material-symbols-outlined text-sm">close</span>
                    </button>
                  </div>
                )}
                {filters.minPrice > 0 && (
                  <div className="bg-white border-2 border-accent-peach rounded-full px-4 py-2 flex items-center gap-2">
                    <span className="text-sm font-medium text-deep-sage">Min: ${filters.minPrice}</span>
                    <button onClick={() => updateFilters({ minPrice: 0 })} className="hover:text-accent-peach">
                      <span className="material-symbols-outlined text-sm">close</span>
                    </button>
                  </div>
                )}
                {filters.maxPrice < 1000 && (
                  <div className="bg-white border-2 border-accent-peach rounded-full px-4 py-2 flex items-center gap-2">
                    <span className="text-sm font-medium text-deep-sage">Max: ${filters.maxPrice}</span>
                    <button onClick={() => updateFilters({ maxPrice: 1000 })} className="hover:text-accent-peach">
                      <span className="material-symbols-outlined text-sm">close</span>
                    </button>
                  </div>
                )}
                {filters.minRating > 0 && (
                  <div className="bg-white border-2 border-accent-peach rounded-full px-4 py-2 flex items-center gap-2">
                    <span className="text-sm font-medium text-deep-sage">{filters.minRating}+ Stars</span>
                    <button onClick={() => updateFilters({ minRating: 0 })} className="hover:text-accent-peach">
                      <span className="material-symbols-outlined text-sm">close</span>
                    </button>
                  </div>
                )}
                {filters.inStock && (
                  <div className="bg-white border-2 border-accent-peach rounded-full px-4 py-2 flex items-center gap-2">
                    <span className="text-sm font-medium text-deep-sage">In Stock</span>
                    <button onClick={() => updateFilters({ inStock: false })} className="hover:text-accent-peach">
                      <span className="material-symbols-outlined text-sm">close</span>
                    </button>
                  </div>
                )}
                <button
                  onClick={clearFilters}
                  className="text-sm text-accent-peach hover:text-deep-sage font-medium px-2"
                >
                  Clear All
                </button>
              </div>
            )}
            
            {/* Results Count & Sort */}
            <div className="flex justify-between items-center">
              <p className="text-sage-green">
                {loading ? 'Searching...' : `${totalResults} results found`}
              </p>
              <div className="flex items-center gap-2">
                <span className="text-sage-green text-sm">Sort by:</span>
                <select
                  value={filters.sortBy}
                  onChange={(e) => updateFilters({ sortBy: e.target.value })}
                  className="border-2 border-sage-green rounded-full px-4 py-2 focus:outline-none focus:border-deep-sage"
                >
                  {sortOptions.map(option => (
                    <option key={option.value} value={option.value}>
                      {option.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>
          
          <div className="flex gap-8">
            
            {/* Filters Panel */}
            {showFilters && (
              <div className="w-80 flex-shrink-0">
                <div className="bg-white rounded-xl p-6 sticky top-8">
                  <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
                    Filters
                  </h2>
                  
                  {/* Category Filter */}
                  <div className="mb-6">
                    <h3 className="font-medium text-deep-sage mb-3">Category</h3>
                    <div className="space-y-2">
                      {categories.map(category => (
                        <label key={category} className="flex items-center gap-2 cursor-pointer">
                          <input
                            type="radio"
                            name="category"
                            checked={filters.category === category}
                            onChange={() => updateFilters({ category: filters.category === category ? '' : category })}
                            className="w-4 h-4 accent-deep-sage"
                          />
                          <span className="text-sage-green">{category}</span>
                        </label>
                      ))}
                    </div>
                  </div>
                  
                  {/* Price Range */}
                  <div className="mb-6">
                    <h3 className="font-medium text-deep-sage mb-3">Price Range</h3>
                    <div className="space-y-3">
                      <div>
                        <label className="text-sm text-sage-green">Min Price: ${filters.minPrice}</label>
                        <input
                          type="range"
                          min="0"
                          max="500"
                          step="10"
                          value={filters.minPrice}
                          onChange={(e) => updateFilters({ minPrice: Number(e.target.value) })}
                          className="w-full accent-deep-sage"
                        />
                      </div>
                      <div>
                        <label className="text-sm text-sage-green">Max Price: ${filters.maxPrice}</label>
                        <input
                          type="range"
                          min="0"
                          max="1000"
                          step="50"
                          value={filters.maxPrice}
                          onChange={(e) => updateFilters({ maxPrice: Number(e.target.value) })}
                          className="w-full accent-deep-sage"
                        />
                      </div>
                    </div>
                  </div>
                  
                  {/* Rating Filter */}
                  <div className="mb-6">
                    <h3 className="font-medium text-deep-sage mb-3">Minimum Rating</h3>
                    <div className="space-y-2">
                      {[4, 3, 2, 1].map(rating => (
                        <label key={rating} className="flex items-center gap-2 cursor-pointer">
                          <input
                            type="radio"
                            name="rating"
                            checked={filters.minRating === rating}
                            onChange={() => updateFilters({ minRating: filters.minRating === rating ? 0 : rating })}
                            className="w-4 h-4 accent-deep-sage"
                          />
                          <div className="flex items-center gap-1">
                            {[...Array(rating)].map((_, i) => (
                              <span key={i} className="material-symbols-outlined text-accent-peach text-sm">
                                star
                              </span>
                            ))}
                            <span className="text-sage-green text-sm ml-1">& up</span>
                          </div>
                        </label>
                      ))}
                    </div>
                  </div>
                  
                  {/* Availability */}
                  <div className="mb-6">
                    <label className="flex items-center gap-2 cursor-pointer">
                      <input
                        type="checkbox"
                        checked={filters.inStock}
                        onChange={(e) => updateFilters({ inStock: e.target.checked })}
                        className="w-4 h-4 accent-deep-sage"
                      />
                      <span className="text-sage-green">In Stock Only</span>
                    </label>
                  </div>
                  
                  <button
                    onClick={clearFilters}
                    className="w-full bg-soft-peach hover:bg-accent-peach text-deep-sage py-3 rounded-full font-medium transition-all"
                  >
                    Clear All Filters
                  </button>
                </div>
              </div>
            )}
            
            {/* Products Grid */}
            <div className="flex-1">
              {loading ? (
                <div className="flex justify-center items-center h-64">
                  <div className="text-center">
                    <span className="material-symbols-outlined text-6xl text-sage-green animate-pulse">
                      potted_plant
                    </span>
                    <p className="text-sage-green mt-4">Loading orchids...</p>
                  </div>
                </div>
              ) : products.length === 0 ? (
                <div className="text-center py-16">
                  <span className="material-symbols-outlined text-8xl text-sage-green mb-4 block">
                    search_off
                  </span>
                  <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-4">
                    No orchids found
                  </h2>
                  <p className="text-sage-green mb-6">
                    Try adjusting your filters or search query
                  </p>
                  <button
                    onClick={clearFilters}
                    className="bg-soft-peach hover:bg-accent-peach text-deep-sage px-8 py-3 rounded-full font-medium transition-all"
                  >
                    Clear Filters
                  </button>
                </div>
              ) : (
                <>
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {products.map(product => (
                      <div key={product.id} className="bg-white rounded-xl overflow-hidden shadow-lg hover:shadow-xl transition-shadow group">
                        <div className="relative h-64">
                          <img
                            src={product.imageUrl || '/images/placeholder.jpg'}
                            alt={product.name}
                            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
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
                            <h3 className="font-playfair text-xl font-bold text-deep-sage flex-1">
                              {product.name}
                            </h3>
                            <p className="text-accent-peach font-bold text-xl">
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
                          
                          <p className="text-sage-green text-sm mb-2">
                            By {product.seller.storeName}
                          </p>
                          
                          <p className="text-sage-green text-sm mb-4 line-clamp-2">
                            {product.description}
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
                            >
                              <span className="material-symbols-outlined">shopping_cart</span>
                            </button>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                  
                  {/* Pagination */}
                  {totalPages > 1 && (
                    <div className="flex justify-center items-center gap-2 mt-12">
                      <button
                        onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                        disabled={currentPage === 1}
                        className="w-10 h-10 bg-white border-2 border-sage-green rounded-full flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed hover:bg-soft-peach transition-all"
                      >
                        <span className="material-symbols-outlined">chevron_left</span>
                      </button>
                      
                      {[...Array(Math.min(5, totalPages))].map((_, i) => {
                        let pageNum
                        if (totalPages <= 5) {
                          pageNum = i + 1
                        } else if (currentPage <= 3) {
                          pageNum = i + 1
                        } else if (currentPage >= totalPages - 2) {
                          pageNum = totalPages - 4 + i
                        } else {
                          pageNum = currentPage - 2 + i
                        }
                        
                        return (
                          <button
                            key={i}
                            onClick={() => setCurrentPage(pageNum)}
                            className={`w-10 h-10 rounded-full font-medium transition-all ${
                              currentPage === pageNum
                                ? 'bg-deep-sage text-white'
                                : 'bg-white border-2 border-sage-green text-deep-sage hover:bg-soft-peach'
                            }`}
                          >
                            {pageNum}
                          </button>
                        )
                      })}
                      
                      <button
                        onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                        disabled={currentPage === totalPages}
                        className="w-10 h-10 bg-white border-2 border-sage-green rounded-full flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed hover:bg-soft-peach transition-all"
                      >
                        <span className="material-symbols-outlined">chevron_right</span>
                      </button>
                    </div>
                  )}
                </>
              )}
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
