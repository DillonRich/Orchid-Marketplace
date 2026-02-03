'use client'
import { useEffect, useState } from 'react'
import SimpleNav from '@/components/SimpleNav'

export default function OrdersPage() {
  const [orders, setOrders] = useState<any[]>([])
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    (async () => {
      try {
        const token = localStorage.getItem('auth-token')
        if (!token) return setError('Please sign in to view orders')
        const res = await fetch('http://localhost:8080/api/orders', {
          headers: { Authorization: `Bearer ${token}` }
        })
        const data = await res.json()
        setOrders(Array.isArray(data) ? data : [])
      } catch (e: any) {
        setError(e?.message || 'Failed to load orders')
      }
    })()
  }, [])

  return (
    <div>
      <SimpleNav />
      <div className="max-w-4xl mx-auto p-6">
      <h1 className="text-2xl font-semibold mb-4">My Orders</h1>
      {error && <p className="text-red-600">{error}</p>}
      <div className="space-y-3">
        {orders.map((o) => (
          <a key={o.id} href={`/app/(customer)/orders/${o.id}`} className="block border rounded p-4 hover:bg-gray-50">
            <div className="flex justify-between">
              <span>Order #{o.orderNumber ?? o.id}</span>
              <span className="uppercase text-sm">{o.status}</span>
            </div>
            <div className="text-sm text-gray-600">Total: ${o.total ?? 0}</div>
          </a>
        ))}
        {orders.length === 0 && <p className="text-gray-700">No orders yet.</p>}
      </div>
      </div>
    </div>
  )
}
