'use client'

import { useState, useEffect } from 'react'

export default function NotificationsPage() {
  const [filter, setFilter] = useState('all')
  const [notifications, setNotifications] = useState<any[]>([])
  
  useEffect(() => {
    // Mock notifications
    setNotifications([
      {
        id: '1',
        type: 'order',
        icon: 'package',
        title: 'Order Shipped',
        message: 'Your order #ORD-12345 has been shipped and is on its way',
        timestamp: '2 hours ago',
        read: false,
        link: '/account/orders/1',
      },
      {
        id: '2',
        type: 'message',
        icon: 'mail',
        title: 'New Message',
        message: 'Orchid Paradise replied to your inquiry',
        timestamp: '5 hours ago',
        read: false,
        link: '/account/messages',
      },
      {
        id: '3',
        type: 'account',
        icon: 'person',
        title: 'Profile Updated',
        message: 'Your profile information has been successfully updated',
        timestamp: '1 day ago',
        read: true,
        link: '/account/profile',
      },
      {
        id: '4',
        type: 'promotion',
        icon: 'local_offer',
        title: 'Special Offer',
        message: 'Get 20% off on all Phalaenopsis orchids this weekend!',
        timestamp: '2 days ago',
        read: true,
        link: '/products?category=phalaenopsis',
      },
    ])
  }, [])
  
  const filteredNotifications = notifications.filter(notif => 
    filter === 'all' || notif.type === filter
  )
  
  const unreadCount = notifications.filter(n => !n.read).length
  
  const filterOptions = [
    { value: 'all', label: 'All', count: notifications.length },
    { value: 'order', label: 'Orders', count: notifications.filter(n => n.type === 'order').length },
    { value: 'message', label: 'Messages', count: notifications.filter(n => n.type === 'message').length },
    { value: 'account', label: 'Account', count: notifications.filter(n => n.type === 'account').length },
    { value: 'promotion', label: 'Promotions', count: notifications.filter(n => n.type === 'promotion').length },
  ]
  
  const handleMarkAsRead = (id: string) => {
    setNotifications(prev => prev.map(n => 
      n.id === id ? { ...n, read: true } : n
    ))
  }
  
  const handleMarkAllAsRead = () => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })))
  }
  
  const handleDelete = (id: string) => {
    setNotifications(prev => prev.filter(n => n.id !== id))
  }
  
  const handleClearAll = () => {
    if (confirm('Are you sure you want to clear all notifications?')) {
      setNotifications([])
    }
  }
  
  return (
    <div className="space-y-8">
      <div>
        <div className="flex items-center justify-between mb-4">
          <div>
            <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
              Notifications
            </h1>
            <p className="text-sage-green">
              {unreadCount > 0 ? `You have ${unreadCount} unread notification${unreadCount === 1 ? '' : 's'}` : 'All caught up!'}
            </p>
          </div>
          
          {notifications.length > 0 && (
            <div className="flex gap-2">
              <button
                onClick={handleMarkAllAsRead}
                className="text-sm text-accent-peach hover:text-deep-sage font-medium"
              >
                Mark all read
              </button>
              <span className="text-sage-green">•</span>
              <button
                onClick={handleClearAll}
                className="text-sm text-accent-peach hover:text-deep-sage font-medium"
              >
                Clear all
              </button>
            </div>
          )}
        </div>
      </div>
      
      {/* Filters */}
      <div className="bg-white rounded-xl p-4">
        <div className="flex gap-2 overflow-x-auto">
          {filterOptions.map(option => (
            <button
              key={option.value}
              onClick={() => setFilter(option.value)}
              className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition-all ${
                filter === option.value
                  ? 'bg-deep-sage text-white'
                  : 'bg-gray-100 text-sage-green hover:bg-soft-peach'
              }`}
            >
              {option.label}
              {option.count > 0 && (
                <span className="ml-2 text-sm opacity-75">
                  {option.count}
                </span>
              )}
            </button>
          ))}
        </div>
      </div>
      
      {/* Notifications List */}
      {filteredNotifications.length > 0 ? (
        <div className="space-y-3">
          {filteredNotifications.map(notif => (
            <div
              key={notif.id}
              className={`bg-white rounded-xl p-6 hover:shadow-lg transition-all ${
                !notif.read ? 'border-l-4 border-accent-peach' : ''
              }`}
            >
              <div className="flex gap-4">
                <div className={`w-12 h-12 rounded-full flex items-center justify-center flex-shrink-0 ${
                  notif.type === 'order' ? 'bg-blue-100' :
                  notif.type === 'message' ? 'bg-green-100' :
                  notif.type === 'account' ? 'bg-purple-100' :
                  'bg-orange-100'
                }`}>
                  <span className={`material-symbols-outlined ${
                    notif.type === 'order' ? 'text-blue-600' :
                    notif.type === 'message' ? 'text-green-600' :
                    notif.type === 'account' ? 'text-purple-600' :
                    'text-orange-600'
                  }`}>
                    {notif.icon}
                  </span>
                </div>
                
                <div className="flex-1">
                  <div className="flex items-start justify-between gap-4 mb-2">
                    <div>
                      <h3 className="font-medium text-deep-sage mb-1">
                        {notif.title}
                      </h3>
                      <p className="text-sage-green text-sm">
                        {notif.message}
                      </p>
                    </div>
                    
                    {!notif.read && (
                      <span className="w-2 h-2 bg-accent-peach rounded-full flex-shrink-0 mt-2" />
                    )}
                  </div>
                  
                  <div className="flex items-center gap-4 text-sm">
                    <span className="text-sage-green">{notif.timestamp}</span>
                    
                    {notif.link && (
                      <a
                        href={notif.link}
                        className="text-accent-peach hover:text-deep-sage font-medium"
                      >
                        View details
                      </a>
                    )}
                    
                    {!notif.read && (
                      <button
                        onClick={() => handleMarkAsRead(notif.id)}
                        className="text-accent-peach hover:text-deep-sage font-medium"
                      >
                        Mark as read
                      </button>
                    )}
                    
                    <button
                      onClick={() => handleDelete(notif.id)}
                      className="text-sage-green hover:text-red-500 ml-auto"
                    >
                      <span className="material-symbols-outlined text-xl">delete</span>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            notifications
          </span>
          <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
            No notifications
          </h3>
          <p className="text-sage-green">
            {filter === 'all' 
              ? 'You\'re all caught up!' 
              : `No ${filter} notifications at this time`
            }
          </p>
        </div>
      )}
    </div>
  )
}
