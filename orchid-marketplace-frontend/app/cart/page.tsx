'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import Link from 'next/link'
import Image from 'next/image'
import Header from '@/components/Header'
import Footer from '@/components/Footer'
import ConfirmDialog from '@/components/ConfirmDialog'
import EmptyState from '@/components/EmptyState'
import { useCartStore } from '@/lib/cart-store'
import { apiClient } from '@/lib/api-client'

export default function CartPage() {
  const router = useRouter()
  const { items, itemCount, updateQty, removeItem, totalPrice } = useCartStore()
  const total = totalPrice()
  
  const [isLoading] = useState(false)
  const [promoCode, setPromoCode] = useState('')
  const [promoApplied, setPromoApplied] = useState(false)
  const [promoDiscount, setPromoDiscount] = useState(0)
  const [promoError, setPromoError] = useState('')
  const [isApplyingPromo, setIsApplyingPromo] = useState(false)
  const [removeConfirm, setRemoveConfirm] = useState<string | null>(null)
  
  const subtotal = total
  const tax = subtotal * 0.08 // 8% tax
  const shipping = subtotal >= 50 ? 0 : 5.99
  const discount = promoApplied ? promoDiscount : 0
  const grandTotal = subtotal + tax + shipping - discount
  
  const handleQuantityChange = async (itemId: string, newQuantity: number) => {
    if (newQuantity < 1) return
    updateQty(itemId, newQuantity)
    
    try {
      await apiClient.updateCartItem(itemId, { quantity: newQuantity })
    } catch (error) {
      console.error('Error updating quantity:', error)
    }
  }
  
  const handleRemoveItem = async (itemId: string) => {
    try {
      await apiClient.removeCartItem(itemId)
      removeItem(itemId)
      setRemoveConfirm(null)
    } catch (error) {
      console.error('Error removing item:', error)
    }
  }
  
  const handleApplyPromo = async () => {
    if (!promoCode.trim()) return
    
    setIsApplyingPromo(true)
    setPromoError('')
    
    try {
      // Call backend to validate promo code
      const response = await apiClient.validatePromoCode(promoCode, subtotal)
      
      setPromoApplied(true)
      setPromoDiscount(response.discountAmount)
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Invalid or expired promo code'
      setPromoError(errorMessage)
      setPromoApplied(false)
      setPromoDiscount(0)
    } finally {
      setIsApplyingPromo(false)
    }
  }
  
  const handleRemovePromo = () => {
    setPromoCode('')
    setPromoApplied(false)
    setPromoDiscount(0)
    setPromoError('')
  }
  
  const handleCheckout = () => {
    // Allow both authenticated and guest users to checkout
    router.push('/checkout')
  }
  
  if (itemCount === 0) {
    return (
      <div className="min-h-screen bg-warm-cream flex flex-col">
        <Header />
        <main className="flex-1 container mx-auto px-4 lg:px-24 py-16 flex items-center justify-center">
          <EmptyState
            icon="shopping_cart"
            title="Your cart is empty"
            description="Looks like you haven't added any items to your cart yet. Start shopping to fill it up!"
            actionLabel="Browse Products"
            actionHref="/products"
          />
        </main>
        <Footer />
      </div>
    )
  }
  
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
            <li className="text-deep-sage font-medium">Cart</li>
          </ol>
        </nav>
        
        {/* Heading */}
        <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-2">
          Shopping Cart
        </h1>
        <p className="text-sage-green text-lg mb-12">
          {itemCount} {itemCount === 1 ? 'item' : 'items'} in your cart
        </p>
        
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Cart Items */}
          <div className="lg:col-span-2 space-y-4">
            {items.map((item: any) => (
              <div key={item.id} className="bg-white rounded-xl p-6 flex gap-6">
                {/* Product Image */}
                <div className="relative w-32 h-32 flex-shrink-0 rounded-lg overflow-hidden bg-gray-100">
                  <Image
                    src={item.imageUrl || '/placeholder.jpg'}
                    alt={item.title}
                    fill
                    className="object-cover"
                  />
                </div>
                
                {/* Product Info */}
                <div className="flex-1">
                  <div className="flex justify-between mb-2">
                    <Link 
                      href={`/product/${item.id}`}
                      className="font-medium text-deep-sage hover:text-accent-peach transition-colors text-lg"
                    >
                      {item.title}
                    </Link>
                    <button
                      onClick={() => setRemoveConfirm(item.id)}
                      className="text-sage-green hover:text-red-500 transition-colors"
                      aria-label="Remove item"
                    >
                      <span className="material-symbols-outlined">delete</span>
                    </button>
                  </div>
                  
                  <p className="text-sage-green text-sm mb-4">by Seller Name</p>
                  
                  <div className="flex items-center justify-between">
                    {/* Quantity Selector */}
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                        disabled={item.quantity <= 1}
                        className="w-8 h-8 rounded-lg border border-gray-300 bg-white flex items-center justify-center text-deep-sage hover:bg-soft-peach transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                      >
                        <span className="material-symbols-outlined text-sm">remove</span>
                      </button>
                      <span className="w-12 text-center text-deep-sage font-medium">
                        {item.quantity}
                      </span>
                      <button
                        onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                        className="w-8 h-8 rounded-lg border border-gray-300 bg-white flex items-center justify-center text-deep-sage hover:bg-soft-peach transition-all"
                      >
                        <span className="material-symbols-outlined text-sm">add</span>
                      </button>
                    </div>
                    
                    {/* Price */}
                    <div className="text-right">
                      <div className="font-playfair text-2xl font-bold text-deep-sage">
                        ${(item.price * item.quantity).toFixed(2)}
                      </div>
                      <div className="text-sm text-sage-green">
                        ${item.price.toFixed(2)} each
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
          
          {/* Order Summary */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-xl p-6 sticky top-4">
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
                Order Summary
              </h2>
              
              {/* Promo Code */}
              <div className="mb-6">
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Promo Code
                </label>
                <div className="flex gap-2">
                  <input
                    type="text"
                    value={promoCode}
                    onChange={(e) => {
                      setPromoCode(e.target.value.toUpperCase())
                      setPromoError('')
                    }}
                    placeholder="Enter code"
                    disabled={promoApplied}
                    className="flex-1 px-4 py-2 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach disabled:opacity-50"
                  />
                  {!promoApplied ? (
                    <button
                      onClick={handleApplyPromo}
                      disabled={isApplyingPromo || !promoCode.trim()}
                      className="px-4 py-2 bg-soft-peach text-deep-sage rounded-lg font-medium hover:bg-primary-peach transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      {isApplyingPromo ? '...' : 'Apply'}
                    </button>
                  ) : (
                    <button
                      onClick={handleRemovePromo}
                      className="px-4 py-2 bg-red-100 text-red-600 rounded-lg font-medium hover:bg-red-200 transition-all"
                    >
                      Remove
                    </button>
                  )}
                </div>
                {promoApplied && (
                  <p className="mt-2 text-sm text-green-600 flex items-center gap-1">
                    <span className="material-symbols-outlined text-sm">check_circle</span>
                    Promo code applied!
                  </p>
                )}
                {promoError && (
                  <p className="mt-2 text-sm text-red-600 flex items-center gap-1">
                    <span className="material-symbols-outlined text-sm">error</span>
                    {promoError}
                  </p>
                )}
              </div>
              
              {/* Summary */}
              <div className="space-y-3 mb-6 pb-6 border-b border-gray-200">
                <div className="flex justify-between text-sage-green">
                  <span>Subtotal</span>
                  <span>${(subtotal || 0).toFixed(2)}</span>
                </div>
                
                {promoApplied && (
                  <div className="flex justify-between text-green-600">
                    <span>Discount</span>
                    <span>-${(discount || 0).toFixed(2)}</span>
                  </div>
                )}
                
                <div className="flex justify-between text-sage-green">
                  <span>Tax (8%)</span>
                  <span>${tax.toFixed(2)}</span>
                </div>
                
                <div className="flex justify-between text-sage-green">
                  <span>Shipping</span>
                  <span>${shipping.toFixed(2)}</span>
                </div>
              </div>
              
              <div className="flex justify-between mb-6">
                <span className="font-playfair text-xl font-bold text-deep-sage">
                  Total
                </span>
                <span className="font-playfair text-2xl font-bold text-deep-sage">
                  ${grandTotal.toFixed(2)}
                </span>
              </div>
              
              <button
                onClick={handleCheckout}
                disabled={isLoading}
                className="w-full bg-deep-sage text-white py-4 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed mb-4"
              >
                {isLoading ? 'Processing...' : 'Proceed to Checkout'}
              </button>
              
              <Link
                href="/products"
                className="block text-center text-sage-green hover:text-deep-sage transition-colors text-sm"
              >
                ← Continue Shopping
              </Link>
              
              {/* Trust Badges */}
              <div className="mt-6 pt-6 border-t border-gray-200 space-y-3">
                <div className="flex items-center gap-3 text-sm text-sage-green">
                  <span className="material-symbols-outlined">verified_user</span>
                  <span>Secure checkout</span>
                </div>
                <div className="flex items-center gap-3 text-sm text-sage-green">
                  <span className="material-symbols-outlined">local_shipping</span>
                  <span>Seller-determined shipping</span>
                </div>
                <div className="flex items-center gap-3 text-sm text-sage-green">
                  <span className="material-symbols-outlined">autorenew</span>
                  <span>30-day returns</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
      
      {/* Remove Item Confirmation Dialog */}
      <ConfirmDialog
        isOpen={!!removeConfirm}
        onClose={() => setRemoveConfirm(null)}
        onConfirm={() => handleRemoveItem(removeConfirm!)}
        title="Remove Item?"
        message="Are you sure you want to remove this item from your cart?"
        confirmText="Remove"
        cancelText="Cancel"
        variant="danger"
      />
      
      <Footer />
    </div>
  )
}
