'use client'
import { useEffect, useState } from 'react'
import { useParams } from 'next/navigation'
import SimpleNav from '@/components/SimpleNav'

export default function OrderDetailPage() {
  const params = useParams()
  const id = Array.isArray(params?.id) ? params.id[0] : (params?.id as string)
  const [order, setOrder] = useState<any | null>(null)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    (async () => {
      if (!id) return
      try {
        const token = localStorage.getItem('auth-token')
        if (!token) return setError('Please sign in to view order details')
        const res = await fetch(`http://localhost:8080/api/orders/${id}`, {
          headers: { Authorization: `Bearer ${token}` }
        })
        const data = await res.json()
        setOrder(data)
      } catch (e: any) {
        setError(e?.message || 'Failed to load order')
      }
    })()
  }, [id])

  if (!id) return <div className="p-6">Invalid order id</div>

  return (
    <div>
      <SimpleNav />
      <div className="max-w-4xl mx-auto p-6 space-y-4">
      <h1 className="text-2xl font-semibold">Order #{order?.orderNumber ?? id}</h1>
      {error && <p className="text-red-600">{error}</p>}
      {!order ? (
        <p>Loading…</p>
      ) : (
        <div className="space-y-4">
          <div className="border rounded p-4">
            <div className="flex justify-between">
              <span>Status</span>
              <span className="uppercase">{order.status}</span>
            </div>
            <div className="flex justify-between mt-2">
              <span>Total</span>
              <span>${order.total ?? 0}</span>
            </div>
          </div>
          <div className="border rounded p-4">
            <h2 className="text-lg font-medium mb-2">Items</h2>
            <div className="space-y-2">
              {order.items?.map((item:any) => (
                <div key={item.id} className="flex justify-between">
                  <span>{item.productTitle ?? item.product?.title} × {item.quantity}</span>
                  <span>${(item.price ?? item.product?.price ?? 0) * item.quantity}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
      </div>
    </div>
  )
}
