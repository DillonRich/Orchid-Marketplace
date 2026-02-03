'use client'

import Link from 'next/link'
import { useState, useEffect } from 'react'
import CategoryDropdown from './CategoryDropdown'
import MobileMenuDrawer from './MobileMenuDrawer'
import { useAuthStore } from '@/lib/auth-store'
import { useCartStore } from '@/lib/cart-store'
import { apiClient } from '@/lib/api-client'

// Header component with 300px height and scaled elements
export default function Header() {
  const { isAuthenticated, user } = useAuthStore()
  const itemCount = useCartStore((state: any) => state.itemCount) || 0
  const [searchQuery, setSearchQuery] = useState('')
  const [unreadNotifications, setUnreadNotifications] = useState(0)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  // Debug: Log user role
  useEffect(() => {
    console.log('Header - User info:', { isAuthenticated, user, role: user?.role })
  }, [isAuthenticated, user])

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    if (searchQuery.trim()) {
      window.location.href = `/products?q=${encodeURIComponent(searchQuery)}`
    }
  }
  
  useEffect(() => {
    if (isAuthenticated) {
      fetchUnreadCount()
      // Poll for new notifications every 30 seconds
      const interval = setInterval(fetchUnreadCount, 30000)
      return () => clearInterval(interval)
    }
  }, [isAuthenticated])
  
  const fetchUnreadCount = async () => {
    try {
      const response = await apiClient.getUnreadNotificationCount()
      setUnreadNotifications(response.count || 0)
    } catch (error) {
      // Silently fail - notifications are not critical
    }
  }

  const cartItemCount = itemCount
  const [open, setOpen] = useState(false)

  return (
    <header className="w-full h-[135px] bg-warm-cream border-b border-primary-peach/30">
      <div className="h-full flex items-center w-full px-4 md:px-6 lg:px-8">
        
        {/* Mobile Menu Button - Hidden on desktop */}
        <button
          onClick={() => setMobileMenuOpen(true)}
          className="md:hidden h-[50px] w-[50px] flex items-center justify-center text-deep-sage hover:bg-soft-peach rounded-full transition-all duration-200 mr-4 flex-shrink-0"
          aria-label="Open Menu"
        >
          <span className="material-symbols-outlined text-[24px]">menu</span>
        </button>
        
        {/* Logo - Far Left */}
        <Link 
          href="/" 
          className="font-serif text-[42px] font-bold text-deep-sage hover:text-accent-peach transition-colors duration-200 flex-shrink-0"
        >
          Orchidillo
        </Link>

        {/* Center Area: Search Bar centered with Hamburger adjacent on right */}
        <div className="flex-1 flex items-center justify-center gap-5 px-8">
          {/* Search Bar - centered */}
          <form 
            onSubmit={handleSearch}
            className="w-[calc(40%+300px)] min-w-[600px] max-w-[900px]"
          >
            <div className="relative h-[69px] flex items-center">
              <input
                type="text"
                placeholder="Search for unique orchids..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full h-full px-8 pr-20 rounded-full border-2 border-sage-green bg-white text-deep-sage placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-sage-green focus:border-sage-green transition-all font-display text-lg"
                style={{ fontFamily: 'Plus Jakarta Sans, system-ui, sans-serif' }}
              />
              <button
                type="submit"
                className="absolute right-5 top-1/2 -translate-y-1/2 p-4 text-sage-green hover:text-deep-sage transition-colors"
                aria-label="Search"
              >
                <span className="material-symbols-outlined text-[27px]">search</span>
              </button>
            </div>
          </form>

          {/* Hamburger Categories Button - horizontal pill shape with dropdown */}
          <div className="relative">
            <button
              onClick={() => setOpen((s) => !s)}
              className="h-[50px] px-6 bg-soft-peach text-deep-sage rounded-full flex items-center gap-2 hover:bg-primary-peach transition-colors duration-200 flex-shrink-0 whitespace-nowrap"
              aria-label="Categories"
            >
              <span className="material-symbols-outlined text-[20px]">menu</span>
              <span className="font-medium text-sm font-sans">Categories</span>
            </button>
            {/* Dropdown injected here */}
            {open && (
              // dynamically import to keep header light if needed
              <CategoryDropdown open={open} onClose={() => setOpen(false)} />
            )}
          </div>
        </div>

        {/* Right Side: Icon Group */}
        <div className="flex items-center gap-[20px]">
          
          {/* Conditional Icons: Signed In vs Guest */}
          {isAuthenticated ? (
            // Signed In User Icons: Heart, Notifications, Shop (if seller), User, Cart as small pill buttons
            <div className="flex items-center gap-[15px]">
              {/* Heart Icon - Favorites */}
              <Link
                href="/account/favorites"
                className="h-[50px] w-[50px] flex items-center justify-center text-deep-sage hover:bg-soft-peach rounded-full transition-all duration-200"
                aria-label="Favorites"
              >
                <span className="material-symbols-outlined text-[24px]">favorite</span>
              </Link>

              {/* Notification Bell Icon with Badge */}
              <Link
                href="/notifications"
                className="relative h-[50px] w-[50px] flex items-center justify-center text-deep-sage hover:bg-soft-peach rounded-full transition-all duration-200"
                aria-label="Notifications"
              >
                <span className="material-symbols-outlined text-[24px]">notifications</span>
                {unreadNotifications > 0 && (
                  <span className="absolute top-[8px] right-[8px] w-5 h-5 bg-accent-peach text-white text-xs font-bold rounded-full flex items-center justify-center">
                    {unreadNotifications > 9 ? '9+' : unreadNotifications}
                  </span>
                )}
              </Link>

              {/* Shop Icon - Only for Sellers */}
              {/* TEMPORARILY SHOWING FOR ALL USERS - Remove condition after backend role fix */}
              {isAuthenticated && (
                <Link
                  href="/seller/dashboard"
                  className="h-[50px] w-[50px] flex items-center justify-center text-deep-sage hover:bg-soft-peach rounded-full transition-all duration-200"
                  aria-label="Seller Dashboard"
                  title="Seller Dashboard"
                >
                  <span className="material-symbols-outlined text-[24px]">storefront</span>
                </Link>
              )}
              {/* TODO: Re-enable role check after backend fix:
              {(user?.role === 'SELLER' || user?.role === 'ADMIN') && (
              */}

              {/* User Profile Icon */}
              <Link
                href="/account"
                className="h-[50px] w-[50px] flex items-center justify-center text-deep-sage hover:bg-soft-peach rounded-full transition-all duration-200"
                aria-label="My Account"
              >
                <span className="material-symbols-outlined text-[24px]">account_circle</span>
              </Link>

              {/* Shopping Cart Icon with Badge */}
              <Link
                href="/cart"
                className="relative h-[50px] w-[50px] flex items-center justify-center text-deep-sage hover:bg-soft-peach rounded-full transition-all duration-200"
                aria-label="Shopping Cart"
              >
                <span className="material-symbols-outlined text-[24px]">shopping_cart</span>
                {cartItemCount > 0 && (
                  <span className="absolute top-[8px] right-[8px] w-5 h-5 bg-accent-peach text-white text-xs font-bold rounded-full flex items-center justify-center">
                    {cartItemCount > 9 ? '9+' : cartItemCount}
                  </span>
                )}
              </Link>
            </div>
          ) : (
            // Guest View: Sign In + Cart as pill buttons
            <div className="flex items-center gap-[15px]">
              {/* Sign In Button */}
              <Link
                href="/login"
                className="h-[50px] px-6 bg-soft-peach text-deep-sage rounded-full flex items-center justify-center font-medium text-sm hover:bg-primary-peach transition-colors duration-200 flex-shrink-0 font-sans whitespace-nowrap"
              >
                Sign In
              </Link>

              {/* Shopping Cart Icon with Badge */}
              <Link
                href="/cart"
                className="relative h-[50px] w-[50px] flex items-center justify-center text-deep-sage hover:bg-soft-peach rounded-full transition-all duration-200"
                aria-label="Shopping Cart"
              >
                <span className="material-symbols-outlined text-[24px]">shopping_cart</span>
                {cartItemCount > 0 && (
                  <span className="absolute top-[8px] right-[8px] w-5 h-5 bg-accent-peach text-white text-xs font-bold rounded-full flex items-center justify-center">
                    {cartItemCount > 9 ? '9+' : cartItemCount}
                  </span>
                )}
              </Link>
            </div>
          )}
        </div>
      </div>
      
      {/* Mobile Menu Drawer */}
      <MobileMenuDrawer
        isOpen={mobileMenuOpen}
        onClose={() => setMobileMenuOpen(false)}
        isAuthenticated={isAuthenticated}
        userRole={user?.role as 'customer' | 'seller' | 'admin'}
      />
    </header>
  )
}
