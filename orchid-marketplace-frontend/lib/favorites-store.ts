// Minimal shim for `useFavoritesStore` to avoid build errors from legacy imports.
// New code uses `lib/favorites-client.ts`.

export const useFavoritesStore = () => {
  return {
    items: {} as Record<string, true>,
    add: (_: string) => {},
    remove: (_: string) => {},
    toggle: (_: string) => {},
    has: (_: string) => false,
    clear: () => {},
    loadFromServer: async () => {},
    setLocal: (_: Record<string, true>) => {},
  } as any
}

export function setFavoritesLocal(_: Record<string, true>) {}

