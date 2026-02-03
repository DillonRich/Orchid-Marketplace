'use client'

import Header from '@/components/Header'
import ProductCard from '@/components/ProductCard'
import Link from 'next/link'
import Image from 'next/image'
import { useState } from 'react'

export default function Page() {
  const [newsletterEmail, setNewsletterEmail] = useState('')
  const [showThankYou, setShowThankYou] = useState(false)

  const handleNewsletterSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (newsletterEmail.trim()) {
      setShowThankYou(true)
      setNewsletterEmail('')
      // Hide popup after 3 seconds
      setTimeout(() => setShowThankYou(false), 3000)
    }
  }

  const orchidTypes = [
    { name: 'Phalaenopsis', image: '/images/orchid1.jpg' },
    { name: 'Cattleya', image: '/images/Orchid2.jpg' },
    { name: 'Vanda', image: '/images/Orchid3.jpg' },
    { name: 'Dendrobium', image: '/images/orchid4.jpg' },
    { name: 'Oncidium', image: '/images/orchid5.jpg' },
    { name: 'Cymbidium', image: '/images/orchid1.jpg' },
  ]

  const products = [
    {
      id: 'sunset-phalaenopsis-premium-bloom',
      name: 'Sunset Phalaenopsis Premium Bloom',
      sellerName: 'Lush Valley Nurseries',
      rating: 4.9,
      reviews: 156,
      price: 38.00,
      image: '/images/orchid1.jpg'
    },
    {
      id: 'fragrant-cattleya-orchid-hybrid',
      name: 'Fragrant Cattleya Orchid Hybrid',
      sellerName: 'Tropical Blooms Co.',
      rating: 4.8,
      reviews: 82,
      price: 54.00,
      image: '/images/Orchid2.jpg'
    },
    {
      id: 'deep-purple-vanda-special-edition',
      name: 'Deep Purple Vanda Special Edition',
      sellerName: 'Island Orchids',
      rating: 5.0,
      reviews: 34,
      price: 62.00,
      image: '/images/Orchid3.jpg'
    },
    {
      id: 'mini-cymbidium-orchid-plant',
      name: 'Mini Cymbidium Orchid Plant',
      sellerName: 'Mountain Flora',
      rating: 4.7,
      reviews: 91,
      price: 45.00,
      image: '/images/orchid4.jpg'
    },
  ]

  const faqItems = [
    {
      question: 'How often should I water my orchid?',
      answer: 'Water approximately once a week. The best indicator is when the bark or moss feels dry to the touch. Orchids prefer to dry out slightly between waterings.'
    },
    {
      question: 'What type of light do orchids need?',
      answer: 'Orchids generally prefer bright, indirect light. A north or east-facing window is ideal. Avoid direct afternoon sun, which can burn the leaves.'
    },
    {
      question: 'How long do orchids bloom?',
      answer: 'Most orchid blooms last 6-8 weeks. With proper care, many varieties can rebloom multiple times per year.'
    },
    {
      question: 'Can I grow orchids without a greenhouse?',
      answer: 'Absolutely! Most orchids are easy to grow indoors. Simply provide bright, indirect light, proper watering, and moderate humidity.'
    },
  ]

  return (
    <div className="min-h-screen bg-soft-peach">
      <Header />

      <main className="flex-1">
        {/* Hero Section */}
        <section className="px-4 md:px-0 max-w-[1280px] mx-auto py-6" style={{ marginTop: '0px' }}>
          <div className="hero-gradient rounded-[2rem] overflow-hidden min-h-[480px] flex flex-col justify-center px-8 md:px-20 relative border border-primary-peach/20 shadow-sm" 
               style={{ 
                 background: 'linear-gradient(rgba(255, 249, 243, 0.2), rgba(255, 249, 243, 0.4)), url(https://images.unsplash.com/photo-1596720426673-e4e14290f0cc?q=80&w=2940&auto=format&fit=crop)',
                 backgroundSize: 'cover',
                 backgroundPosition: 'center'
               }}>
            <div className="max-w-xl flex flex-col gap-6 relative z-10 rounded-xl p-6" style={{ boxShadow: '0 0 40px rgba(255, 182, 193, 0.3)' }}>
              <span className="inline-block px-4 py-1.5 bg-sage-green text-white font-bold tracking-widest uppercase text-[10px] rounded-full w-fit">
                Direct from Nurseries
              </span>
              <h1 className="text-deep-sage text-4xl md:text-6xl font-serif leading-[1.1]">
                Exotic <span className="text-accent-peach italic">Blooms</span> delivered to your door.
              </h1>
              <p className="text-deep-sage/80 text-lg leading-relaxed font-medium">
                Connect with master orchid growers across the country. Find rare species, vibrant hybrids, and everything you need for a healthy bloom.
              </p>
              <div className="flex gap-4">
                <Link href="/products" className="h-12 px-8 bg-deep-sage text-warm-cream font-bold rounded-full shadow-lg hover:bg-sage-green transition-all transform hover:-translate-y-0.5 flex items-center justify-center">
                  Browse Marketplace
                </Link>
                <Link href="/explore" className="h-12 px-8 bg-white/80 backdrop-blur-sm text-deep-sage border border-primary-peach/30 font-bold rounded-full hover:bg-white transition-all shadow-sm flex items-center justify-center">
                  Explore Species
                </Link>
              </div>
            </div>
          </div>
        </section>

        {/* Shop by Orchid Type Section */}
        <section className="px-4 md:px-0 max-w-[1280px] mx-auto py-9" style={{ marginTop: '56px' }}>
          <div className="flex items-center justify-between mb-8">
            <h2 className="text-2xl font-serif text-deep-sage">Shop by Orchid Type</h2>
            <Link href="/products" className="text-sage-green font-bold text-sm hover:scale-110 transition-transform flex items-center gap-1">
              View all species <span className="material-symbols-outlined text-lg">arrow_forward</span>
            </Link>
          </div>
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-6">
            {orchidTypes.map((type) => (
              <Link key={type.name} href={`/products?type=${type.name.toLowerCase()}`} className="group flex flex-col items-center gap-4">
                <div className="w-full aspect-square rounded-2xl bg-primary-peach/20 border border-primary-peach/30 overflow-hidden group-hover:shadow-md transition-all">
                  <div className="w-full h-full relative transition-transform duration-500 group-hover:scale-110">
                    <Image 
                      src={type.image} 
                      alt={type.name}
                      fill
                      className="object-cover"
                    />
                  </div>
                </div>
                <span className="font-bold text-deep-sage group-hover:text-accent-peach transition-colors">{type.name}</span>
              </Link>
            ))}
          </div>
        </section>

        {/* Trending Products Section */}
        <section className="max-w-full py-9" style={{ marginTop: '56px' }}>
          <div className="px-4 md:px-0 max-w-[1280px] mx-auto">
            <div className="flex items-end justify-between mb-8 border-b border-primary-peach/20 pb-4">
              <div>
                <h2 className="text-3xl font-serif text-deep-sage">Trending Orchids</h2>
                <p className="text-sage-green font-medium mt-1">Most loved by our community this week.</p>
              </div>
              <Link href="/products" className="text-sage-green font-bold text-sm hover:scale-110 transition-transform flex items-center gap-1">
                View More <span className="material-symbols-outlined text-lg">arrow_forward</span>
              </Link>
            </div>
          </div>
          
          {/* Trending Products Section */}
          <div className="px-4 md:px-0 max-w-[1280px] mx-auto">
            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
              {[...products, ...products, ...products, ...products, ...products].slice(0, 20).map((product, index) => (
                <ProductCard key={`${product.id}-${index}`} product={product} />
              ))}
            </div>
          </div>
        </section>

        {/* Newsletter Section */}
        <section className="px-4 md:px-0 max-w-[1280px] mx-auto py-9" style={{ marginTop: '70px' }}>
          <div className="bg-primary-peach/20 border border-primary-peach/30 rounded-[2.5rem] p-8 md:p-12 text-center flex flex-col items-center gap-4">
            <h2 className="text-3xl font-serif text-deep-sage">Join the Orchidillo Club</h2>
            <p className="text-sage-green font-medium max-w-xl text-base">
              Care tips from experts, exclusive offers, and info about rare flowers.
            </p>
            <form onSubmit={handleNewsletterSubmit} className="flex flex-col sm:flex-row w-full max-w-md gap-3 mt-2">
              <input 
                type="email"
                value={newsletterEmail}
                onChange={(e) => setNewsletterEmail(e.target.value)}
                className="flex-1 rounded-full border-primary-peach/50 bg-white px-6 h-12 focus:ring-sage-green focus:border-sage-green shadow-sm text-sm text-deep-sage placeholder:text-sage-green/50" 
                placeholder="Enter your email address" 
                required
              />
              <button type="submit" className="h-12 px-8 bg-deep-sage text-warm-cream font-bold rounded-full hover:bg-sage-green shadow-md transition-all text-sm">
                Subscribe
              </button>
            </form>
          </div>
        </section>

        {/* FAQ Section */}
        <section className="px-4 md:px-0 max-w-[1280px] mx-auto py-12" style={{ marginTop: '70px' }}>
          <h2 className="mb-8 text-2xl font-bold font-serif text-deep-sage">Frequently Asked Questions</h2>
          <div className="space-y-4">
            {faqItems.map((item, index) => (
              <details key={index} className="group border-b border-primary-peach/30 pb-4">
                <summary className="flex justify-between items-center cursor-pointer text-deep-sage font-medium hover:text-accent-peach transition-colors duration-200 list-none">
                  <span>{item.question}</span>
                  <span className="material-symbols-outlined text-sage-green transition-transform duration-200 group-open:rotate-180">
                    expand_more
                  </span>
                </summary>
                <p className="mt-4 text-sage-green">{item.answer}</p>
              </details>
            ))}
          </div>
        </section>

        {/* Footer Section */}
        <footer className="bg-warm-cream border-t border-primary-peach/20 pt-12 pb-8" style={{ marginTop: '70px' }}>
          <div className="max-w-[1280px] mx-auto px-4 md:px-0 flex gap-32">
            <div className="flex-shrink-0">
              <div className="flex items-center gap-2 text-deep-sage mb-6">
                <h2 className="text-xl font-bold font-serif">Orchidillo</h2>
              </div>
              <p className="text-sage-green font-medium max-w-[280px] leading-relaxed mb-6">
                The premier curated marketplace for orchid enthusiasts. We bridge the gap between nurseries, trait breeders, and passionate collectors across the US.
              </p>
            </div>
            
            <div className="flex-1 flex gap-24">
              <div>
                <h3 className="font-bold text-deep-sage mb-6 uppercase text-xs tracking-widest">Shop All</h3>
                <ul className="flex flex-col gap-4 text-sm text-sage-green font-medium">
                  <li><Link href="/products?sort=trending" className="hover:text-accent-peach transition-colors">Trending Now</Link></li>
                  <li><Link href="/products?sort=new" className="hover:text-accent-peach transition-colors">New Arrivals</Link></li>
                  <li><Link href="/products?category=rare" className="hover:text-accent-peach transition-colors">Rare & Exotic</Link></li>
                  <li><Link href="/products?category=supplies" className="hover:text-accent-peach transition-colors">Orchid Supplies</Link></li>
                  <li><Link href="/products?category=gifts" className="hover:text-accent-peach transition-colors">Gift Bundles</Link></li>
                </ul>
              </div>
              
              <div>
                <h3 className="font-bold text-deep-sage mb-6 uppercase text-xs tracking-widest">Help & Info</h3>
                <ul className="flex flex-col gap-4 text-sm text-sage-green font-medium">
                  <li><Link href="/shipping" className="hover:text-accent-peach transition-colors">Shipping Policy</Link></li>
                  <li><Link href="/returns" className="hover:text-accent-peach transition-colors">Returns & Refunds</Link></li>
                  <li><Link href="/faq" className="hover:text-accent-peach transition-colors">Nursery Partner FAQ</Link></li>
                  <li><Link href="/care-guides" className="hover:text-accent-peach transition-colors">Care Guides</Link></li>
                </ul>
              </div>
              
              <div>
                <h3 className="font-bold text-deep-sage mb-6 uppercase text-xs tracking-widest">The Company</h3>
                <ul className="flex flex-col gap-4 text-sm text-sage-green font-medium">
                  <li><Link href="/about" className="hover:text-accent-peach transition-colors">Our Story</Link></li>
                  <li><Link href="/terms" className="hover:text-accent-peach transition-colors">Terms of Service</Link></li>
                  <li><Link href="/privacy" className="hover:text-accent-peach transition-colors">Privacy Policy</Link></li>
                  <li><Link href="/contact" className="hover:text-accent-peach transition-colors">Contact Support</Link></li>
                </ul>
              </div>
            </div>
          </div>
          
          <div className="max-w-[1280px] mx-auto px-4 md:px-0 mt-16 pt-8 border-t border-primary-peach/10 flex flex-col md:flex-row justify-between items-center gap-6">
            <p className="text-xs text-deep-sage font-medium">© 2026 Orchidillo Marketplace. All rights reserved.</p>
            <div className="flex gap-6 text-xs text-sage-green font-medium">
              <Link href="/cookies" className="hover:text-accent-peach hover:underline transition-colors">Cookie Settings</Link>
              <Link href="/privacy" className="hover:text-accent-peach hover:underline transition-colors">Privacy Policy</Link>
              <Link href="/terms" className="hover:text-accent-peach hover:underline transition-colors">Terms of Service</Link>
            </div>
          </div>
        </footer>
      </main>

      {/* Newsletter Thank You Popup */}
      {showThankYou && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50" onClick={() => setShowThankYou(false)}>
          <div className="bg-accent-peach rounded-2xl p-8 max-w-md mx-4 relative animate-in fade-in zoom-in duration-300">
            <button 
              onClick={() => setShowThankYou(false)}
              className="absolute top-4 right-4 text-sage-green hover:text-deep-sage"
            >
              <span className="material-symbols-outlined">close</span>
            </button>
            <h3 className="text-2xl font-serif text-deep-sage mb-4">Thank You!</h3>
            <p className="text-sage-green">Thank you for subscribing to the Orchidillo Newsletter! Check your inbox for exclusive orchid care tips and offers.</p>
          </div>
        </div>
      )}
    </div>
  )
}
