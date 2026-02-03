"use client"

import React, { useState } from 'react'
import Link from 'next/link'
import Image from 'next/image'
import Heart from './Heart'
import QuickViewModal from './QuickViewModal'
import { useCartStore } from '@/lib/cart-store'
import { useToast } from '@/lib/toast-context'

type ProductCardProps = {
  product: {
    id: string
    title?: string
    name?: string
    basePrice?: number
    price?: number
    images?: string[]
    image?: string
    seller?: {
      id?: string
      businessName?: string
      storeName?: string
    }
    sellerName?: string
    averageRating?: number
    rating?: number
    reviewCount?: number
    reviews?: number
  }
}

export default function ProductCard({ product }: ProductCardProps) {
  const addItem = useCartStore((s: any) => s.addItem)
  const toast = useToast()
  const [showQuickView, setShowQuickView] = useState(false)

  // Normalize product data to handle different API response formats
  const productName = product.title || product.name || 'Product'
  const productPrice = product.basePrice || product.price || 0
  const productImage = product.images?.[0] || product.image || '/images/orchid1.jpg'
  const sellerName = product.seller?.storeName || product.seller?.businessName || product.sellerName || 'Unknown Seller'
  const productRating = product.averageRating || product.rating || 0
  const reviewCount = product.reviewCount || product.reviews || 0

  const handleAddToCart = (e: React.MouseEvent) => {
    e.preventDefault() // Prevent navigation when clicking add to cart
    e.stopPropagation()
    addItem({ 
      id: product.id, 
      title: productName, 
      price: Number(productPrice), 
      quantity: 1, 
      imageUrl: productImage 
    })
    toast.success(`${productName} added to cart`)
  }

  const handleQuickView = (e: React.MouseEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setShowQuickView(true)
  }

  // Transform product data for QuickViewModal
  const quickViewProduct = {
    id: product.id,
    name: productName,
    price: productPrice,
    description: 'View product details for more information',
    images: [{ url: productImage, altText: productName }],
    rating: productRating,
    reviewCount: reviewCount,
    stock: 100, // Default stock
    category: 'Orchids'
  }

  return (
    <>
      <Link href={`/product/${product.id}`} className="group flex flex-col gap-3 h-full">
        <div className="relative aspect-[4/5] rounded-2xl overflow-hidden shadow-sm transition-shadow hover:shadow-md">
          <div className="w-full h-full relative">
            <Image 
              src={productImage}
              alt={productName}
              fill
              className="object-cover"
            />
          </div>
          <div className="absolute top-3 right-3 opacity-0 group-hover:opacity-100 transition-opacity">
            <Heart id={product.id} />
          </div>
          
          {/* Quick View Button */}
          <button
            onClick={handleQuickView}
            className="absolute bottom-3 left-1/2 -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-all transform translate-y-2 group-hover:translate-y-0 px-4 py-2 bg-white text-deep-sage rounded-full text-xs font-bold flex items-center gap-2 shadow-lg hover:bg-deep-sage hover:text-white"
          >
            <span className="material-symbols-outlined text-base">visibility</span>
            Quick View
          </button>
        </div>
      
      <div className="flex flex-col gap-2 px-1 flex-grow">
        <h3 className="text-sm font-normal text-deep-sage line-clamp-1">{productName}</h3>
        
        <div className="flex items-center gap-1 flex-wrap">
          {productRating > 0 && (
            <>
              <span className="font-bold text-xs text-deep-sage">{productRating.toFixed(1)}</span>
              <span className="material-symbols-outlined text-xs fill-current text-yellow-500">star</span>
              <span className="text-gray-500 text-xs">({reviewCount})</span>
              <span className="text-gray-400 text-xs">•</span>
            </>
          )}
          <span className="text-sage-green text-xs line-clamp-1">{sellerName}</span>
        </div>
        
        <p className="font-bold text-base mt-1 text-deep-sage">${productPrice.toFixed(2)}</p>
        
        <div className="flex flex-col gap-2 mt-auto">
          <button 
            onClick={handleAddToCart}
            className="h-9 px-3 rounded-full border-2 border-deep-sage text-xs font-bold flex items-center justify-center gap-1 hover:bg-deep-sage hover:text-white transition-colors text-deep-sage whitespace-nowrap"
          >
            <span className="material-symbols-outlined text-base">add</span>
            Add to cart
          </button>
        </div>
      </div>
    </Link>
    
    {/* Quick View Modal */}
    <QuickViewModal
      isOpen={showQuickView}
      onClose={() => setShowQuickView(false)}
      product={quickViewProduct}
    />
  </>
  )
}
