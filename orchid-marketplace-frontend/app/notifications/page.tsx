'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import Header from '@/components/Header'
import Footer from '@/components/Footer'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'

interface Notification {
  id: number
  type: 'ORDER' | 'MESSAGE' | 'SYSTEM' | 'REVIEW'
  title: string
  message: string
  read: boolean
  createdAt: string
  relatedEntityId?: number
  relatedEntityType?: string
}

export default function NotificationsPage() {
  const router = useRouter()
  const { isAuthenticated } = useAuthStore()
  
  const [notifications, setNotifications] = useState<Notification[]>([])
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState<'all' | 'unread'>('all')
  
  useEffect(() => {
    if (!isAuthenticated) {
      router.push('/login?redirect=/notifications')
      return
    }
    fetchNotifications()
  }, [isAuthenticated, filter])
  
  const fetchNotifications = async () => {
    try {
      setLoading(true)
      const data = await apiClient.getNotifications({ 
        unreadOnly: filter === 'unread',
        page: 0,
        size: 50
      })
      setNotifications(data.content || [])
    } catch (error) {
      console.error('Failed to fetch notifications:', error)
    } finally {
      setLoading(false)
    }
  }
  
  const handleNotificationClick = async (notification: Notification) => {
    // Mark as read
    if (!notification.read) {
      try {
        await apiClient.markNotificationAsRead(notification.id)
        setNotifications(prev => 
          prev.map(n => n.id === notification.id ? { ...n, read: true } : n)
        )
      } catch (error) {
        console.error('Failed to mark notification as read:', error)
      }
    }
    
    // Navigate to related page
    if (notification.relatedEntityType === 'ORDER' && notification.relatedEntityId) {
      router.push(`/account/orders/${notification.relatedEntityId}`)
    } else if (notification.relatedEntityType === 'MESSAGE' && notification.relatedEntityId) {
      router.push(`/messages/${notification.relatedEntityId}`)
    } else if (notification.relatedEntityType === 'PRODUCT' && notification.relatedEntityId) {
      router.push(`/product/${notification.relatedEntityId}`)
    }
  }
  
  const handleMarkAllAsRead = async () => {
    try {
      await apiClient.markAllNotificationsAsRead()
      setNotifications(prev => prev.map(n => ({ ...n, read: true })))
    } catch (error) {
      console.error('Failed to mark all as read:', error)
    }
  }
  
  const handleDeleteNotification = async (id: number, event: React.MouseEvent) => {
    event.stopPropagation()
    try {
      await apiClient.deleteNotification(id)
      setNotifications(prev => prev.filter(n => n.id !== id))
    } catch (error) {
      console.error('Failed to delete notification:', error)
    }
  }
  
  const getNotificationIcon = (type: string) => {
    switch (type) {
      case 'ORDER':
        return 'shopping_bag'
      case 'MESSAGE':
        return 'mail'
      case 'REVIEW':
        return 'star'
      case 'SYSTEM':
      default:
        return 'notifications'
    }
  }
  
  const getNotificationColor = (type: string) => {
    switch (type) {
      case 'ORDER':
        return 'bg-accent-peach'
      case 'MESSAGE':
        return 'bg-deep-sage'
      case 'REVIEW':
        return 'bg-yellow-500'
      case 'SYSTEM':
      default:
        return 'bg-sage-green'
    }
  }
  
  const formatTimeAgo = (dateString: string) => {
    const date = new Date(dateString)
    const now = new Date()
    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000)
    
    if (seconds < 60) return 'Just now'
    if (seconds < 3600) return `${Math.floor(seconds / 60)}m ago`
    if (seconds < 86400) return `${Math.floor(seconds / 3600)}h ago`
    if (seconds < 604800) return `${Math.floor(seconds / 86400)}d ago`
    return date.toLocaleDateString()
  }
  
  const unreadCount = notifications.filter(n => !n.read).length
  
  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[900px] mx-auto px-4 py-16">
          
          {/* Header */}
          <div className="mb-8">
            <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-4">
              Notifications
            </h1>
            <p className="text-sage-green text-lg">
              Stay updated with your orders, messages, and important updates
            </p>
          </div>
          
          {/* Filter & Actions Bar */}
          <div className="bg-white rounded-xl p-4 mb-6 flex items-center justify-between">
            <div className="flex gap-2">
              <button
                onClick={() => setFilter('all')}
                className={`px-6 py-2 rounded-full font-medium transition-all ${
                  filter === 'all'
                    ? 'bg-deep-sage text-white'
                    : 'bg-soft-peach text-deep-sage hover:bg-accent-peach'
                }`}
              >
                All
              </button>
              <button
                onClick={() => setFilter('unread')}
                className={`px-6 py-2 rounded-full font-medium transition-all flex items-center gap-2 ${
                  filter === 'unread'
                    ? 'bg-deep-sage text-white'
                    : 'bg-soft-peach text-deep-sage hover:bg-accent-peach'
                }`}
              >
                Unread
                {unreadCount > 0 && (
                  <span className="bg-accent-peach text-white text-xs px-2 py-1 rounded-full">
                    {unreadCount}
                  </span>
                )}
              </button>
            </div>
            
            {unreadCount > 0 && (
              <button
                onClick={handleMarkAllAsRead}
                className="text-sage-green hover:text-deep-sage font-medium text-sm flex items-center gap-1"
              >
                <span className="material-symbols-outlined text-sm">done_all</span>
                Mark all as read
              </button>
            )}
          </div>
          
          {/* Notifications List */}
          {loading ? (
            <div className="flex justify-center items-center h-64">
              <div className="text-center">
                <span className="material-symbols-outlined text-6xl text-sage-green animate-pulse">
                  notifications
                </span>
                <p className="text-sage-green mt-4">Loading notifications...</p>
              </div>
            </div>
          ) : notifications.length === 0 ? (
            <div className="text-center py-16 bg-white rounded-xl">
              <span className="material-symbols-outlined text-8xl text-sage-green mb-4 block">
                notifications_off
              </span>
              <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-4">
                No notifications
              </h2>
              <p className="text-sage-green mb-6">
                {filter === 'unread' 
                  ? "You're all caught up! No unread notifications."
                  : "You don't have any notifications yet."}
              </p>
              <button
                onClick={() => router.push('/products')}
                className="bg-soft-peach hover:bg-accent-peach text-deep-sage px-8 py-3 rounded-full font-medium transition-all"
              >
                Continue Shopping
              </button>
            </div>
          ) : (
            <div className="space-y-3">
              {notifications.map(notification => (
                <div
                  key={notification.id}
                  onClick={() => handleNotificationClick(notification)}
                  className={`bg-white rounded-xl p-6 cursor-pointer hover:shadow-lg transition-all group ${
                    !notification.read ? 'border-2 border-accent-peach' : ''
                  }`}
                >
                  <div className="flex items-start gap-4">
                    <div className={`w-12 h-12 ${getNotificationColor(notification.type)} rounded-full flex items-center justify-center flex-shrink-0`}>
                      <span className="material-symbols-outlined text-white text-2xl">
                        {getNotificationIcon(notification.type)}
                      </span>
                    </div>
                    
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-4 mb-1">
                        <h3 className="font-medium text-deep-sage text-lg">
                          {notification.title}
                          {!notification.read && (
                            <span className="ml-2 w-2 h-2 bg-accent-peach rounded-full inline-block"></span>
                          )}
                        </h3>
                        <span className="text-sage-green text-sm whitespace-nowrap">
                          {formatTimeAgo(notification.createdAt)}
                        </span>
                      </div>
                      
                      <p className="text-sage-green mb-2">
                        {notification.message}
                      </p>
                      
                      <div className="flex items-center gap-4 text-sm">
                        {notification.relatedEntityType && (
                          <span className="text-deep-sage font-medium group-hover:text-accent-peach transition-colors">
                            View Details →
                          </span>
                        )}
                      </div>
                    </div>
                    
                    <button
                      onClick={(e) => handleDeleteNotification(notification.id, e)}
                      className="w-8 h-8 bg-soft-peach rounded-full flex items-center justify-center hover:bg-accent-peach transition-all opacity-0 group-hover:opacity-100"
                      title="Delete notification"
                    >
                      <span className="material-symbols-outlined text-sm text-deep-sage">
                        close
                      </span>
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
      <Footer />
    </>
  )
}
