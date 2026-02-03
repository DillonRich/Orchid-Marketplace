"use client"

import { useState } from 'react'

export default function SellerMessagesPage() {
  const [conversations, setConversations] = useState([
    { id: '1', customer: 'John Doe', lastMessage: 'Is this orchid suitable for beginners?', time: '2 hours ago', unread: true },
    { id: '2', customer: 'Jane Smith', lastMessage: 'Thank you for the quick shipping!', time: '1 day ago', unread: false },
    { id: '3', customer: 'Mike Johnson', lastMessage: 'Do you offer bulk discounts?', time: '2 days ago', unread: false },
  ])
  
  const [selectedConversation, setSelectedConversation] = useState<string | null>(null)
  
  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-serif font-bold text-deep-sage mb-2">Messages</h1>
        <p className="text-sage-green">Communicate with your customers</p>
      </div>
      
      {/* Messages Interface */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden grid grid-cols-3 h-[600px]">
        {/* Conversations List */}
        <div className="col-span-1 border-r border-gray-200 overflow-y-auto">
          <div className="p-4 border-b border-gray-200">
            <input
              type="text"
              placeholder="Search messages..."
              className="w-full px-4 py-2 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          {conversations.map((conv) => (
            <button
              key={conv.id}
              onClick={() => setSelectedConversation(conv.id)}
              className={`w-full p-4 border-b border-gray-200 hover:bg-warm-cream transition-colors text-left ${
                selectedConversation === conv.id ? 'bg-soft-peach' : ''
              }`}
            >
              <div className="flex items-center justify-between mb-2">
                <span className="font-medium text-deep-sage">{conv.customer}</span>
                {conv.unread && (
                  <span className="w-2 h-2 bg-accent-peach rounded-full"></span>
                )}
              </div>
              <p className="text-sm text-sage-green truncate">{conv.lastMessage}</p>
              <p className="text-xs text-gray-400 mt-1">{conv.time}</p>
            </button>
          ))}
        </div>
        
        {/* Message View */}
        <div className="col-span-2 flex flex-col">
          {selectedConversation ? (
            <>
              {/* Conversation Header */}
              <div className="p-4 border-b border-gray-200">
                <h3 className="font-medium text-deep-sage">
                  {conversations.find(c => c.id === selectedConversation)?.customer}
                </h3>
              </div>
              
              {/* Messages */}
              <div className="flex-1 p-4 overflow-y-auto space-y-4">
                <div className="flex justify-start">
                  <div className="bg-soft-peach rounded-2xl rounded-tl-none px-4 py-3 max-w-[70%]">
                    <p className="text-sm text-deep-sage">
                      {conversations.find(c => c.id === selectedConversation)?.lastMessage}
                    </p>
                    <p className="text-xs text-sage-green mt-1">
                      {conversations.find(c => c.id === selectedConversation)?.time}
                    </p>
                  </div>
                </div>
              </div>
              
              {/* Message Input */}
              <div className="p-4 border-t border-gray-200">
                <div className="flex gap-3">
                  <input
                    type="text"
                    placeholder="Type your message..."
                    className="flex-1 px-4 py-3 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-accent-peach"
                  />
                  <button className="bg-deep-sage text-white px-6 py-3 rounded-full hover:bg-sage-green transition-all flex items-center gap-2">
                    <span className="material-symbols-outlined">send</span>
                    Send
                  </button>
                </div>
              </div>
            </>
          ) : (
            <div className="flex-1 flex items-center justify-center">
              <div className="text-center">
                <span className="material-symbols-outlined text-6xl text-gray-300">mail</span>
                <p className="text-sage-green mt-4">Select a conversation to view messages</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
