'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import Header from '@/components/Header'
import Footer from '@/components/Footer'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'

interface Conversation {
  id: number
  otherParticipant: {
    id: number
    name: string
    avatar?: string
  }
  lastMessage: {
    content: string
    createdAt: string
    senderId: number
  }
  unreadCount: number
  relatedOrderId?: number
}

interface Message {
  id: number
  content: string
  senderId: number
  senderName: string
  createdAt: string
  read: boolean
}

export default function MessagesPage() {
  const router = useRouter()
  const { isAuthenticated, user } = useAuthStore()
  
  const [conversations, setConversations] = useState<Conversation[]>([])
  const [selectedConversation, setSelectedConversation] = useState<number | null>(null)
  const [messages, setMessages] = useState<Message[]>([])
  const [newMessage, setNewMessage] = useState('')
  const [loading, setLoading] = useState(true)
  const [sending, setSending] = useState(false)
  
  useEffect(() => {
    if (!isAuthenticated) {
      router.push('/login?redirect=/messages')
      return
    }
    fetchConversations()
  }, [isAuthenticated])
  
  useEffect(() => {
    if (selectedConversation) {
      fetchMessages(selectedConversation)
    }
  }, [selectedConversation])
  
  const fetchConversations = async () => {
    try {
      setLoading(true)
      const data = await apiClient.getMessages()
      
      // Group messages by conversation
      // This is simplified - backend should return formatted conversations
      setConversations(data || [])
    } catch (error) {
      console.error('Failed to fetch conversations:', error)
    } finally {
      setLoading(false)
    }
  }
  
  const fetchMessages = async (conversationId: number) => {
    try {
      const data = await apiClient.getMessages(conversationId.toString())
      setMessages(data || [])
    } catch (error) {
      console.error('Failed to fetch messages:', error)
    }
  }
  
  const handleSendMessage = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!newMessage.trim() || !selectedConversation) return
    
    try {
      setSending(true)
      await apiClient.sendMessage({
        conversationId: selectedConversation,
        content: newMessage,
        recipientId: conversations.find(c => c.id === selectedConversation)?.otherParticipant.id
      })
      
      setNewMessage('')
      fetchMessages(selectedConversation)
      fetchConversations() // Refresh conversation list
    } catch (error) {
      console.error('Failed to send message:', error)
    } finally {
      setSending(false)
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
  
  const formatMessageTime = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit' })
  }
  
  const selectedConversationData = conversations.find(c => c.id === selectedConversation)
  
  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1400px] mx-auto px-4 py-8">
          
          <div className="bg-white rounded-xl overflow-hidden shadow-lg" style={{ height: 'calc(100vh - 250px)' }}>
            <div className="grid grid-cols-12 h-full">
              
              {/* Conversations List */}
              <div className="col-span-4 border-r border-gray-200 flex flex-col">
                <div className="p-6 border-b border-gray-200">
                  <h1 className="font-playfair text-3xl font-bold text-deep-sage">
                    Messages
                  </h1>
                </div>
                
                <div className="flex-1 overflow-y-auto">
                  {loading ? (
                    <div className="flex justify-center items-center h-64">
                      <span className="material-symbols-outlined text-6xl text-sage-green animate-pulse">
                        mail
                      </span>
                    </div>
                  ) : conversations.length === 0 ? (
                    <div className="p-8 text-center">
                      <span className="material-symbols-outlined text-6xl text-sage-green mb-4 block">
                        inbox
                      </span>
                      <p className="text-sage-green">No conversations yet</p>
                      <p className="text-sm text-sage-green mt-2">
                        Start shopping to connect with sellers
                      </p>
                    </div>
                  ) : (
                    conversations.map(conversation => (
                      <div
                        key={conversation.id}
                        onClick={() => setSelectedConversation(conversation.id)}
                        className={`p-4 border-b border-gray-200 cursor-pointer hover:bg-soft-peach transition-all ${
                          selectedConversation === conversation.id ? 'bg-soft-peach' : ''
                        }`}
                      >
                        <div className="flex items-center gap-3">
                          <div className="w-12 h-12 bg-sage-green rounded-full flex items-center justify-center text-white font-bold flex-shrink-0">
                            {conversation.otherParticipant.name.charAt(0).toUpperCase()}
                          </div>
                          
                          <div className="flex-1 min-w-0">
                            <div className="flex items-center justify-between mb-1">
                              <h3 className="font-medium text-deep-sage truncate">
                                {conversation.otherParticipant.name}
                              </h3>
                              <span className="text-xs text-sage-green whitespace-nowrap ml-2">
                                {formatTimeAgo(conversation.lastMessage.createdAt)}
                              </span>
                            </div>
                            <p className="text-sm text-sage-green truncate">
                              {conversation.lastMessage.senderId === Number(user?.id) ? 'You: ' : ''}
                              {conversation.lastMessage.content}
                            </p>
                            {conversation.relatedOrderId && (
                              <p className="text-xs text-accent-peach mt-1">
                                Order #{conversation.relatedOrderId}
                              </p>
                            )}
                          </div>
                          
                          {conversation.unreadCount > 0 && (
                            <div className="w-6 h-6 bg-accent-peach text-white text-xs font-bold rounded-full flex items-center justify-center flex-shrink-0">
                              {conversation.unreadCount}
                            </div>
                          )}
                        </div>
                      </div>
                    ))
                  )}
                </div>
              </div>
              
              {/* Message Thread */}
              <div className="col-span-8 flex flex-col">
                {selectedConversation ? (
                  <>
                    {/* Thread Header */}
                    <div className="p-6 border-b border-gray-200 flex items-center justify-between">
                      <div className="flex items-center gap-3">
                        <div className="w-12 h-12 bg-sage-green rounded-full flex items-center justify-center text-white font-bold">
                          {selectedConversationData?.otherParticipant.name.charAt(0).toUpperCase()}
                        </div>
                        <div>
                          <h2 className="font-medium text-lg text-deep-sage">
                            {selectedConversationData?.otherParticipant.name}
                          </h2>
                          {selectedConversationData?.relatedOrderId && (
                            <p className="text-sm text-sage-green">
                              Order #{selectedConversationData.relatedOrderId}
                            </p>
                          )}
                        </div>
                      </div>
                      
                      {selectedConversationData?.relatedOrderId && (
                        <button
                          onClick={() => router.push(`/account/orders/${selectedConversationData.relatedOrderId}`)}
                          className="bg-soft-peach hover:bg-accent-peach text-deep-sage px-6 py-2 rounded-full font-medium transition-all"
                        >
                          View Order
                        </button>
                      )}
                    </div>
                    
                    {/* Messages */}
                    <div className="flex-1 overflow-y-auto p-6 space-y-4">
                      {messages.map(message => (
                        <div
                          key={message.id}
                          className={`flex ${message.senderId === Number(user?.id) ? 'justify-end' : 'justify-start'}`}
                        >
                          <div className={`max-w-[70%] ${message.senderId === Number(user?.id) ? 'order-2' : 'order-1'}`}>
                            <div
                              className={`px-4 py-3 rounded-2xl ${
                                message.senderId === Number(user?.id)
                                  ? 'bg-deep-sage text-white rounded-br-sm'
                                  : 'bg-soft-peach text-deep-sage rounded-bl-sm'
                              }`}
                            >
                              <p className="leading-relaxed">{message.content}</p>
                            </div>
                            <p className={`text-xs text-sage-green mt-1 ${message.senderId === Number(user?.id) ? 'text-right' : 'text-left'}`}>
                              {formatMessageTime(message.createdAt)}
                            </p>
                          </div>
                        </div>
                      ))}
                    </div>
                    
                    {/* Message Input */}
                    <form onSubmit={handleSendMessage} className="p-6 border-t border-gray-200">
                      <div className="flex gap-3">
                        <input
                          type="text"
                          value={newMessage}
                          onChange={(e) => setNewMessage(e.target.value)}
                          placeholder="Type your message..."
                          className="flex-1 px-4 py-3 border-2 border-sage-green rounded-full focus:outline-none focus:border-deep-sage"
                          disabled={sending}
                        />
                        <button
                          type="submit"
                          disabled={!newMessage.trim() || sending}
                          className="w-12 h-12 bg-deep-sage hover:bg-sage-green text-white rounded-full flex items-center justify-center disabled:bg-gray-300 disabled:cursor-not-allowed transition-all"
                        >
                          <span className="material-symbols-outlined">send</span>
                        </button>
                      </div>
                    </form>
                  </>
                ) : (
                  <div className="flex-1 flex items-center justify-center">
                    <div className="text-center">
                      <span className="material-symbols-outlined text-8xl text-sage-green mb-4 block">
                        forum
                      </span>
                      <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
                        Select a conversation
                      </h2>
                      <p className="text-sage-green">
                        Choose a conversation from the left to start messaging
                      </p>
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
