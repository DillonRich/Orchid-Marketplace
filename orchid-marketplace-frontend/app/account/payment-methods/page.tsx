'use client'

import { useState, useEffect } from 'react'

export default function PaymentMethodsPage() {
  const [cards, setCards] = useState<any[]>([])
  const [showAddCard, setShowAddCard] = useState(false)
  
  useEffect(() => {
    // Mock payment methods
    setCards([
      {
        id: '1',
        brand: 'Visa',
        last4: '4242',
        expMonth: 12,
        expYear: 2026,
        isDefault: true,
      },
      {
        id: '2',
        brand: 'Mastercard',
        last4: '5555',
        expMonth: 9,
        expYear: 2027,
        isDefault: false,
      },
    ])
  }, [])
  
  const handleSetDefault = (id: string) => {
    setCards(prev => prev.map(card => ({
      ...card,
      isDefault: card.id === id
    })))
  }
  
  const handleDelete = (id: string) => {
    if (confirm('Are you sure you want to remove this payment method?')) {
      setCards(prev => prev.filter(card => card.id !== id))
    }
  }
  
  const getBrandIcon = (brand: string) => {
    switch (brand.toLowerCase()) {
      case 'visa':
        return '💳'
      case 'mastercard':
        return '💳'
      case 'amex':
        return '💳'
      default:
        return '💳'
    }
  }
  
  return (
    <div className="space-y-8">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
            Payment Methods
          </h1>
          <p className="text-sage-green">
            Manage your saved payment methods
          </p>
        </div>
        
        <button
          onClick={() => setShowAddCard(true)}
          className="bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all flex items-center gap-2"
        >
          <span className="material-symbols-outlined">add</span>
          Add Card
        </button>
      </div>
      
      {/* Add Card Modal Placeholder */}
      {showAddCard && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl p-8 max-w-md w-full">
            <div className="flex items-center justify-between mb-6">
              <h2 className="font-medium text-deep-sage text-xl">Add Payment Method</h2>
              <button
                onClick={() => setShowAddCard(false)}
                className="text-sage-green hover:text-deep-sage"
              >
                <span className="material-symbols-outlined">close</span>
              </button>
            </div>
            
            <div className="bg-soft-peach rounded-lg p-8 text-center mb-6">
              <span className="material-symbols-outlined text-6xl text-deep-sage mb-4">
                credit_card
              </span>
              <p className="text-sage-green">
                Stripe payment form will be integrated here
              </p>
            </div>
            
            <div className="flex gap-3">
              <button
                onClick={() => setShowAddCard(false)}
                className="flex-1 bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all"
              >
                Add Card
              </button>
              <button
                onClick={() => setShowAddCard(false)}
                className="flex-1 px-6 py-3 border border-gray-300 text-sage-green rounded-full font-medium hover:bg-soft-peach transition-all"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
      
      {/* Security Notice */}
      <div className="bg-blue-50 border border-blue-200 rounded-xl p-6">
        <div className="flex gap-3">
          <span className="material-symbols-outlined text-blue-600">
            shield
          </span>
          <div>
            <h3 className="font-medium text-deep-sage mb-1">
              Your payment information is secure
            </h3>
            <p className="text-sm text-sage-green">
              All payment information is encrypted and processed securely through Stripe. We never store your full card details.
            </p>
          </div>
        </div>
      </div>
      
      {/* Cards Grid */}
      {cards.length > 0 ? (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {cards.map(card => (
            <div
              key={card.id}
              className="bg-gradient-to-br from-deep-sage to-sage-green text-white rounded-xl p-6 relative hover:shadow-lg transition-all"
            >
              {card.isDefault && (
                <span className="absolute top-4 right-4 bg-white text-deep-sage text-xs px-3 py-1 rounded-full font-medium">
                  Default
                </span>
              )}
              
              <div className="mb-6">
                <div className="text-4xl mb-4">{getBrandIcon(card.brand)}</div>
                <p className="text-2xl font-mono tracking-wider mb-2">
                  •••• •••• •••• {card.last4}
                </p>
                <p className="text-sm opacity-90">
                  Expires {String(card.expMonth).padStart(2, '0')}/{card.expYear}
                </p>
              </div>
              
              <div className="flex gap-2">
                {!card.isDefault && (
                  <>
                    <button
                      onClick={() => handleSetDefault(card.id)}
                      className="flex-1 py-2 px-4 bg-white/20 backdrop-blur-sm text-white rounded-full text-sm font-medium hover:bg-white/30 transition-all"
                    >
                      Set as Default
                    </button>
                    <button
                      onClick={() => handleDelete(card.id)}
                      className="p-2 bg-white/20 backdrop-blur-sm hover:bg-red-500 rounded-full transition-all"
                    >
                      <span className="material-symbols-outlined">delete</span>
                    </button>
                  </>
                )}
                {card.isDefault && (
                  <button
                    disabled
                    className="flex-1 py-2 px-4 bg-white/20 backdrop-blur-sm text-white rounded-full text-sm font-medium opacity-60 cursor-not-allowed"
                  >
                    Default Card
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            credit_card
          </span>
          <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
            No payment methods saved
          </h3>
          <p className="text-sage-green mb-6">
            Add a payment method to speed up checkout
          </p>
          <button
            onClick={() => setShowAddCard(true)}
            className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
          >
            Add Payment Method
          </button>
        </div>
      )}
    </div>
  )
}
