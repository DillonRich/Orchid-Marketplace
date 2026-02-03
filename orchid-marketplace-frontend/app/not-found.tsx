import Link from 'next/link'
import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function NotFound() {
  return (
    <>
      <Header />
      <div className="min-h-[60vh] bg-warm-cream flex items-center justify-center px-4 py-20">
        <div className="max-w-2xl w-full bg-white rounded-2xl shadow-lg p-8 md:p-12 text-center">
          <div className="mb-6">
            <span className="material-symbols-outlined text-sage-green text-8xl">
              search_off
            </span>
          </div>
          <h1 className="font-playfair text-6xl font-bold text-deep-sage mb-4">
            404
          </h1>
          <h2 className="font-playfair text-2xl font-semibold text-deep-sage mb-4">
            Page Not Found
          </h2>
          <p className="text-sage-green text-lg mb-2">
            The page you're looking for doesn't exist or has been moved.
          </p>
          <p className="text-sage-green mb-8">
            Let's get you back on track with our beautiful orchids!
          </p>
          
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              href="/"
              className="px-8 py-3 bg-accent-peach text-white rounded-full font-medium hover:opacity-90 transition-opacity inline-flex items-center justify-center gap-2"
            >
              <span className="material-symbols-outlined">home</span>
              Go to Homepage
            </Link>
            <Link
              href="/products"
              className="px-8 py-3 bg-white border-2 border-sage-green text-sage-green rounded-full font-medium hover:bg-sage-green hover:text-white transition-colors inline-flex items-center justify-center gap-2"
            >
              <span className="material-symbols-outlined">storefront</span>
              Browse Products
            </Link>
          </div>
          
          <div className="mt-12 pt-8 border-t border-gray-200">
            <p className="text-sm text-sage-green mb-4">Popular pages:</p>
            <div className="flex flex-wrap gap-3 justify-center">
              <Link href="/about" className="text-sm text-deep-sage hover:text-accent-peach underline">
                About Us
              </Link>
              <Link href="/care-guides" className="text-sm text-deep-sage hover:text-accent-peach underline">
                Care Guides
              </Link>
              <Link href="/shipping" className="text-sm text-deep-sage hover:text-accent-peach underline">
                Shipping Info
              </Link>
              <Link href="/account" className="text-sm text-deep-sage hover:text-accent-peach underline">
                My Account
              </Link>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
