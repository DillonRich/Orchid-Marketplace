// Returns a persistent local id stored in `localStorage` (legacy / fallback)
export function getLocalUserId(): string | undefined {
  if (typeof window === 'undefined') return undefined
  const key = 'orchid-user-id'
  let id = window.localStorage.getItem(key)
  if (!id) {
    const newId = (typeof crypto !== 'undefined' && (crypto as any).randomUUID)
      ? (crypto as any).randomUUID()
      : Math.random().toString(36).slice(2, 10)
    window.localStorage.setItem(key, newId)
    id = newId
  }
  return id || undefined
}

// If your app wires authentication (NextAuth, custom auth), set a global
// `window.__orchid_auth_user_id = '<user-id>'` after sign-in so the client
// can prefer the authenticated id. This helper reads that value, if present.
export function getAuthUserId(): string | undefined {
  if (typeof window === 'undefined') return undefined
  // auth integration should set this global after successful sign-in
  // e.g. window.__orchid_auth_user_id = session.user.id
  // Keep tolerant lookups for common names used by integrations.
  // @ts-ignore
  const gid = (window.__orchid_auth_user_id as string) || (window.__AUTH_USER_ID as string) || undefined
  return gid
}

// Effective user id used for server requests: prefer authenticated id,
// fall back to local id. Useful for calling server endpoints that expect
// a stable user identifier.
export function getEffectiveUserId(): string | undefined {
  return getAuthUserId() || getLocalUserId()
}

export function clearLocalUserId() {
  if (typeof window === 'undefined') return
  window.localStorage.removeItem('orchid-user-id')
}
