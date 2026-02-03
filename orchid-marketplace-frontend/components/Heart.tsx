"use client"

import React, { useState } from 'react'
import './heart.css'
import { useFavoritesClientStore as useFavoritesStore, mergeLocalFavoritesToAccount } from '@/lib/favorites-client'
import { getEffectiveUserId, getAuthUserId } from '@/lib/user'

type Props = {
  id: string
  onToggle?: (v: boolean) => void
}

export default function Heart({ id, onToggle }: Props) {
  const liked = useFavoritesStore((s: any) => !!s.items[id])
  const toggleAction = useFavoritesStore((s: any) => s.toggle)
  const [anim, setAnim] = useState<boolean>(false)

  const toggle = (e: React.MouseEvent) => {
    e.stopPropagation()
    const next = !liked
    // optimistic local update
    toggleAction(id)
    // persist to server using effective id (auth preferred)
    const uid = getEffectiveUserId()
    if (uid) {
      const method = next ? 'POST' : 'DELETE'
      fetch('/api/favorites', { method, headers: { 'Content-Type': 'application/json', 'x-user-id': uid }, body: JSON.stringify({ id }) }).catch(() => {})
    }
    setAnim(true)
    onToggle?.(next)
    window.setTimeout(() => setAnim(false), 420)
  }

  // sync once from server on first mount
  React.useEffect(() => {
    try {
      // Only sync once per window session
      if ((window as any).__orchid_favs_synced) return

      // If user is authenticated, merge local items into account first
      const authId = getAuthUserId()
      if (authId && !(window as any).__orchid_favs_merged) {
        // attempt merge; this will also seed the client store if successful
        mergeLocalFavoritesToAccount().catch(() => {})
        ;(window as any).__orchid_favs_merged = true
      }

      const uid = getEffectiveUserId()
      if (!uid) return
      fetch(`/api/favorites?userId=${uid}`).then((r) => r.json()).then((data) => {
        if (data?.items && Array.isArray(data.items)) {
          const map: Record<string, true> = {}
          data.items.forEach((i: string) => (map[i] = true))
          // @ts-ignore
          useFavoritesStore.setState({ items: map })
        }
      }).catch(() => {})
      ;(window as any).__orchid_favs_synced = true
    } catch (e) {
      // ignore
    }
  }, [])

  return (
    <button
      type="button"
      aria-pressed={liked}
      onClick={toggle}
      className="heart-btn"
    >
      <svg viewBox="0 0 256 256" className={`heart-svg ${anim ? 'heart-anim' : ''} ${liked ? 'heart-filled' : 'heart-empty'}`}>
        <rect fill="none" height="256" width="256"></rect>
        <path d="M224.6,51.9a59.5,59.5,0,0,0-43-19.9,60.5,60.5,0,0,0-44,17.6L128,59.1l-7.5-7.4C97.2,28.3,59.2,26.3,35.9,47.4a59.9,59.9,0,0,0-2.3,87l83.1,83.1a15.9,15.9,0,0,0,22.6,0l81-81C243.7,113.2,245.6,75.2,224.6,51.9Z" strokeWidth="18px" stroke="#E6E6E6" fill="none"></path>
      </svg>

      {anim && !liked && <span className="heart-pulse" aria-hidden />}
    </button>
  )
}
