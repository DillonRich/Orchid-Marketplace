'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { useParams } from 'next/navigation'
// import { useRouter } from 'next/navigation'
import Image from 'next/image'
import { apiClient } from '@/lib/api-client'

interface OrderItem {
  id: string
  productId: string
  productTitle: string
  productImage: string
  quantity: number
  priceAtPurchase: number
}

interface ShippingAddress {
  fullName: string
  addressLine1: string
  addressLine2?: string
  city: string
  state: string
  zipCode: string
  country: string
  phone: string
}

interface OrderDetails {
  id: string
  orderNumber: string
  customerId: string
  customerName: string
  customerEmail: string
  status: string
  totalAmount: number
  shippingCost: number
  taxAmount: number
  createdAt: string
  updatedAt: string
  items: OrderItem[]
  shippingAddress: ShippingAddress
  trackingNumber?: string
  carrier?: string
  estimatedDelivery?: string
}

export default function OrderDetailPage() {
  const params = useParams()
  const orderId = params.id as string

  const [order, setOrder] = useState<OrderDetails | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [isUpdating, setIsUpdating] = useState(false)
  const [selectedStatus, setSelectedStatus] = useState('')
  
  // Tracking info
  const [trackingNumber, setTrackingNumber] = useState('')
  const [carrier, setCarrier] = useState('')
  const [estimatedDelivery, setEstimatedDelivery] = useState('')

  // Message
  const [message, setMessage] = useState('')
  const [messageTemplate, setMessageTemplate] = useState('')

  useEffect(() => {
    fetchOrder()
  }, [orderId])

  const fetchOrder = async () => {
    try {
      setIsLoading(true)
      const data: OrderDetails = await apiClient.getOrderById(orderId)
      setOrder(data)
      setSelectedStatus(data.status)
      setTrackingNumber(data.trackingNumber || '')
      setCarrier(data.carrier || '')
      setEstimatedDelivery(data.estimatedDelivery || '')
    } catch (err) {
      console.error('Failed to fetch order:', err)
    } finally {
      setIsLoading(false)
    }
  }

  const handleStatusUpdate = async () => {
    if (!selectedStatus || selectedStatus === order?.status) return
    
    try {
      setIsUpdating(true)
      await apiClient.updateOrderStatus(orderId, selectedStatus)
      await fetchOrder()
      alert('Order status updated successfully')
    } catch (err) {
      console.error('Failed to update status:', err)
      alert('Failed to update order status')
    } finally {
      setIsUpdating(false)
    }
  }

  const handleAddTracking = async () => {
    if (!trackingNumber || !carrier) {
      alert('Please enter both tracking number and carrier')
      return
    }

    try {
      setIsUpdating(true)
      await apiClient.addOrderTracking(orderId, {
        trackingNumber,
        carrier,
        estimatedDelivery: estimatedDelivery || undefined
      })
      await fetchOrder()
      alert('Tracking information added successfully')
    } catch (err) {
      console.error('Failed to add tracking:', err)
      alert('Failed to add tracking information')
    } finally {
      setIsUpdating(false)
    }
  }

  const handleSendMessage = async () => {
    const finalMessage = messageTemplate || message
    if (!finalMessage.trim()) {
      alert('Please enter a message')
      return
    }

    try {
      setIsUpdating(true)
      await apiClient.sendOrderMessage(orderId, finalMessage)
      setMessage('')
      setMessageTemplate('')
      alert('Message sent to customer successfully')
    } catch (err) {
      console.error('Failed to send message:', err)
      alert('Failed to send message')
    } finally {
      setIsUpdating(false)
    }
  }

  const messageTemplates = [
    { value: '', label: 'Select a template...' },
    { value: 'order_shipped', label: 'Order Shipped' },
    { value: 'order_delayed', label: 'Order Delayed' },
    { value: 'request_info', label: 'Request More Information' },
  ]

  const getTemplateMessage = (template: string): string => {
    switch (template) {
      case 'order_shipped':
        return `Hi ${order?.customerName},\n\nYour order #${order?.orderNumber} has been shipped! You can track your package with tracking number: ${trackingNumber}.\n\nThank you for your purchase!`
      case 'order_delayed':
        return `Hi ${order?.customerName},\n\nWe wanted to let you know that your order #${order?.orderNumber} will be slightly delayed. We're working hard to get it to you as soon as possible.\n\nWe apologize for any inconvenience.`
      case 'request_info':
        return `Hi ${order?.customerName},\n\nRegarding your order #${order?.orderNumber}, we need some additional information to proceed. Please reply to this message.\n\nThank you!`
      default:
        return ''
    }
  }

  useEffect(() => {
    if (messageTemplate) {
      setMessage(getTemplateMessage(messageTemplate))
    }
  }, [messageTemplate])

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'pending': return 'bg-yellow-100 text-yellow-700'
      case 'processing': return 'bg-blue-100 text-blue-700'
      case 'shipped': return 'bg-purple-100 text-purple-700'
      case 'delivered': return 'bg-green-100 text-green-700'
      case 'cancelled': return 'bg-red-100 text-red-700'
      default: return 'bg-gray-100 text-gray-700'
    }
  }

  const statusOptions = ['pending', 'processing', 'shipped', 'delivered', 'cancelled']
  const carrierOptions = ['USPS', 'FedEx', 'UPS', 'DHL', 'Other']

  if (isLoading || !order) {
    return (
      <div className="max-w-7xl mx-auto p-6">
        <div className="animate-pulse space-y-6">
          <div className="h-8 bg-gray-200 rounded w-1/4"></div>
          <div className="grid grid-cols-3 gap-6">
            <div className="col-span-2 space-y-4">
              <div className="h-64 bg-gray-200 rounded"></div>
              <div className="h-64 bg-gray-200 rounded"></div>
            </div>
            <div className="space-y-4">
              <div className="h-48 bg-gray-200 rounded"></div>
              <div className="h-48 bg-gray-200 rounded"></div>
            </div>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="max-w-7xl mx-auto p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <Link href="/seller/orders" className="text-sage-green hover:text-deep-sage">
              <span className="material-symbols-outlined">arrow_back</span>
            </Link>
            <h1 className="text-3xl font-bold text-deep-sage">Order {order.orderNumber}</h1>
          </div>
          <p className="text-sage-green">
            Placed on {new Date(order.createdAt).toLocaleString()}
          </p>
        </div>
        <span className={`text-sm px-4 py-2 rounded-full ${getStatusColor(order.status)}`}>
          {order.status.charAt(0).toUpperCase() + order.status.slice(1)}
        </span>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-6">
          {/* Order Items */}
          <div className="bg-white rounded-xl p-6">
            <h2 className="text-xl font-semibold text-deep-sage mb-4">Order Items</h2>
            <div className="space-y-4">
              {order.items.map(item => (
                <div key={item.id} className="flex gap-4 p-4 bg-soft-peach rounded-lg">
                  <div className="relative w-20 h-20 rounded-lg overflow-hidden bg-gray-100 flex-shrink-0">
                    <Image
                      src={item.productImage}
                      alt={item.productTitle}
                      fill
                      className="object-cover"
                    />
                  </div>
                  <div className="flex-1">
                    <Link
                      href={`/product/${item.productId}`}
                      className="font-medium text-deep-sage hover:text-accent-peach"
                    >
                      {item.productTitle}
                    </Link>
                    <p className="text-sm text-sage-green mt-1">
                      Quantity: {item.quantity} × ${item.priceAtPurchase.toFixed(2)}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="font-semibold text-deep-sage">
                      ${(item.quantity * item.priceAtPurchase).toFixed(2)}
                    </p>
                  </div>
                </div>
              ))}
            </div>

            {/* Order Summary */}
            <div className="mt-6 pt-6 border-t border-gray-200">
              <div className="space-y-2 text-sm">
                <div className="flex justify-between text-sage-green">
                  <span>Subtotal:</span>
                  <span>${(order.totalAmount - order.shippingCost - order.taxAmount).toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sage-green">
                  <span>Shipping:</span>
                  <span>${order.shippingCost.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sage-green">
                  <span>Tax:</span>
                  <span>${order.taxAmount.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-lg font-semibold text-deep-sage pt-2 border-t border-gray-200">
                  <span>Total:</span>
                  <span>${order.totalAmount.toFixed(2)}</span>
                </div>
              </div>
            </div>
          </div>

          {/* Shipping Address */}
          <div className="bg-white rounded-xl p-6">
            <h2 className="text-xl font-semibold text-deep-sage mb-4">Shipping Address</h2>
            <div className="text-sage-green">
              <p className="font-medium text-deep-sage">{order.shippingAddress.fullName}</p>
              <p>{order.shippingAddress.addressLine1}</p>
              {order.shippingAddress.addressLine2 && <p>{order.shippingAddress.addressLine2}</p>}
              <p>
                {order.shippingAddress.city}, {order.shippingAddress.state} {order.shippingAddress.zipCode}
              </p>
              <p>{order.shippingAddress.country}</p>
              <p className="mt-2">Phone: {order.shippingAddress.phone}</p>
            </div>
          </div>

          {/* Customer Message */}
          <div className="bg-white rounded-xl p-6">
            <h2 className="text-xl font-semibold text-deep-sage mb-4">Contact Customer</h2>
            
            <div className="mb-4">
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Message Template (Optional)
              </label>
              <select
                value={messageTemplate}
                onChange={(e) => setMessageTemplate(e.target.value)}
                className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
              >
                {messageTemplates.map(template => (
                  <option key={template.value} value={template.value}>
                    {template.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="mb-4">
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Message
              </label>
              <textarea
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                rows={6}
                className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent resize-none"
                placeholder="Type your message to the customer..."
              />
            </div>

            <button
              onClick={handleSendMessage}
              disabled={isUpdating}
              className="w-full px-6 py-3 rounded-lg bg-accent-peach text-white hover:bg-deep-sage transition-all disabled:opacity-50"
            >
              Send Message
            </button>
          </div>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Status Update */}
          <div className="bg-white rounded-xl p-6">
            <h2 className="text-xl font-semibold text-deep-sage mb-4">Update Status</h2>
            
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Order Status
                </label>
                <select
                  value={selectedStatus}
                  onChange={(e) => setSelectedStatus(e.target.value)}
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                >
                  {statusOptions.map(status => (
                    <option key={status} value={status}>
                      {status.charAt(0).toUpperCase() + status.slice(1)}
                    </option>
                  ))}
                </select>
              </div>

              <button
                onClick={handleStatusUpdate}
                disabled={isUpdating || selectedStatus === order.status}
                className="w-full px-6 py-3 rounded-lg bg-deep-sage text-white hover:bg-sage-green transition-all disabled:opacity-50"
              >
                Update Status
              </button>
            </div>
          </div>

          {/* Tracking Info */}
          <div className="bg-white rounded-xl p-6">
            <h2 className="text-xl font-semibold text-deep-sage mb-4">Shipping Tracking</h2>
            
            {order.trackingNumber ? (
              <div className="p-4 bg-green-50 rounded-lg mb-4">
                <p className="text-sm text-green-700 mb-1">Tracking Number:</p>
                <p className="font-mono text-deep-sage">{order.trackingNumber}</p>
                <p className="text-sm text-green-700 mt-2">Carrier: {order.carrier}</p>
                {order.estimatedDelivery && (
                  <p className="text-sm text-green-700 mt-1">
                    Est. Delivery: {new Date(order.estimatedDelivery).toLocaleDateString()}
                  </p>
                )}
              </div>
            ) : (
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-deep-sage mb-2">
                    Tracking Number
                  </label>
                  <input
                    type="text"
                    value={trackingNumber}
                    onChange={(e) => setTrackingNumber(e.target.value)}
                    className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                    placeholder="1Z999AA10123456784"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-deep-sage mb-2">
                    Carrier
                  </label>
                  <select
                    value={carrier}
                    onChange={(e) => setCarrier(e.target.value)}
                    className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  >
                    <option value="">Select carrier</option>
                    {carrierOptions.map(c => (
                      <option key={c} value={c}>{c}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-deep-sage mb-2">
                    Estimated Delivery (Optional)
                  </label>
                  <input
                    type="date"
                    value={estimatedDelivery}
                    onChange={(e) => setEstimatedDelivery(e.target.value)}
                    className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  />
                </div>

                <button
                  onClick={handleAddTracking}
                  disabled={isUpdating}
                  className="w-full px-6 py-3 rounded-lg bg-accent-peach text-white hover:bg-deep-sage transition-all disabled:opacity-50"
                >
                  Add Tracking Info
                </button>
              </div>
            )}
          </div>

          {/* Customer Info */}
          <div className="bg-white rounded-xl p-6">
            <h2 className="text-xl font-semibold text-deep-sage mb-4">Customer Info</h2>
            <div className="space-y-2 text-sm">
              <p className="font-medium text-deep-sage">{order.customerName}</p>
              <p className="text-sage-green">{order.customerEmail}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
