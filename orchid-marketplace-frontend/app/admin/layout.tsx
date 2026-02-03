'use client'

import { ReactNode, useEffect } from 'react'
import { useRouter, usePathname } from 'next/navigation'
import Link from 'next/link'
import { useAuthStore } from '@/lib/auth-store'
import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function AdminLayout({ children }: { children: ReactNode }) {
  const router = useRouter()
  const pathname = usePathname()
  const { user, isAuthenticated } = useAuthStore()
  
  useEffect(() => {
    if (!isAuthenticated) {
      router.push(`/login?returnUrl=${encodeURIComponent(pathname)}`)
      return
    }
    
    // Check if user has ADMIN role
    if (user?.role !== 'ADMIN') {
      router.push('/account') // Redirect to customer account if not admin
    }
  }, [isAuthenticated, user, router, pathname])
  
  if (!isAuthenticated || user?.role !== 'ADMIN') {
    return null
  }
  
  const menuItems = [
    { icon: 'dashboard', label: 'Dashboard', href: '/admin' },
    { icon: 'people', label: 'Users', href: '/admin/users' },
    { icon: 'category', label: 'Categories', href: '/admin/categories' },
    { icon: 'inventory_2', label: 'Products', href: '/admin/products' },
    { icon: 'shopping_cart', label: 'Orders', href: '/admin/orders' },
    { icon: 'analytics', label: 'Analytics', href: '/admin/analytics' },
    { icon: 'settings', label: 'Settings', href: '/admin/settings' },
  ]
  
  const isActive = (href: string) => {
    if (href === '/admin') {
      return pathname === '/admin'
    }
    return pathname.startsWith(href)
  }
  
  return (
    <>
      <Header />
      
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1400px] mx-auto px-8 lg:px-32 py-12">
          <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
            {/* Sidebar */}
            <aside className="lg:col-span-1">
              <div className="bg-white rounded-xl p-6 sticky top-6">
                {/* Admin Info */}
                <div className="mb-6 pb-6 border-b border-gray-200">
                  <div className="flex items-center gap-3 mb-4">
                    <div className="w-16 h-16 rounded-full bg-gradient-to-br from-deep-sage to-sage-green flex items-center justify-center">
                      <span className="material-symbols-outlined text-3xl text-white">
                        shield
                      </span>
                    </div>
                    <div className="flex-1 min-w-0">
                      <h3 className="font-medium text-deep-sage truncate">
                        Admin Panel
                      </h3>
                      <p className="text-sm text-sage-green truncate">
                        {user?.email}
                      </p>
                    </div>
                  </div>
                </div>
                
                {/* Navigation */}
                <nav className="space-y-1">
                  {menuItems.map(item => (
                    <Link
                      key={item.href}
                      href={item.href}
                      className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-all ${
                        isActive(item.href)
                          ? 'bg-soft-peach text-deep-sage'
                          : 'text-sage-green hover:bg-soft-peach hover:text-deep-sage'
                      }`}
                    >
                      <span className="material-symbols-outlined">
                        {item.icon}
                      </span>
                      <span className="font-medium">{item.label}</span>
                    </Link>
                  ))}
                </nav>
                
                {/* Back to Site */}
                <div className="mt-6 pt-6 border-t border-gray-200">
                  <Link
                    href="/"
                    className="flex items-center gap-3 px-4 py-3 rounded-lg text-sage-green hover:bg-soft-peach hover:text-deep-sage transition-all"
                  >
                    <span className="material-symbols-outlined">
                      home
                    </span>
                    <span className="font-medium">Back to Site</span>
                  </Link>
                </div>
              </div>
            </aside>
            
            {/* Main Content */}
            <main className="lg:col-span-3">
              {children}
            </main>
          </div>
        </div>
      </div>
      
      <Footer />
    </>
  )
}
