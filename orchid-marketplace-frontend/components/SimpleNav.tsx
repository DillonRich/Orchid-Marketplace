'use client'

import Link from 'next/link'

export default function SimpleNav() {
  return (
    <nav className="border-b border-warm-cream bg-white sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-6 py-4">
        <Link href="/" className="text-2xl font-bold text-deep-sage hover:text-sage-green transition-colors">
          🌸 Orchidillo
        </Link>
      </div>
    </nav>
  )
}
