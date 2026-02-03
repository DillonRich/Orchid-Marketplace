import { create } from 'zustand'

export type CartItem = {
  id: string
  title: string
  price: number
  quantity: number
  imageUrl?: string
}

interface CartState {
  itemCount: number
  items: CartItem[]
  slideOpen: boolean
  setItemCount: (count: number) => void
  addItem: (item: CartItem) => void
  addToCart: (item: CartItem) => void // Alias for addItem
  removeItem: (id: string) => void
  updateQty: (id: string, qty: number) => void
  clear: () => void
  clearCart: () => void // Alias for clear
  openSlide: () => void
  closeSlide: () => void
  totalPrice: () => number
}

export const useCartStore = create<CartState>((set, get) => ({
  itemCount: 0,
  items: [],
  slideOpen: false,

  setItemCount: (count: number) => set({ itemCount: count }),

  addItem: (item: CartItem) => {
    set((state) => {
      const exists = state.items.find((i) => i.id === item.id)
      let items
      if (exists) {
        items = state.items.map((i) => (i.id === item.id ? { ...i, quantity: i.quantity + item.quantity } : i))
      } else {
        items = [...state.items, item]
      }
      return { items, itemCount: items.reduce((s, it) => s + it.quantity, 0), slideOpen: true }
    })
    // automatically open slide when adding
    setTimeout(() => {
      // keep slide open for a short time by default, ui component controls closing
    }, 0)
  },

  addToCart: (item: CartItem) => get().addItem(item), // Alias

  removeItem: (id: string) => set((state) => {
    const items = state.items.filter((i) => i.id !== id)
    return { items, itemCount: items.reduce((s, it) => s + it.quantity, 0) }
  }),

  updateQty: (id: string, qty: number) => set((state) => {
    const items = state.items.map((i) => (i.id === id ? { ...i, quantity: qty } : i)).filter((i) => i.quantity > 0)
    return { items, itemCount: items.reduce((s, it) => s + it.quantity, 0) }
  }),

  clear: () => set({ items: [], itemCount: 0 }),

  clearCart: () => get().clear(), // Alias

  openSlide: () => set({ slideOpen: true }),
  closeSlide: () => set({ slideOpen: false }),

  totalPrice: () => get().items.reduce((s, it) => s + it.price * it.quantity, 0),
}))
