'use client'

import { ReactNode, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import Link from 'next/link'
import { usePathname } from 'next/navigation'
import Header from './Header'
import Footer from './Footer'
import { useAuthStore } from '@/lib/auth-store'

interface AccountLayoutProps {
  children: ReactNode
}

export default function AccountLayout({ children }: AccountLayoutProps) {
  const router = useRouter()
  const pathname = usePathname()
  const { isAuthenticated, user, logout } = useAuthStore()
  
  useEffect(() => {
    if (!isAuthenticated) {
      router.push('/login?redirect=' + encodeURIComponent(pathname))
    }
  }, [isAuthenticated, router, pathname])
  
  if (!isAuthenticated) return null
  
  const navItems = [
    { href: '/account', icon: 'dashboard', label: 'Dashboard' },
    { href: '/account/profile', icon: 'person', label: 'Profile' },
    { href: '/account/orders', icon: 'shopping_bag', label: 'Orders' },
    { href: '/account/addresses', icon: 'location_on', label: 'Addresses' },
    { href: '/account/payment-methods', icon: 'credit_card', label: 'Payment Methods' },
    { href: '/wishlist', icon: 'favorite', label: 'Wishlist' },
    { href: '/account/messages', icon: 'mail', label: 'Messages' },
    { href: '/account/notifications', icon: 'notifications', label: 'Notifications' },
    { href: '/account/settings', icon: 'settings', label: 'Settings' },
  ]
  
  const handleLogout = () => {
    logout()
    router.push('/')
  }
  
  return (
    <div className="min-h-screen bg-warm-cream flex flex-col">
      <Header />
      
      <main className="flex-1 container mx-auto px-8 lg:px-32 py-16">
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Sidebar */}
          <aside className="lg:col-span-1">
            <div className="bg-white rounded-xl p-6 sticky top-4">
              <div className="flex flex-col items-center mb-6 pb-6 border-b border-gray-200">
                <div className="w-20 h-20 rounded-full bg-soft-peach flex items-center justify-center mb-3">
                  <span className="material-symbols-outlined text-4xl text-deep-sage">
                    person
                  </span>
                </div>
                <h2 className="font-medium text-deep-sage text-lg text-center">
                  {user?.fullName || user?.email}
                </h2>
                <p className="text-sm text-sage-green text-center">{user?.email}</p>
              </div>
              
              <nav className="space-y-2">
                {navItems.map(item => {
                  const isActive = pathname === item.href
                  return (
                    <Link
                      key={item.href}
                      href={item.href}
                      className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-all ${
                        isActive
                          ? 'bg-soft-peach text-deep-sage font-medium'
                          : 'text-sage-green hover:bg-soft-peach hover:text-deep-sage'
                      }`}
                    >
                      <span className="material-symbols-outlined">{item.icon}</span>
                      {item.label}
                    </Link>
                  )
                })}
              </nav>
              
              <button
                onClick={handleLogout}
                className="w-full mt-6 flex items-center justify-center gap-3 px-4 py-3 rounded-lg text-red-500 hover:bg-red-50 transition-all"
              >
                <span className="material-symbols-outlined">logout</span>
                Logout
              </button>
            </div>
          </aside>
          
          {/* Main Content */}
          <div className="lg:col-span-3">
            {children}
          </div>
        </div>
      </main>
      
      <Footer />
    </div>
  )
}
