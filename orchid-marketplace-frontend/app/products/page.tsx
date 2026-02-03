'use client'

import { useState, useEffect, Suspense } from 'react'
import { useRouter, useSearchParams } from 'next/navigation'
import Link from 'next/link'
import Header from '@/components/Header'
import Footer from '@/components/Footer'
import ProductCard from '@/components/ProductCard'
import { apiClient } from '@/lib/api-client'
import type { Product } from '@/lib/types'
import LoadingSkeleton from '@/components/LoadingSkeleton'
import EmptyState from '@/components/EmptyState'

function ProductsContent() {
  const router = useRouter()
  const searchParams = useSearchParams()
  
  // URL params
  const query = searchParams.get('q') || ''
  const categoryParam = searchParams.get('category') || ''
  const minPriceParam = searchParams.get('minPrice') || ''
  const maxPriceParam = searchParams.get('maxPrice') || ''
  const ratingParam = searchParams.get('rating') || ''
  const inStockParam = searchParams.get('inStock') || ''
  const sortByParam = searchParams.get('sortBy') || 'createdAt'
  const sortOrderParam = searchParams.get('sortOrder') || 'DESC'
  const pageParam = searchParams.get('page') || '0'
  
  const [products, setProducts] = useState<Product[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [totalPages, setTotalPages] = useState(1)
  
  // Filters state
  const [filters, setFilters] = useState({
    categories: categoryParam.split(',').filter(Boolean),
    minPrice: minPriceParam,
    maxPrice: maxPriceParam,
    rating: ratingParam,
    inStock: inStockParam === 'true',
  })
  
  const [sortBy, setSortBy] = useState(sortByParam)
  const [sortOrder, setSortOrder] = useState(sortOrderParam)
  const [currentPage, setCurrentPage] = useState(parseInt(pageParam))
  
  const categories = [
    { id: 'phalaenopsis', name: 'Phalaenopsis' },
    { id: 'cattleya', name: 'Cattleya' },
    { id: 'dendrobium', name: 'Dendrobium' },
    { id: 'oncidium', name: 'Oncidium' },
    { id: 'cymbidium', name: 'Cymbidium' },
    { id: 'paphiopedilum', name: 'Paphiopedilum' },
  ]
  
  useEffect(() => {
    fetchProducts()
  }, [query, filters, sortBy, sortOrder, currentPage])
  
  const fetchProducts = async () => {
    setIsLoading(true)
    try {
      // Build filter params for backend
      const params: any = {
        page: currentPage,
        size: 40,
      }
      
      // Add search query if present
      if (query) {
        params.query = query
      }
      
      // Add categories filter
      if (filters.categories.length > 0) {
        params.category = filters.categories.join(',')
      }
      
      // Add price range
      if (filters.minPrice) {
        params.minPrice = parseFloat(filters.minPrice)
      }
      if (filters.maxPrice) {
        params.maxPrice = parseFloat(filters.maxPrice)
      }
      
      // Add rating filter
      if (filters.rating) {
        params.minRating = parseFloat(filters.rating)
      }
      
      // Add stock filter
      if (filters.inStock) {
        params.inStock = true
      }
      
      // Add sort params
      params.sortBy = sortBy
      params.sortOrder = sortOrder
      
      // Call API with all filters
      const data = await apiClient.getProducts(params)
      setProducts(data)
      
      // In a real implementation, the backend would return totalPages
      // For now, we'll calculate it based on the result
      setTotalPages(Math.ceil(data.length / 40))
    } catch (error) {
      console.error('Error fetching products:', error)
      setProducts([])
    } finally {
      setIsLoading(false)
    }
  }
  
  const updateURL = (newParams: Record<string, string>) => {
    const params = new URLSearchParams(searchParams.toString())
    
    Object.entries(newParams).forEach(([key, value]) => {
      if (value) {
        params.set(key, value)
      } else {
        params.delete(key)
      }
    })
    
    router.push(`/products?${params.toString()}`)
  }
  
  const toggleCategory = (categoryId: string) => {
    const newCategories = filters.categories.includes(categoryId)
      ? filters.categories.filter(c => c !== categoryId)
      : [...filters.categories, categoryId]
    
    setFilters(prev => ({ ...prev, categories: newCategories }))
    updateURL({ category: newCategories.join(','), page: '0' })
    setCurrentPage(0)
  }
  
  const handlePriceChange = (min: string, max: string) => {
    setFilters(prev => ({ ...prev, minPrice: min, maxPrice: max }))
    updateURL({ minPrice: min, maxPrice: max, page: '0' })
    setCurrentPage(0)
  }
  
  const handleRatingChange = (rating: string) => {
    setFilters(prev => ({ ...prev, rating }))
    updateURL({ rating, page: '0' })
    setCurrentPage(0)
  }
  
  const handleInStockToggle = () => {
    const newInStock = !filters.inStock
    setFilters(prev => ({ ...prev, inStock: newInStock }))
    updateURL({ inStock: newInStock ? 'true' : '', page: '0' })
    setCurrentPage(0)
  }
  
  const handleSortChange = (newSortBy: string, newSortOrder: string) => {
    setSortBy(newSortBy)
    setSortOrder(newSortOrder)
    updateURL({ sortBy: newSortBy, sortOrder: newSortOrder })
  }
  
  const handlePageChange = (page: number) => {
    setCurrentPage(page)
    updateURL({ page: page.toString() })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
  
  const clearFilters = () => {
    setFilters({
      categories: [],
      minPrice: '',
      maxPrice: '',
      rating: '',
      inStock: false,
    })
    router.push('/products')
    setCurrentPage(0)
  }
  
  const hasActiveFilters = filters.categories.length > 0 || filters.minPrice || filters.maxPrice || filters.rating || filters.inStock
  
  return (
    <div className="min-h-screen bg-warm-cream flex flex-col">
      <Header />
      
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
            <li className="text-deep-sage font-medium">
              {query ? `Search: "${query}"` : 'All Products'}
            </li>
          </ol>
        </nav>
        
        {/* Heading */}
        <div className="mb-12">
          <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-4">
            {query ? `Search Results for "${query}"` : 'All Products'}
          </h1>
          <p className="text-sage-green text-lg">
            {isLoading ? 'Loading...' : `${products.length} ${products.length === 1 ? 'product' : 'products'} found`}
          </p>
        </div>
        
        {/* Filters + Products Grid Layout */}
        <div className="flex flex-col lg:flex-row gap-8">
          {/* Filters Sidebar */}
          <aside className="lg:w-72 flex-shrink-0">
            <div className="bg-white rounded-xl shadow-lg p-6 sticky top-4">
              <div className="flex items-center justify-between mb-6">
                <h2 className="font-playfair text-2xl font-bold text-deep-sage">Filters</h2>
                {hasActiveFilters && (
                  <button
                    onClick={clearFilters}
                    className="text-sm text-accent-peach hover:text-deep-sage transition-colors"
                  >
                    Clear all
                  </button>
                )}
              </div>
              
              {/* Categories */}
              <div className="mb-6 pb-6 border-b border-gray-200">
                <h3 className="font-medium text-deep-sage mb-3">Category</h3>
                <div className="space-y-2">
                  {categories.map(category => (
                    <label key={category.id} className="flex items-center cursor-pointer group">
                      <input
                        type="checkbox"
                        checked={filters.categories.includes(category.id)}
                        onChange={() => toggleCategory(category.id)}
                        className="w-4 h-4 rounded border-gray-300 text-accent-peach focus:ring-accent-peach"
                      />
                      <span className="ml-2 text-sm text-sage-green group-hover:text-deep-sage transition-colors">
                        {category.name}
                      </span>
                    </label>
                  ))}
                </div>
              </div>
              
              {/* Price Range */}
              <div className="mb-6 pb-6 border-b border-gray-200">
                <h3 className="font-medium text-deep-sage mb-3">Price</h3>
                <div className="flex items-center gap-2">
                  <input
                    type="number"
                    placeholder="Min"
                    value={filters.minPrice}
                    onChange={(e) => handlePriceChange(e.target.value, filters.maxPrice)}
                    className="w-full px-3 py-2 rounded-lg border border-gray-300 bg-warm-cream text-sm focus:outline-none focus:ring-2 focus:ring-accent-peach"
                  />
                  <span className="text-sage-green">—</span>
                  <input
                    type="number"
                    placeholder="Max"
                    value={filters.maxPrice}
                    onChange={(e) => handlePriceChange(filters.minPrice, e.target.value)}
                    className="w-full px-3 py-2 rounded-lg border border-gray-300 bg-warm-cream text-sm focus:outline-none focus:ring-2 focus:ring-accent-peach"
                  />
                </div>
              </div>
              
              {/* Rating */}
              <div className="mb-6 pb-6 border-b border-gray-200">
                <h3 className="font-medium text-deep-sage mb-3">Minimum Rating</h3>
                <div className="space-y-2">
                  {[5, 4, 3, 2, 1].map(rating => (
                    <label key={rating} className="flex items-center cursor-pointer group">
                      <input
                        type="radio"
                        name="rating"
                        checked={filters.rating === rating.toString()}
                        onChange={() => handleRatingChange(rating.toString())}
                        className="w-4 h-4 border-gray-300 text-accent-peach focus:ring-accent-peach"
                      />
                      <div className="ml-2 flex items-center">
                        {[...Array(rating)].map((_, i) => (
                          <span key={i} className="material-symbols-outlined text-lg text-accent-peach">
                            star
                          </span>
                        ))}
                        <span className="ml-1 text-sm text-sage-green group-hover:text-deep-sage transition-colors">
                          & up
                        </span>
                      </div>
                    </label>
                  ))}
                </div>
              </div>
              
              {/* Stock Status */}
              <div>
                <label className="flex items-center cursor-pointer group">
                  <input
                    type="checkbox"
                    checked={filters.inStock}
                    onChange={handleInStockToggle}
                    className="w-4 h-4 rounded border-gray-300 text-accent-peach focus:ring-accent-peach"
                  />
                  <span className="ml-2 text-sm text-sage-green group-hover:text-deep-sage transition-colors">
                    In Stock Only
                  </span>
                </label>
              </div>
            </div>
          </aside>
          
          {/* Products Grid */}
          <div className="flex-1">
            {/* Sort Bar */}
            <div className="flex items-center justify-between mb-8">
              <div className="text-sm text-sage-green">
                {!isLoading && products.length > 0 && (
                  <span>Showing {currentPage * 40 + 1}-{Math.min((currentPage + 1) * 40, products.length)} of {products.length}</span>
                )}
              </div>
              
              <div className="flex items-center gap-2">
                <span className="text-sm text-sage-green">Sort by:</span>
                <select
                  value={`${sortBy}-${sortOrder}`}
                  onChange={(e) => {
                    const [newSortBy, newSortOrder] = e.target.value.split('-')
                    handleSortChange(newSortBy, newSortOrder)
                  }}
                  className="px-4 py-2 rounded-lg border border-gray-300 bg-white text-deep-sage text-sm focus:outline-none focus:ring-2 focus:ring-accent-peach"
                >
                  <option value="createdAt-DESC">Newest</option>
                  <option value="basePrice-ASC">Price: Low to High</option>
                  <option value="basePrice-DESC">Price: High to Low</option>
                  <option value="averageRating-DESC">Highest Rated</option>
                  <option value="title-ASC">Name: A to Z</option>
                  <option value="title-DESC">Name: Z to A</option>
                </select>
              </div>
            </div>
            
            {/* Loading State */}
            {isLoading && <LoadingSkeleton type="card" count={16} />}
            
            {/* No Results */}
            {!isLoading && products.length === 0 && (
              <EmptyState
                icon="search_off"
                title="No products found"
                description="Try adjusting your filters or search terms"
                actionLabel="Clear filters"
                onAction={clearFilters}
              />
            )}
            
            {/* Products Grid */}
            {!isLoading && products.length > 0 && (
              <>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                  {products.map(product => (
                    <ProductCard key={product.id} product={product} />
                  ))}
                </div>
                
                {/* Pagination */}
                {totalPages > 1 && (
                  <div className="flex flex-col items-center gap-4 mt-12">
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 0}
                        className="px-4 py-2 rounded-lg border border-gray-300 bg-white text-deep-sage hover:bg-soft-peach transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-1"
                      >
                        <span className="material-symbols-outlined text-sm">chevron_left</span>
                        Previous
                      </button>
                      
                      {/* Show first page */}
                      <button
                        onClick={() => handlePageChange(0)}
                        className={`w-10 h-10 rounded-lg font-medium transition-all ${
                          currentPage === 0
                            ? 'bg-deep-sage text-white'
                            : 'border border-gray-300 bg-white text-deep-sage hover:bg-soft-peach'
                        }`}
                      >
                        1
                      </button>
                      
                      {/* Show ellipsis if needed */}
                      {currentPage > 3 && <span className="text-sage-green">...</span>}
                      
                      {/* Show pages around current */}
                      {[...Array(totalPages)].map((_, i) => {
                        if (i === 0 || i === totalPages - 1) return null
                        if (i < currentPage - 2 || i > currentPage + 2) return null
                        return (
                          <button
                            key={i}
                            onClick={() => handlePageChange(i)}
                            className={`w-10 h-10 rounded-lg font-medium transition-all ${
                              currentPage === i
                                ? 'bg-deep-sage text-white'
                                : 'border border-gray-300 bg-white text-deep-sage hover:bg-soft-peach'
                            }`}
                          >
                            {i + 1}
                          </button>
                        )
                      })}
                      
                      {/* Show ellipsis if needed */}
                      {currentPage < totalPages - 4 && <span className="text-sage-green">...</span>}
                      
                      {/* Show last page */}
                      {totalPages > 1 && (
                        <button
                          onClick={() => handlePageChange(totalPages - 1)}
                          className={`w-10 h-10 rounded-lg font-medium transition-all ${
                            currentPage === totalPages - 1
                              ? 'bg-deep-sage text-white'
                              : 'border border-gray-300 bg-white text-deep-sage hover:bg-soft-peach'
                          }`}
                        >
                          {totalPages}
                        </button>
                      )}
                      
                      <button
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage >= totalPages - 1}
                        className="px-4 py-2 rounded-lg border border-gray-300 bg-white text-deep-sage hover:bg-soft-peach transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-1"
                      >
                        Next
                        <span className="material-symbols-outlined text-sm">chevron_right</span>
                      </button>
                    </div>
                    <p className="text-sm text-sage-green">
                      Page {currentPage + 1} of {totalPages}
                    </p>
                  </div>
                )}
              </>
            )}
          </div>
        </div>
      </main>
      
      <Footer />
    </div>
  )
}

export default function ProductsPage() {
  return (
    <Suspense fallback={
      <div className="min-h-screen bg-warm-cream flex items-center justify-center">
        <div className="text-center">
          <div className="w-12 h-12 border-4 border-accent-peach border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-sage-green">Loading products...</p>
        </div>
      </div>
    }>
      <ProductsContent />
    </Suspense>
  )
}
