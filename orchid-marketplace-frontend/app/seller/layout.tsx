'use client'

import { ReactNode, useEffect } from 'react'
import { useRouter, usePathname } from 'next/navigation'
import Link from 'next/link'
import { useAuthStore } from '@/lib/auth-store'
import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function SellerLayout({ children }: { children: ReactNode }) {
  const router = useRouter()
  const pathname = usePathname()
  const { user, isAuthenticated } = useAuthStore()
  
  useEffect(() => {
    console.log('Seller Layout - User check:', { isAuthenticated, user, role: user?.role })
    
    if (!isAuthenticated) {
      router.push(`/login?returnUrl=${encodeURIComponent(pathname)}`)
      return
    }
    
    // TEMPORARILY DISABLED - Allow all authenticated users to see seller dashboard for testing
    // TODO: Re-enable this check after testing
    /*
    if (user?.role !== 'SELLER' && user?.role !== 'ADMIN') {
      console.log('Seller Layout - Redirecting to /account, user is not a seller')
      router.push('/account')
    }
    */
  }, [isAuthenticated, user, router, pathname])
  
  // TEMPORARILY DISABLED - Allow all users for testing
  // TODO: Re-enable after testing
  /*
  if (!isAuthenticated || (user?.role !== 'SELLER' && user?.role !== 'ADMIN')) {
    return null
  }
  */
  
  if (!isAuthenticated) {
    return null
  }
  
  const menuItems = [
    { icon: 'dashboard', label: 'Dashboard', href: '/seller/dashboard' },
    { icon: 'inventory_2', label: 'Products', href: '/seller/products' },
    { icon: 'local_shipping', label: 'Orders & Deliveries', href: '/seller/orders' },
    { icon: 'mail', label: 'Messages', href: '/seller/messages' },
    { icon: 'analytics', label: 'Stats', href: '/seller/stats' },
    { icon: 'account_balance', label: 'Finances', href: '/seller/finances' },
    { icon: 'settings', label: 'Shop Settings', href: '/seller/settings' },
  ]
  
  const isActive = (href: string) => {
    if (href === '/seller') {
      return pathname === '/seller'
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
                {/* Seller Info */}
                <div className="mb-6 pb-6 border-b border-gray-200">
                  <div className="flex items-center gap-3 mb-4">
                    <div className="w-16 h-16 rounded-full bg-soft-peach flex items-center justify-center">
                      <span className="material-symbols-outlined text-3xl text-deep-sage">
                        storefront
                      </span>
                    </div>
                    <div className="flex-1 min-w-0">
                      <h3 className="font-medium text-deep-sage truncate">
                        {user?.fullName || 'Seller'}
                      </h3>
                      <p className="text-sm text-sage-green truncate">
                        {user?.email}
                      </p>
                    </div>
                  </div>
                  <Link
                    href="/store/my-store"
                    className="text-sm text-accent-peach hover:text-deep-sage flex items-center gap-1"
                  >
                    <span className="material-symbols-outlined text-sm">open_in_new</span>
                    View My Store
                  </Link>
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
                
                {/* Back to Shopping */}
                <div className="mt-6 pt-6 border-t border-gray-200">
                  <Link
                    href="/"
                    className="flex items-center gap-3 px-4 py-3 rounded-lg text-sage-green hover:bg-soft-peach hover:text-deep-sage transition-all"
                  >
                    <span className="material-symbols-outlined">
                      storefront
                    </span>
                    <span className="font-medium">Back to Shopping</span>
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
