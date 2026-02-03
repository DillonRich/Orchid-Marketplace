"use client"

import React, { useEffect, useRef } from 'react'
import Link from 'next/link'
import { useCartStore } from '@/lib/cart-store'

export default function CartSlide() {
  const slideOpen = useCartStore((s: any) => s.slideOpen)
  const items = useCartStore((s: any) => s.items)
  const close = useCartStore((s: any) => s.closeSlide)
  const removeItem = useCartStore((s: any) => s.removeItem)
  const totalPrice = useCartStore((s: any) => s.totalPrice)

  const ref = useRef<HTMLDivElement | null>(null)

  useEffect(() => {
    function onDoc(e: MouseEvent) {
      if (!slideOpen) return
      if (ref.current && !ref.current.contains(e.target as Node)) close()
    }
    document.addEventListener('mousedown', onDoc)
    return () => document.removeEventListener('mousedown', onDoc)
  }, [slideOpen, close])

  if (!slideOpen) return null

  return (
    <>
      <div className="fixed inset-0 bg-black/15 z-40" aria-hidden onClick={close} />

      <aside
        ref={ref}
        className="fixed right-0 top-0 h-full bg-white shadow-xl z-50 transform transition-transform duration-300 flex flex-col"
        style={{ width: window.innerWidth < 768 ? '100%' : `${Math.max(320, Math.floor(window.innerWidth * 0.2))}px` }}
        role="dialog"
        aria-label="Cart Slide"
      >
        {/* Header */}
        <div className="p-4 flex items-center justify-between border-b border-primary-peach/20 flex-shrink-0">
          <h3 className="text-lg font-serif text-deep-sage">{items.length} Item{items.length !== 1 ? 's' : ''} Added to Your Cart</h3>
          <button onClick={close} aria-label="Close" className="p-2 rounded-full hover:bg-soft-peach">
            <span className="material-symbols-outlined">close</span>
          </button>
        </div>

        {/* Body (scrollable) */}
        <div className="p-4 overflow-auto flex-1">
          {items.length === 0 && <div className="text-sm text-gray-500">No items in your cart.</div>}
          <ul className="space-y-4">
            {items.map((it: any) => (
              <li key={it.id} className="flex items-center gap-3 p-2 rounded-lg border border-primary-peach/10 group">
                <div className="w-16 h-16 bg-gray-100 rounded-md flex items-center justify-center overflow-hidden">
                  {it.imageUrl ? <img src={it.imageUrl} alt={it.title} className="w-full h-full object-cover" /> : <span className="text-gray-400">IMG</span>}
                </div>
                <div className="flex-1">
                  <div className="text-sm text-deep-sage font-medium line-clamp-2">{it.title}</div>
                  <div className="text-xs text-gray-500 mt-1">Qty: {it.quantity} • ${Number(it.price * it.quantity).toFixed(2)}</div>
                </div>
                <div className="flex flex-col items-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button onClick={() => removeItem(it.id)} className="w-9 h-9 bg-white p-2 rounded-full shadow-sm flex items-center justify-center hover:bg-primary-peach/10">
                    <span className="material-symbols-outlined text-deep-sage">delete</span>
                  </button>
                </div>
              </li>
            ))}
          </ul>
        </div>

        {/* Bottom fixed section: subtotal + two stacked buttons */}
        <div className="p-4 border-t border-primary-peach/20 bg-white flex-shrink-0" style={{ paddingBottom: 'calc(env(safe-area-inset-bottom, 16px) + 12px)' }}>
          <div className="mb-3">
            <div className="flex items-center gap-2">
              <div className="text-sm text-gray-600">Subtotal</div>
              <div className="text-base font-bold text-deep-sage">${totalPrice().toFixed(2)}</div>
            </div>
          </div>

          <div className="flex flex-col gap-3">
            <Link href="/cart" className="w-full bg-sage-green text-white py-3 rounded-full flex items-center justify-center hover:opacity-95 transition-colors">
              View Cart
            </Link>

            <Link href="/checkout" className="w-full bg-deep-sage text-white py-3 rounded-full flex items-center justify-center hover:opacity-95 transition-colors">
              Checkout
            </Link>
          </div>
        </div>
      </aside>
    </>
  )
}
