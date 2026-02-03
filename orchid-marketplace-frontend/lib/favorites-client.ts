import { create } from 'zustand'
import { persist, createJSONStorage } from 'zustand/middleware'
import { getLocalUserId, getAuthUserId, getEffectiveUserId, clearLocalUserId } from './user'

type FavState = {
  items: Record<string, true>
  add: (id: string) => void
  remove: (id: string) => void
  toggle: (id: string) => void
  has: (id: string) => boolean
  clear: () => void
  loadFromServer: () => Promise<void>
  setLocal: (items: Record<string, true>) => void
}

export const useFavoritesClientStore = create<FavState>()(
  persist(
    (set, get) => ({
      items: {},
      add: (id: string) => {
        set((s) => ({ items: { ...s.items, [id]: true } }))
        const uid = getEffectiveUserId()
        if (uid) {
          fetch('/api/favorites', { method: 'POST', headers: { 'Content-Type': 'application/json', 'x-user-id': uid }, body: JSON.stringify({ id }) }).catch(() => {})
        }
      },
      remove: (id: string) => {
        set((s) => {
          const items = { ...s.items }
          delete items[id]
          return { items }
        })
        const uid = getEffectiveUserId()
        if (uid) {
          fetch('/api/favorites', { method: 'DELETE', headers: { 'Content-Type': 'application/json', 'x-user-id': uid }, body: JSON.stringify({ id }) }).catch(() => {})
        }
      },
      toggle: (id: string) => {
        const has = !!get().items[id]
        if (has) get().remove(id)
        else get().add(id)
      },
      has: (id: string) => !!get().items[id],
      clear: () => set({ items: {} }),
      loadFromServer: async () => {
        try {
          const uid = getEffectiveUserId()
          if (!uid) return
          const res = await fetch(`/api/favorites?userId=${uid}`)
          if (!res.ok) return
          const data = await res.json()
          if (data?.items && Array.isArray(data.items)) {
            const map: Record<string, true> = {}
            data.items.forEach((i: string) => (map[i] = true))
            set({ items: map })
          }
        } catch (e) {
          // ignore network errors
        }
      },
      setLocal: (items: Record<string, true>) => set({ items }),
    }),
    {
      name: 'orchid-favorites-client',
      storage: createJSONStorage(() => (typeof window !== 'undefined' ? window.localStorage : ({} as Storage))),
    }
  )
)

export function setFavoritesLocalClient(items: Record<string, true>) {
  // @ts-ignore
  useFavoritesClientStore.setState({ items })
}

// Called when a user signs in: merge local client items into authenticated account
export async function mergeLocalFavoritesToAccount() {
  try {
    const authId = getAuthUserId()
    if (!authId) return
    // read local client items
    // @ts-ignore
    const itemsMap: Record<string, true> = useFavoritesClientStore.getState().items || {}
    const items = Object.keys(itemsMap)
    if (items.length === 0) {
      // nothing to merge
      return
    }
    const localId = getLocalUserId()
    const res = await fetch('/api/favorites/merge', { method: 'POST', headers: { 'Content-Type': 'application/json', 'x-user-id': authId }, body: JSON.stringify({ items, localId }) })
    if (!res.ok) return
    const data = await res.json()
    // seed client from merged server state
    if (data?.items && Array.isArray(data.items)) {
      const map: Record<string, true> = {}
      data.items.forEach((i: string) => (map[i] = true))
      // @ts-ignore
      useFavoritesClientStore.setState({ items: map })
    }
    // clear the legacy local id (optional) so future writes go to server
    clearLocalUserId()
    // mark merged in this window session
    // @ts-ignore
    window.__orchid_favs_merged = true
  } catch (e) {
    // ignore
  }
}
