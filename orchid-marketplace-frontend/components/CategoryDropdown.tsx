"use client"

import React, { useEffect, useLayoutEffect, useRef, useState } from 'react'
import Link from 'next/link'

type Subcategory = { id: string; name: string; image?: string }
type Category = { id: string; name: string; subs: Subcategory[] }

const CATEGORIES: Category[] = [
  {
    id: 'orchid-types',
    name: 'Orchid Categories (by Type)',
    subs: [
      { id: 'phalaenopsis', name: 'Phalaenopsis (Moth Orchid)' },
      { id: 'dendrobium', name: 'Dendrobium' },
      { id: 'oncidium', name: 'Oncidium (Dancing Lady)' },
      { id: 'cattleya', name: 'Cattleya' },
      { id: 'paphiopedilum', name: "Paphiopedilum (Lady's Slipper Orchid)" },
    ],
  },
  {
    id: 'specialist',
    name: 'Specialist & Enthusiast',
    subs: [
      { id: 'vanda', name: 'Vanda' },
      { id: 'cymbidium', name: 'Cymbidium' },
      { id: 'miltoniopsis', name: 'Miltoniopsis (Pansy Orchid)' },
      { id: 'brassia', name: 'Brassia (Spider Orchid)' },
    ],
  },
  {
    id: 'miniature',
    name: 'Miniature & Small-Grow',
    subs: [
      { id: 'mini-phal', name: 'Mini Phalaenopsis' },
      { id: 'mini-catt', name: 'Mini Cattleya' },
      { id: 'pleurothallis', name: 'Pleurothallis' },
    ],
  },
  {
    id: 'hybrids',
    name: 'Hybrids & Collections',
    subs: [
      { id: 'intergeneric', name: 'Intergeneric Hybrids' },
      { id: 'novelty', name: 'Novelty & Art-Shape Blooms' },
      { id: 'collector-pack', name: "Assorted Orchid Collector's Pack" },
    ],
  },
  {
    id: 'growers',
    name: "Grower's Corner",
    subs: [
      { id: 'keiki', name: 'Keiki (Baby Orchid Plants)' },
      { id: 'seedlings', name: 'Orchid Seedlings & Flasks' },
      { id: 'bare-root', name: 'Bare-Root Orchids' },
    ],
  },
  {
    id: 'supplies',
    name: 'Supplies',
    subs: [
      { id: 'pots', name: 'Pots & Containers' },
      { id: 'media', name: 'Potting Media' },
      { id: 'care', name: 'Care Essentials' },
      { id: 'growing', name: 'Growing & Display' },
    ],
  },
]

export default function CategoryDropdown({ open, onClose }: { open: boolean; onClose: () => void }) {
  const ref = useRef<HTMLDivElement | null>(null)
  const [active, setActive] = useState<string | null>(CATEGORIES[0].id)
  const [isMobile, setIsMobile] = useState(false)
  const [expanded, setExpanded] = useState<Record<string, boolean>>({})
  const [leftPx, setLeftPx] = useState<number>(0)
  const [topPx, setTopPx] = useState<number>(140)
  const [widthPx, setWidthPx] = useState<number>(720)

  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth < 768)
    handleResize()
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [])

  // click-outside
  useEffect(() => {
    function onDoc(e: MouseEvent) {
      if (!open) return
      if (ref.current && !ref.current.contains(e.target as Node)) onClose()
    }
    document.addEventListener('mousedown', onDoc)
    return () => document.removeEventListener('mousedown', onDoc)
  }, [open, onClose])

  // compute fixed position so left edge aligns to vertical centerline of the page
  useLayoutEffect(() => {
    if (!open) return
    const margin = 16
    const vw = window.innerWidth

    // desired width: cap at 920, but leave space for margins
    const desired = Math.min(920, Math.floor(vw * 0.9))

    if (isMobile) {
      const w = Math.max(320, vw - margin * 2)
      const l = Math.max(margin, Math.floor((vw - w) / 2))
      setWidthPx(w)
      setLeftPx(l)
    } else {
      // left edge should sit at the vertical centerline of the page
      const centerX = Math.floor(vw / 2)
      let left = centerX
      // if dropdown would overflow right edge, shift left so it fits
      if (left + desired > vw - margin) {
        left = Math.max(margin, vw - desired - margin)
      }
      setWidthPx(desired)
      setLeftPx(left)
    }

    // compute top based on header bottom (keep vertical position consistent)
    const hdr = document.querySelector('header')
    if (hdr) {
      const r = hdr.getBoundingClientRect()
      setTopPx(Math.ceil(r.bottom + 8))
    }
  }, [open, isMobile])

  if (!open) return null

  return (
    <div
      ref={ref}
      className="bg-white rounded-xl shadow-lg border border-primary-peach/20 z-50"
      role="menu"
      aria-hidden={!open}
      style={{ position: 'fixed', top: `${topPx}px`, left: `${leftPx}px`, width: `${widthPx}px` }}
    >
      <div className="flex flex-col md:flex-row">
        {/* Left column - 30% */}
        <div className="w-full md:w-1/3 border-r border-primary-peach/10">
          <div className="p-4">
            <ul className="space-y-1">
              {CATEGORIES.map((c) => (
                <li key={c.id}>
                  <button
                    className={`w-full text-left py-3 px-3 rounded-md flex items-center justify-between hover:bg-soft-peach transition-colors ${active === c.id ? 'bg-soft-peach' : ''}`}
                    onMouseEnter={() => !isMobile && setActive(c.id)}
                    onClick={() => {
                      if (isMobile) {
                        setExpanded((s) => ({ ...s, [c.id]: !s[c.id] }))
                      } else setActive(c.id)
                    }}
                  >
                    <span className="text-deep-sage font-medium">{c.name}</span>
                    <span className="text-deep-sage opacity-60">&gt;</span>
                  </button>

                  {/* mobile accordion content */}
                  {isMobile && expanded[c.id] && (
                    <div className="mt-2 grid grid-cols-1 gap-2">
                      {c.subs.map((s) => (
                        <Link
                          key={s.id}
                          href={`/products?category=${encodeURIComponent(c.name)}&sub=${encodeURIComponent(s.name)}`}
                          onClick={onClose}
                          className="flex items-center gap-3 p-2 rounded-md hover:bg-primary-peach/10"
                        >
                          <div className="w-16 h-12 bg-gray-100 rounded-md flex-shrink-0 flex items-center justify-center text-sm text-gray-500">
                            IMG
                          </div>
                          <div className="text-sm text-deep-sage">{s.name}</div>
                        </Link>
                      ))}
                    </div>
                  )}
                </li>
              ))}
            </ul>
          </div>
        </div>

        {/* Right column - 70% */}
        <div className="w-full md:w-2/3 p-4">
          <div className="md:h-[360px] overflow-auto">
            <div className="grid grid-cols-3 gap-4">
              {(CATEGORIES.find((c) => c.id === active) || CATEGORIES[0]).subs.map((s) => (
                <Link
                  key={s.id}
                  href={`/products?category=${encodeURIComponent((CATEGORIES.find((c) => c.id === active) || CATEGORIES[0]).name)}&sub=${encodeURIComponent(s.name)}`}
                  onClick={onClose}
                  className="block bg-white rounded-lg border border-primary-peach/20 p-2 hover:shadow-md transition-shadow text-center"
                >
                  <div className="w-full h-28 bg-gray-100 rounded-md mb-2 flex items-center justify-center text-gray-500">Image</div>
                  <div className="text-sm font-medium text-deep-sage text-center">{s.name}</div>
                </Link>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
