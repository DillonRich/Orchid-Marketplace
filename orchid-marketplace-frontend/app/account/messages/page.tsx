'use client'

import { useState, useEffect } from 'react'

export default function MessagesPage() {
  const [conversations, setConversations] = useState<any[]>([])
  const [activeConversation, setActiveConversation] = useState<string | null>(null)
  const [messageText, setMessageText] = useState('')
  const [searchQuery, setSearchQuery] = useState('')
  const [filter, setFilter] = useState('all')
  
  useEffect(() => {
    // Mock conversations
    setConversations([
      {
        id: '1',
        sellerId: 'seller1',
        sellerName: 'Orchid Paradise',
        sellerAvatar: '/placeholder.jpg',
        lastMessage: 'Thank you for your purchase! Your orchid will be shipped tomorrow.',
        timestamp: '2 hours ago',
        unread: 2,
        orderId: 'ORD-12345',
        messages: [
          {
            id: 'm1',
            sender: 'customer',
            text: 'Hi, when will my order be shipped?',
            timestamp: '3 hours ago',
            read: true,
          },
          {
            id: 'm2',
            sender: 'seller',
            text: 'Hello! Your order will be shipped tomorrow morning. Thank you for your patience!',
            timestamp: '2 hours ago',
            read: false,
          },
          {
            id: 'm3',
            sender: 'seller',
            text: 'You should receive a tracking number within 24 hours.',
            timestamp: '2 hours ago',
            read: false,
          },
        ],
      },
      {
        id: '2',
        sellerId: 'seller2',
        sellerName: 'Exotic Blooms',
        sellerAvatar: '/placeholder.jpg',
        lastMessage: 'Yes, we can accommodate custom arrangements.',
        timestamp: '1 day ago',
        unread: 0,
        orderId: null,
        messages: [
          {
            id: 'm4',
            sender: 'customer',
            text: 'Do you offer custom arrangements?',
            timestamp: '2 days ago',
            read: true,
          },
          {
            id: 'm5',
            sender: 'seller',
            text: 'Yes, we can accommodate custom arrangements. Please let us know what you have in mind!',
            timestamp: '1 day ago',
            read: true,
          },
        ],
      },
    ])
    
    // Set first conversation as active by default
    if (conversations.length > 0) {
      setActiveConversation('1')
    }
  }, [])
  
  const filteredConversations = conversations.filter(conv => {
    const matchesFilter = 
      filter === 'all' || 
      (filter === 'unread' && conv.unread > 0) ||
      (filter === 'archived' && false) // No archived conversations in mock data
    
    const matchesSearch = 
      conv.sellerName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      conv.lastMessage.toLowerCase().includes(searchQuery.toLowerCase())
    
    return matchesFilter && matchesSearch
  })
  
  const activeConv = conversations.find(c => c.id === activeConversation)
  
  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault()
    if (!messageText.trim() || !activeConversation) return
    
    // Add message to active conversation
    setConversations(prev => prev.map(conv => {
      if (conv.id === activeConversation) {
        return {
          ...conv,
          messages: [
            ...conv.messages,
            {
              id: `m${Date.now()}`,
              sender: 'customer',
              text: messageText,
              timestamp: 'Just now',
              read: false,
            }
          ],
          lastMessage: messageText,
          timestamp: 'Just now',
        }
      }
      return conv
    }))
    
    setMessageText('')
  }
  
  return (
    <div className="space-y-8">
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Messages
        </h1>
        <p className="text-sage-green">
          Chat with sellers about products and orders
        </p>
      </div>
      
      <div className="bg-white rounded-xl overflow-hidden" style={{ height: '600px' }}>
        <div className="grid grid-cols-12 h-full">
          {/* Conversations List */}
          <div className="col-span-12 md:col-span-4 border-r border-gray-200 flex flex-col">
            {/* Search & Filters */}
            <div className="p-4 border-b border-gray-200 space-y-3">
              <input
                type="text"
                placeholder="Search conversations..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full px-4 py-2 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach text-sm"
              />
              
              <div className="flex gap-2">
                {['all', 'unread', 'archived'].map(f => (
                  <button
                    key={f}
                    onClick={() => setFilter(f)}
                    className={`px-3 py-1 rounded-lg text-sm font-medium transition-all ${
                      filter === f
                        ? 'bg-deep-sage text-white'
                        : 'bg-gray-100 text-sage-green hover:bg-soft-peach'
                    }`}
                  >
                    {f.charAt(0).toUpperCase() + f.slice(1)}
                  </button>
                ))}
              </div>
            </div>
            
            {/* Conversations */}
            <div className="flex-1 overflow-y-auto">
              {filteredConversations.length > 0 ? (
                filteredConversations.map(conv => (
                  <button
                    key={conv.id}
                    onClick={() => setActiveConversation(conv.id)}
                    className={`w-full p-4 border-b border-gray-200 hover:bg-soft-peach transition-all text-left ${
                      activeConversation === conv.id ? 'bg-soft-peach' : ''
                    }`}
                  >
                    <div className="flex gap-3">
                      <div className="relative">
                        <div className="w-12 h-12 rounded-full bg-gray-200 flex-shrink-0" />
                        {conv.unread > 0 && (
                          <span className="absolute -top-1 -right-1 w-5 h-5 bg-accent-peach text-white text-xs rounded-full flex items-center justify-center">
                            {conv.unread}
                          </span>
                        )}
                      </div>
                      
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center justify-between mb-1">
                          <h4 className="font-medium text-deep-sage truncate">
                            {conv.sellerName}
                          </h4>
                          <span className="text-xs text-sage-green flex-shrink-0 ml-2">
                            {conv.timestamp}
                          </span>
                        </div>
                        <p className="text-sm text-sage-green truncate">
                          {conv.lastMessage}
                        </p>
                        {conv.orderId && (
                          <span className="text-xs text-accent-peach mt-1 inline-block">
                            Order: {conv.orderId}
                          </span>
                        )}
                      </div>
                    </div>
                  </button>
                ))
              ) : (
                <div className="p-8 text-center">
                  <span className="material-symbols-outlined text-4xl text-sage-green mb-2">
                    inbox
                  </span>
                  <p className="text-sm text-sage-green">
                    No conversations found
                  </p>
                </div>
              )}
            </div>
          </div>
          
          {/* Message Thread */}
          <div className="col-span-12 md:col-span-8 flex flex-col">
            {activeConv ? (
              <>
                {/* Thread Header */}
                <div className="p-4 border-b border-gray-200 flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-gray-200" />
                    <div>
                      <h3 className="font-medium text-deep-sage">
                        {activeConv.sellerName}
                      </h3>
                      {activeConv.orderId && (
                        <a
                          href={`/account/orders/${activeConv.id}`}
                          className="text-xs text-accent-peach hover:underline"
                        >
                          View Order {activeConv.orderId}
                        </a>
                      )}
                    </div>
                  </div>
                  
                  <button className="text-sage-green hover:text-deep-sage">
                    <span className="material-symbols-outlined">more_vert</span>
                  </button>
                </div>
                
                {/* Messages */}
                <div className="flex-1 overflow-y-auto p-4 space-y-4">
                  {activeConv.messages.map((msg: any) => (
                    <div
                      key={msg.id}
                      className={`flex ${msg.sender === 'customer' ? 'justify-end' : 'justify-start'}`}
                    >
                      <div className={`max-w-[70%] ${
                        msg.sender === 'customer'
                          ? 'bg-deep-sage text-white'
                          : 'bg-gray-100 text-sage-green'
                      } rounded-2xl px-4 py-3`}>
                        <p className="text-sm">{msg.text}</p>
                        <p className={`text-xs mt-1 ${
                          msg.sender === 'customer' ? 'text-white/70' : 'text-sage-green/70'
                        }`}>
                          {msg.timestamp}
                        </p>
                      </div>
                    </div>
                  ))}
                </div>
                
                {/* Message Input */}
                <form onSubmit={handleSendMessage} className="p-4 border-t border-gray-200">
                  <div className="flex gap-3">
                    <button
                      type="button"
                      className="text-sage-green hover:text-deep-sage"
                    >
                      <span className="material-symbols-outlined">attach_file</span>
                    </button>
                    
                    <input
                      type="text"
                      value={messageText}
                      onChange={(e) => setMessageText(e.target.value)}
                      placeholder="Type a message..."
                      className="flex-1 px-4 py-2 rounded-full border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                    />
                    
                    <button
                      type="submit"
                      disabled={!messageText.trim()}
                      className="bg-deep-sage text-white px-6 py-2 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      Send
                    </button>
                  </div>
                </form>
              </>
            ) : (
              <div className="flex-1 flex items-center justify-center">
                <div className="text-center">
                  <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
                    chat
                  </span>
                  <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
                    Select a conversation
                  </h3>
                  <p className="text-sage-green">
                    Choose a conversation from the list to start messaging
                  </p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
      
      {conversations.length === 0 && (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            mail
          </span>
          <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
            No messages yet
          </h3>
          <p className="text-sage-green mb-6">
            Start a conversation with a seller by visiting their product page
          </p>
          <a
            href="/products"
            className="inline-block bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
          >
            Browse Products
          </a>
        </div>
      )}
    </div>
  )
}
