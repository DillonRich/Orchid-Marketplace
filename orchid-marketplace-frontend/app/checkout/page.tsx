'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import Link from 'next/link'
import Image from 'next/image'
import { useAuthStore } from '@/lib/auth-store'
import { useCartStore } from '@/lib/cart-store'
import { apiClient } from '@/lib/api-client'
import AddressFormModal from '@/components/AddressFormModal'
import StripeProvider from '@/components/StripeProvider'
import StripePaymentForm from '@/components/StripePaymentForm'

export default function CheckoutPage() {
  const router = useRouter()
  const { isAuthenticated } = useAuthStore()
  const { items, itemCount, clearCart, totalPrice } = useCartStore()
  const total = totalPrice()
  
  const [step, setStep] = useState(1) // 1: Address, 2: Payment
  const [isLoading, setIsLoading] = useState(false)
  
  // Guest email state
  const [guestEmail, setGuestEmail] = useState('')
  const [guestEmailError, setGuestEmailError] = useState('')
  
  // Address state
  const [addresses, setAddresses] = useState<any[]>([])
  const [selectedAddressId, setSelectedAddressId] = useState('')
  const [showAddressForm, setShowAddressForm] = useState(false)
  const [guestAddress, setGuestAddress] = useState<any>(null) // For guest checkout
  const [sameAsBilling, setSameAsBilling] = useState(true)
  
  // Payment state
  const [paymentIntentClientSecret, setPaymentIntentClientSecret] = useState<string | null>(null)
  const [orderId, setOrderId] = useState<string | null>(null)
  const [saveCard, setSaveCard] = useState(false)
  
  // Summary
  const subtotal = total
  const tax = subtotal * 0.08
  const shipping = subtotal >= 50 ? 0 : 5.99
  const grandTotal = subtotal + tax + shipping
  
  useEffect(() => {
    if (itemCount === 0) {
      router.push('/cart')
      return
    }
    
    // Only fetch addresses for authenticated users
    if (isAuthenticated) {
      fetchAddresses()
    }
    // For guests, they'll click "Add New Address" button manually
  }, [])
  
  const fetchAddresses = async () => {
    try {
      const data = await apiClient.getAddresses()
      setAddresses(data)
      if (data.length > 0) {
        setSelectedAddressId(data[0].id)
      } else {
        setShowAddressForm(true)
      }
    } catch (error) {
      console.error('Error fetching addresses:', error)
    }
  }
  
  // const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  //   const { name, value, type, checked } = e.target
  //   setNewAddress(prev => ({
  //     ...prev,
  //     [name]: type === 'checkbox' ? checked : value,
  //   }))
  // }
  
  // const handleSaveAddress = async () => {
  //   try {
  //     const address = await apiClient.createAddress(newAddress)
  //     setAddresses(prev => [...prev, address])
  //     setSelectedAddressId(address.id)
  //     setShowAddressForm(false)
  //   } catch (error) {
  //     console.error('Error saving address:', error)
  //     alert('Failed to save address')
  //   }
  // }
  
  // Handler for AddressFormModal
  const handleModalSaveAddress = async (addressData: any) => {
    try {
      if (isAuthenticated) {
        // For authenticated users: save to backend
        const apiAddress = {
          firstName: addressData.fullName.split(' ')[0] || '',
          lastName: addressData.fullName.split(' ').slice(1).join(' ') || '',
          street: addressData.streetAddress,
          apt: '',
          city: addressData.city,
          state: addressData.stateProvince,
          zip: addressData.postalCode,
          phone: addressData.phoneNumber,
          isDefault: addressData.isDefault || false,
          saveForFuture: true,
        }
        
        const savedAddress = await apiClient.createAddress(apiAddress)
        await fetchAddresses() // Refresh the list
        setSelectedAddressId(savedAddress.id)
      } else {
        // For guest users: store locally in backend-compatible format
        const guestAddressData = {
          firstName: addressData.fullName.split(' ')[0] || '',
          lastName: addressData.fullName.split(' ').slice(1).join(' ') || '',
          street: addressData.streetAddress,
          apt: '',
          city: addressData.city,
          state: addressData.stateProvince,
          zip: addressData.postalCode,
          phone: addressData.phoneNumber,
        }
        setGuestAddress(guestAddressData)
        setSelectedAddressId('guest-address')
      }
      
      setShowAddressForm(false)
    } catch (error) {
      console.error('Error saving address:', error)
      throw error // Let modal handle the error
    }
  }
  
  const handleContinueToPayment = async () => {
    // Validate guest email if not authenticated
    if (!isAuthenticated) {
      if (!guestEmail.trim()) {
        setGuestEmailError('Email is required for order confirmation')
        return
      }
      if (!/\S+@\S+\.\S+/.test(guestEmail)) {
        setGuestEmailError('Please enter a valid email address')
        return
      }
    }
    
    // Validate address for guest users
    if (!isAuthenticated && !guestAddress) {
      alert('Please add a shipping address')
      return
    }
    
    // Validate address for authenticated users
    if (isAuthenticated && !selectedAddressId) {
      alert('Please select or add a shipping address')
      return
    }
    
    setIsLoading(true)
    
    try {
      if (isAuthenticated) {
        // Authenticated user checkout
        const order = await apiClient.checkoutCreateOrder({
          shippingAddressId: selectedAddressId,
          billingAddressId: sameAsBilling ? selectedAddressId : selectedAddressId,
          paymentMethodId: undefined, // We'll capture payment method with Stripe Elements
          createPaymentMethodForFuture: saveCard,
        })
        
        setOrderId(order.orderId)
        
        // Get payment intent client secret
        const payment = await apiClient.initiatePayment(
          order.orderId,
          `${window.location.origin}/checkout/success?order_id=${order.orderId}`,
          `${window.location.origin}/checkout/cancel?error=payment_failed`
        )
        
        setPaymentIntentClientSecret(payment.clientSecret)
        setStep(2)
      } else {
        // Guest checkout - create order with cart items
        const cartItemsData = items.map(item => ({
          productId: item.id,
          quantity: item.quantity,
          price: item.price,
          title: item.title
        }));
        
        const order = await apiClient.checkoutCreateOrderGuest({
          guestEmail: guestEmail,
          shippingAddress: {
            firstName: guestAddress.firstName,
            lastName: guestAddress.lastName,
            street: guestAddress.street,
            apt: guestAddress.apt,
            city: guestAddress.city,
            state: guestAddress.state,
            zip: guestAddress.zip,
            phone: guestAddress.phone
          },
          billingAddress: sameAsBilling ? {
            firstName: guestAddress.firstName,
            lastName: guestAddress.lastName,
            street: guestAddress.street,
            apt: guestAddress.apt,
            city: guestAddress.city,
            state: guestAddress.state,
            zip: guestAddress.zip,
            phone: guestAddress.phone
          } : null,
          cartItems: cartItemsData
        });
        
        setOrderId(order.orderId.toString());
        
        // Create payment intent
        const payment = await apiClient.createPaymentIntent({
          amount: order.totalAmount,
          currency: 'usd',
          orderId: order.orderId,
          guestEmail: guestEmail
        });
        
        setPaymentIntentClientSecret(payment.clientSecret);
        setStep(2);
      }
    } catch (error: any) {
      console.error('Error creating order:', error)
      alert(error.response?.data?.message || 'Failed to proceed to payment. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }
  
  const handlePaymentSuccess = (paymentIntentId: string) => {
    clearCart()
    router.push(`/checkout/success?order_id=${orderId}&session_id=${paymentIntentId}`)
  }
  
  const handlePaymentError = (error: string) => {
    console.error('Payment error:', error)
    // Keep user on payment page to retry
  }
  
  return (
    <div className="min-h-screen bg-warm-cream">
      {/* Simplified Header */}
      <header className="bg-white border-b border-gray-200">
        <div className="container mx-auto px-4 lg:px-24 py-6">
          <Link href="/" className="font-playfair text-3xl font-bold text-deep-sage">
            Orchidillo
          </Link>
        </div>
      </header>
      
      <main className="container mx-auto px-4 lg:px-24 py-12">
        {/* Breadcrumb */}
        <nav className="mb-8">
          <ol className="flex items-center space-x-2 text-sm text-sage-green">
            <li>
              <Link href="/cart" className="hover:text-deep-sage transition-colors">
                Cart
              </Link>
            </li>
            <li>
              <span className="material-symbols-outlined text-sm">chevron_right</span>
            </li>
            <li className={step === 1 ? 'text-deep-sage font-medium' : ''}>
              Shipping
            </li>
            <li>
              <span className="material-symbols-outlined text-sm">chevron_right</span>
            </li>
            <li className={step === 2 ? 'text-deep-sage font-medium' : ''}>
              Payment
            </li>
          </ol>
        </nav>
        
        {/* Progress Steps */}
        <div className="flex items-center justify-center mb-12">
          <div className="flex items-center gap-4">
            <div className={`flex items-center justify-center w-10 h-10 rounded-full font-bold ${
              step >= 1 ? 'bg-deep-sage text-white' : 'bg-gray-200 text-sage-green'
            }`}>
              1
            </div>
            <span className={`font-medium ${step >= 1 ? 'text-deep-sage' : 'text-sage-green'}`}>
              Shipping
            </span>
            <div className={`w-16 h-0.5 ${step >= 2 ? 'bg-deep-sage' : 'bg-gray-300'}`}></div>
            <div className={`flex items-center justify-center w-10 h-10 rounded-full font-bold ${
              step >= 2 ? 'bg-deep-sage text-white' : 'bg-gray-200 text-sage-green'
            }`}>
              2
            </div>
            <span className={`font-medium ${step >= 2 ? 'text-deep-sage' : 'text-sage-green'}`}>
              Payment
            </span>
          </div>
        </div>
        
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Main Content */}
          <div className="lg:col-span-2">
            {/* Step 1: Shipping Address */}
            {step === 1 && (
              <div className="bg-white rounded-xl p-8">
                <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-6">
                  Shipping Address
                </h2>
                
                {/* Guest Email Input */}
                {!isAuthenticated && (
                  <div className="mb-8 p-4 bg-soft-peach border border-accent-peach rounded-lg">
                    <div className="flex items-start gap-2 mb-3">
                      <span className="material-symbols-outlined text-accent-peach text-xl">info</span>
                      <div>
                        <p className="text-sm font-medium text-deep-sage mb-1">
                          Checking out as a guest
                        </p>
                        <p className="text-xs text-sage-green">
                          We'll send your order confirmation to this email. You can{' '}
                          <Link href="/login?redirect=/checkout" className="text-accent-peach hover:underline">
                            sign in
                          </Link>{' '}
                          or{' '}
                          <Link href="/register?redirect=/checkout" className="text-accent-peach hover:underline">
                            create an account
                          </Link>{' '}
                          to save your information.
                        </p>
                      </div>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-deep-sage mb-2">
                        Email Address *
                      </label>
                      <input
                        type="email"
                        value={guestEmail}
                        onChange={(e) => {
                          setGuestEmail(e.target.value)
                          setGuestEmailError('')
                        }}
                        placeholder="your.email@example.com"
                        className={`w-full px-4 py-3 rounded-lg border ${
                          guestEmailError ? 'border-red-500' : 'border-gray-300'
                        } bg-white focus:outline-none focus:ring-2 focus:ring-accent-peach`}
                      />
                      {guestEmailError && (
                        <p className="mt-1 text-xs text-red-600 flex items-center gap-1">
                          <span className="material-symbols-outlined text-sm">error</span>
                          {guestEmailError}
                        </p>
                      )}
                    </div>
                  </div>
                )}
                
                {/* Existing Addresses */}
                {addresses.length > 0 && !showAddressForm && (
                  <div className="space-y-4 mb-6">
                    {addresses.map(address => (
                      <label 
                        key={address.id}
                        className={`block p-4 border-2 rounded-lg cursor-pointer transition-all ${
                          selectedAddressId === address.id 
                            ? 'border-accent-peach bg-soft-peach' 
                            : 'border-gray-300 hover:border-sage-green'
                        }`}
                      >
                        <input
                          type="radio"
                          name="address"
                          value={address.id}
                          checked={selectedAddressId === address.id}
                          onChange={(e) => setSelectedAddressId(e.target.value)}
                          className="sr-only"
                        />
                        <div className="flex items-start gap-3">
                          <div className={`w-5 h-5 rounded-full border-2 flex items-center justify-center flex-shrink-0 mt-0.5 ${
                            selectedAddressId === address.id ? 'border-accent-peach' : 'border-gray-300'
                          }`}>
                            {selectedAddressId === address.id && (
                              <div className="w-3 h-3 rounded-full bg-accent-peach"></div>
                            )}
                          </div>
                          <div className="flex-1">
                            <p className="font-medium text-deep-sage mb-1">
                              {address.firstName} {address.lastName}
                            </p>
                            <p className="text-sm text-sage-green">
                              {address.street}
                              {address.apt && `, ${address.apt}`}
                            </p>
                            <p className="text-sm text-sage-green">
                              {address.city}, {address.state} {address.zip}
                            </p>
                            {address.phone && (
                              <p className="text-sm text-sage-green">{address.phone}</p>
                            )}
                            {address.isDefault && (
                              <span className="inline-block mt-2 text-xs bg-soft-peach text-deep-sage px-2 py-1 rounded">
                                Default
                              </span>
                            )}
                          </div>
                        </div>
                      </label>
                    ))}
                  </div>
                )}
                
                {/* Guest Address Display */}
                {!isAuthenticated && guestAddress && (
                  <div className="mb-6">
                    <div className="block p-4 border-2 border-accent-peach bg-soft-peach rounded-lg">
                      <div className="flex items-start gap-3">
                        <div className="w-5 h-5 rounded-full border-2 border-accent-peach flex items-center justify-center flex-shrink-0 mt-0.5">
                          <div className="w-3 h-3 rounded-full bg-accent-peach"></div>
                        </div>
                        <div className="flex-1">
                          <p className="font-medium text-deep-sage mb-1">
                            {guestAddress.fullName}
                          </p>
                          <p className="text-sm text-sage-green">
                            {guestAddress.streetAddress}
                          </p>
                          <p className="text-sm text-sage-green">
                            {guestAddress.city}, {guestAddress.stateProvince} {guestAddress.postalCode}
                          </p>
                          <p className="text-sm text-sage-green">{guestAddress.country}</p>
                          {guestAddress.phoneNumber && (
                            <p className="text-sm text-sage-green">{guestAddress.phoneNumber}</p>
                          )}
                        </div>
                        <button
                          onClick={() => setShowAddressForm(true)}
                          className="text-sage-green hover:text-accent-peach transition-colors text-sm"
                        >
                          Edit
                        </button>
                      </div>
                    </div>
                  </div>
                )}
                
                {/* Add New Address Button - Show for guests without address or authenticated users */}
                {(!isAuthenticated && !guestAddress) && (
                  <button
                    onClick={() => setShowAddressForm(true)}
                    className="w-full py-3 border-2 border-dashed border-sage-green text-sage-green rounded-lg hover:border-accent-peach hover:text-accent-peach transition-all flex items-center justify-center gap-2"
                  >
                    <span className="material-symbols-outlined">add</span>
                    <span>Add New Address</span>
                  </button>
                )}
                
                {(isAuthenticated && addresses.length === 0) && (
                  <button
                    onClick={() => setShowAddressForm(true)}
                    className="w-full py-3 border-2 border-dashed border-sage-green text-sage-green rounded-lg hover:border-accent-peach hover:text-accent-peach transition-all flex items-center justify-center gap-2"
                  >
                    <span className="material-symbols-outlined">add</span>
                    <span>Add New Address</span>
                  </button>
                )}
                
                {(isAuthenticated && addresses.length > 0) && (
                  <button
                    onClick={() => setShowAddressForm(true)}
                    className="w-full py-3 border-2 border-dashed border-sage-green text-sage-green rounded-lg hover:border-accent-peach hover:text-accent-peach transition-all flex items-center justify-center gap-2 mt-4"
                  >
                    <span className="material-symbols-outlined">add</span>
                    <span>Add Another Address</span>
                  </button>
                )}
                
                {/* Continue Button */}
                <button
                  onClick={handleContinueToPayment}
                  className="w-full mt-8 bg-deep-sage text-white py-4 px-6 rounded-full font-medium hover:bg-sage-green transition-all"
                >
                  Continue to Payment
                </button>
              </div>
            )}
            
            {/* Step 2: Payment */}
            {step === 2 && (
              <div className="bg-white rounded-xl p-8">
                <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-6">
                  Payment Method
                </h2>
                
                {/* Billing Address */}
                <div className="mb-8">
                  <h3 className="font-medium text-deep-sage mb-4">Billing Address</h3>
                  <label className="flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      checked={sameAsBilling}
                      onChange={(e) => setSameAsBilling(e.target.checked)}
                      className="w-4 h-4 rounded border-gray-300 text-accent-peach focus:ring-accent-peach"
                    />
                    <span className="ml-2 text-sage-green">
                      Same as shipping address
                    </span>
                  </label>
                </div>
                
                {/* Payment Method */}
                <div className="mb-8">
                  <h3 className="font-medium text-deep-sage mb-4">Card Information</h3>
                  
                  {paymentIntentClientSecret ? (
                    <StripeProvider clientSecret={paymentIntentClientSecret}>
                      <StripePaymentForm
                        amount={grandTotal}
                        onSuccess={handlePaymentSuccess}
                        onError={handlePaymentError}
                      />
                    </StripeProvider>
                  ) : !isAuthenticated ? (
                    <div className="bg-soft-peach border border-accent-peach rounded-lg p-6">
                      <div className="flex items-start gap-3 mb-4">
                        <span className="material-symbols-outlined text-accent-peach">info</span>
                        <div>
                          <h4 className="font-medium text-deep-sage mb-1">Guest Checkout Active</h4>
                          <p className="text-sm text-sage-green mb-3">
                            Your order details have been saved. Payment processing will be enabled once the backend server is running.
                          </p>
                          <div className="space-y-2 text-sm">
                            <p><strong>Order ID:</strong> {orderId}</p>
                            <p><strong>Email:</strong> {guestEmail}</p>
                            <p><strong>Total:</strong> ${grandTotal.toFixed(2)}</p>
                          </div>
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div className="text-center py-8">
                      <div className="w-12 h-12 border-4 border-accent-peach border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
                      <p className="text-sage-green">Loading payment form...</p>
                    </div>
                  )}
                  
                  <label className="flex items-center cursor-pointer mt-4">
                    <input
                      type="checkbox"
                      checked={saveCard}
                      onChange={(e) => setSaveCard(e.target.checked)}
                      className="w-4 h-4 rounded border-gray-300 text-accent-peach focus:ring-accent-peach"
                    />
                    <span className="ml-2 text-sm text-sage-green">
                      Save card for future purchases
                    </span>
                  </label>
                </div>
                
                {/* Back Button */}
                <button
                  onClick={() => setStep(1)}
                  disabled={isLoading}
                  className="px-8 py-3 border border-gray-300 text-sage-green rounded-full font-medium hover:bg-soft-peach transition-all disabled:opacity-50"
                >
                  ← Back to Shipping
                </button>
              </div>
            )}
          </div>
          
          {/* Order Summary Sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-xl p-6 sticky top-4">
              <h3 className="font-playfair text-xl font-bold text-deep-sage mb-4">
                Order Summary
              </h3>
              
              {/* Items */}
              <div className="space-y-3 mb-6 max-h-64 overflow-y-auto">
                {items.map((item: any) => (
                  <div key={item.id} className="flex gap-3">
                    <div className="relative w-16 h-16 flex-shrink-0 rounded-lg overflow-hidden bg-gray-100">
                      <Image
                        src={item.imageUrl || '/placeholder.jpg'}
                        alt={item.title}
                        fill
                        className="object-cover"
                      />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm text-deep-sage font-medium truncate">
                        {item.title}
                      </p>
                      <p className="text-sm text-sage-green">
                        Qty: {item.quantity}
                      </p>
                      <p className="text-sm font-medium text-deep-sage">
                        ${(item.price * item.quantity).toFixed(2)}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
              
              {/* Totals */}
              <div className="space-y-3 mb-6 pb-6 border-b border-gray-200">
                <div className="flex justify-between text-sage-green text-sm">
                  <span>Subtotal</span>
                  <span>${(subtotal || 0).toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sage-green text-sm">
                  <span>Tax</span>
                  <span>${(tax || 0).toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sage-green text-sm">
                  <span>Shipping</span>
                  <span>{shipping === 0 ? 'FREE' : `$${shipping.toFixed(2)}`}</span>
                </div>
              </div>
              
              <div className="flex justify-between mb-6">
                <span className="font-playfair text-xl font-bold text-deep-sage">
                  Total
                </span>
                <span className="font-playfair text-2xl font-bold text-deep-sage">
                  ${grandTotal.toFixed(2)}
                </span>
              </div>
              
              {/* Trust Badges */}
              <div className="space-y-2 text-xs text-sage-green">
                <div className="flex items-center gap-2">
                  <span className="material-symbols-outlined text-sm">lock</span>
                  <span>Secure SSL encryption</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className="material-symbols-outlined text-sm">verified_user</span>
                  <span>PCI DSS compliant</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
      
      {/* Address Form Modal */}
      <AddressFormModal
        isOpen={showAddressForm}
        onClose={() => setShowAddressForm(false)}
        onSave={handleModalSaveAddress}
        mode="add"
      />
    </div>
  )
}
