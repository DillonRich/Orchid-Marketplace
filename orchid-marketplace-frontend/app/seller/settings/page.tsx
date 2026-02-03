'use client'

import { useState, useEffect } from 'react'
import Image from 'next/image'
import { apiClient } from '@/lib/api-client'
import { useAuthStore } from '@/lib/auth-store'

interface StoreSettings {
  id: string
  name: string
  description: string
  logo?: string
  contactEmail: string
  contactPhone: string
  businessHours: string
  returnPolicy: string
  stripeConnected: boolean
  stripeAccountId?: string
}

interface ShippingSettings {
  defaultRate: number
  freeShippingThreshold: number
  processingDays: number
  availableCarriers: string[]
}

export default function SellerSettingsPage() {
  const { user } = useAuthStore()
  const [activeTab, setActiveTab] = useState<'store' | 'shipping' | 'payments'>('store')
  const [isLoading, setIsLoading] = useState(true)
  const [isSaving, setIsSaving] = useState(false)

  // Store settings
  const [storeData, setStoreData] = useState<StoreSettings>({
    id: '',
    name: '',
    description: '',
    logo: '',
    contactEmail: '',
    contactPhone: '',
    businessHours: '',
    returnPolicy: '',
    stripeConnected: false,
    stripeAccountId: ''
  })

  // Shipping settings
  const [shippingData, setShippingData] = useState<ShippingSettings>({
    defaultRate: 5.99,
    freeShippingThreshold: 50,
    processingDays: 3,
    availableCarriers: ['USPS', 'FedEx', 'UPS']
  })

  const [logoFile, setLogoFile] = useState<File | null>(null)
  const [logoPreview, setLogoPreview] = useState<string>('')

  useEffect(() => {
    if (user?.storeId) {
      fetchSettings()
    }
  }, [user])

  const fetchSettings = async () => {
    try {
      setIsLoading(true)
      // Fetch store settings
      const store = await apiClient.getStoreById(user?.storeId || '')
      setStoreData(store)
      
      // Fetch shipping settings (mock for now)
      // const shipping = await apiClient.getStoreShippingSettings(user?.storeId || '')
      // setShippingData(shipping)
    } catch (err) {
      console.error('Failed to fetch settings:', err)
    } finally {
      setIsLoading(false)
    }
  }

  const handleLogoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      setLogoFile(file)
      const reader = new FileReader()
      reader.onloadend = () => {
        setLogoPreview(reader.result as string)
      }
      reader.readAsDataURL(file)
    }
  }

  const handleSaveStore = async () => {
    try {
      setIsSaving(true)
      
      // Upload logo if changed
      if (logoFile) {
        const formData = new FormData()
        formData.append('logo', logoFile)
        // await apiClient.uploadStoreLogo(storeData.id, formData)
      }

      // Update store settings
      await apiClient.updateStore(storeData.id, storeData)
      
      alert('Store settings saved successfully!')
    } catch (err) {
      console.error('Failed to save settings:', err)
      alert('Failed to save settings')
    } finally {
      setIsSaving(false)
    }
  }

  const handleSaveShipping = async () => {
    try {
      setIsSaving(true)
      // await apiClient.updateStoreShippingSettings(user?.storeId || '', shippingData)
      alert('Shipping settings saved successfully!')
    } catch (err) {
      console.error('Failed to save shipping settings:', err)
      alert('Failed to save shipping settings')
    } finally {
      setIsSaving(false)
    }
  }

  const handleStripeConnect = () => {
    // Redirect to Stripe Connect OAuth
    const clientId = process.env.NEXT_PUBLIC_STRIPE_CONNECT_CLIENT_ID
    const redirectUri = `${window.location.origin}/seller/settings/stripe/callback`
    const stripeConnectUrl = `https://connect.stripe.com/oauth/authorize?response_type=code&client_id=${clientId}&scope=read_write&redirect_uri=${redirectUri}&state=${user?.storeId}`
    
    window.location.href = stripeConnectUrl
  }

  const handleStripeDisconnect = async () => {
    if (!confirm('Are you sure you want to disconnect your Stripe account?')) return
    
    try {
      setIsSaving(true)
      // await apiClient.disconnectStripe(user?.storeId || '')
      setStoreData(prev => ({ ...prev, stripeConnected: false, stripeAccountId: undefined }))
      alert('Stripe account disconnected')
    } catch (err) {
      console.error('Failed to disconnect Stripe:', err)
      alert('Failed to disconnect Stripe')
    } finally {
      setIsSaving(false)
    }
  }

  const tabs = [
    { id: 'store' as const, label: 'Store Profile', icon: 'storefront' },
    { id: 'shipping' as const, label: 'Shipping', icon: 'local_shipping' },
    { id: 'payments' as const, label: 'Payments', icon: 'payments' }
  ]

  if (isLoading) {
    return (
      <div className="max-w-5xl mx-auto p-6">
        <div className="animate-pulse space-y-6">
          <div className="h-8 bg-gray-200 rounded w-1/4"></div>
          <div className="h-64 bg-gray-200 rounded"></div>
        </div>
      </div>
    )
  }

  return (
    <div className="max-w-5xl mx-auto p-6 space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-deep-sage">Settings</h1>
        <p className="text-sage-green mt-1">Manage your store configuration</p>
      </div>

      {/* Tabs */}
      <div className="bg-white rounded-xl overflow-hidden">
        <div className="flex border-b border-gray-200">
          {tabs.map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`flex items-center gap-2 px-6 py-4 font-medium transition-all ${
                activeTab === tab.id
                  ? 'text-accent-peach border-b-2 border-accent-peach'
                  : 'text-sage-green hover:text-deep-sage'
              }`}
            >
              <span className="material-symbols-outlined text-sm">{tab.icon}</span>
              {tab.label}
            </button>
          ))}
        </div>

        <div className="p-6">
          {/* Store Profile Tab */}
          {activeTab === 'store' && (
            <div className="space-y-6">
              {/* Logo Upload */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Store Logo</label>
                <div className="flex items-center gap-4">
                  <div className="relative w-24 h-24 rounded-full overflow-hidden bg-gray-100 border-2 border-gray-200">
                    {(logoPreview || storeData.logo) && (
                      <Image
                        src={logoPreview || storeData.logo || ''}
                        alt="Store logo"
                        fill
                        className="object-cover"
                      />
                    )}
                    {!logoPreview && !storeData.logo && (
                      <div className="w-full h-full flex items-center justify-center">
                        <span className="material-symbols-outlined text-4xl text-gray-400">store</span>
                      </div>
                    )}
                  </div>
                  <div>
                    <input
                      type="file"
                      accept="image/*"
                      onChange={handleLogoChange}
                      className="hidden"
                      id="logo-upload"
                    />
                    <label
                      htmlFor="logo-upload"
                      className="px-4 py-2 rounded-lg border border-gray-300 text-sage-green hover:bg-gray-50 cursor-pointer inline-block"
                    >
                      Change Logo
                    </label>
                    <p className="text-xs text-sage-green mt-2">
                      Recommended: Square image, at least 200x200px
                    </p>
                  </div>
                </div>
              </div>

              {/* Store Name */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Store Name *</label>
                <input
                  type="text"
                  value={storeData.name}
                  onChange={(e) => setStoreData({ ...storeData, name: e.target.value })}
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  placeholder="My Beautiful Store"
                />
              </div>

              {/* Description */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Description</label>
                <textarea
                  value={storeData.description}
                  onChange={(e) => setStoreData({ ...storeData, description: e.target.value })}
                  rows={4}
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent resize-none"
                  placeholder="Tell customers about your store..."
                />
              </div>

              {/* Contact Email */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Contact Email *</label>
                <input
                  type="email"
                  value={storeData.contactEmail}
                  onChange={(e) => setStoreData({ ...storeData, contactEmail: e.target.value })}
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  placeholder="contact@mystore.com"
                />
              </div>

              {/* Contact Phone */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Contact Phone</label>
                <input
                  type="tel"
                  value={storeData.contactPhone}
                  onChange={(e) => setStoreData({ ...storeData, contactPhone: e.target.value })}
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  placeholder="(555) 123-4567"
                />
              </div>

              {/* Business Hours */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Business Hours</label>
                <textarea
                  value={storeData.businessHours}
                  onChange={(e) => setStoreData({ ...storeData, businessHours: e.target.value })}
                  rows={3}
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent resize-none"
                  placeholder="Monday - Friday: 9AM - 5PM&#10;Saturday: 10AM - 3PM&#10;Sunday: Closed"
                />
              </div>

              {/* Return Policy */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Return Policy</label>
                <textarea
                  value={storeData.returnPolicy}
                  onChange={(e) => setStoreData({ ...storeData, returnPolicy: e.target.value })}
                  rows={4}
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent resize-none"
                  placeholder="Describe your return and refund policy..."
                />
              </div>

              <div className="flex justify-end">
                <button
                  onClick={handleSaveStore}
                  disabled={isSaving}
                  className="px-6 py-3 rounded-lg bg-accent-peach text-white hover:bg-deep-sage transition-all disabled:opacity-50"
                >
                  {isSaving ? 'Saving...' : 'Save Changes'}
                </button>
              </div>
            </div>
          )}

          {/* Shipping Tab */}
          {activeTab === 'shipping' && (
            <div className="space-y-6">
              {/* Default Shipping Rate */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Default Shipping Rate</label>
                <div className="relative">
                  <span className="absolute left-4 top-1/2 -translate-y-1/2 text-sage-green">$</span>
                  <input
                    type="number"
                    value={shippingData.defaultRate}
                    onChange={(e) => setShippingData({ ...shippingData, defaultRate: parseFloat(e.target.value) })}
                    step="0.01"
                    min="0"
                    className="w-full pl-8 pr-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                  />
                </div>
                <p className="text-xs text-sage-green mt-1">Standard shipping cost per order</p>
              </div>

              {/* Shipping Note */}
              <div className="bg-soft-peach rounded-lg p-4">
                <p className="text-sm text-deep-sage">
                  <strong>Note:</strong> Set your shipping rates based on your costs and preferences. Customers will see your shipping fees during checkout.
                </p>
              </div>

              {/* Processing Days */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Processing Time (days)</label>
                <input
                  type="number"
                  value={shippingData.processingDays}
                  onChange={(e) => setShippingData({ ...shippingData, processingDays: parseInt(e.target.value) })}
                  min="1"
                  max="30"
                  className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:ring-2 focus:ring-accent-peach focus:border-transparent"
                />
                <p className="text-xs text-sage-green mt-1">Typical days needed to prepare orders for shipment</p>
              </div>

              {/* Available Carriers */}
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">Available Carriers</label>
                <div className="space-y-2">
                  {['USPS', 'FedEx', 'UPS', 'DHL'].map(carrier => (
                    <label key={carrier} className="flex items-center gap-2 cursor-pointer">
                      <input
                        type="checkbox"
                        checked={shippingData.availableCarriers.includes(carrier)}
                        onChange={(e) => {
                          if (e.target.checked) {
                            setShippingData({
                              ...shippingData,
                              availableCarriers: [...shippingData.availableCarriers, carrier]
                            })
                          } else {
                            setShippingData({
                              ...shippingData,
                              availableCarriers: shippingData.availableCarriers.filter(c => c !== carrier)
                            })
                          }
                        }}
                        className="w-4 h-4 rounded accent-deep-sage"
                      />
                      <span className="text-sage-green">{carrier}</span>
                    </label>
                  ))}
                </div>
              </div>

              <div className="flex justify-end">
                <button
                  onClick={handleSaveShipping}
                  disabled={isSaving}
                  className="px-6 py-3 rounded-lg bg-accent-peach text-white hover:bg-deep-sage transition-all disabled:opacity-50"
                >
                  {isSaving ? 'Saving...' : 'Save Changes'}
                </button>
              </div>
            </div>
          )}

          {/* Payments Tab */}
          {activeTab === 'payments' && (
            <div className="space-y-6">
              <div className="bg-soft-peach rounded-xl p-6">
                <div className="flex items-start gap-4">
                  <div className="p-3 bg-white rounded-lg">
                    <span className="material-symbols-outlined text-3xl text-accent-peach">credit_card</span>
                  </div>
                  <div className="flex-1">
                    <h3 className="text-lg font-semibold text-deep-sage mb-2">Stripe Connect</h3>
                    <p className="text-sage-green text-sm mb-4">
                      Connect your Stripe account to receive payments directly from customers. Stripe handles all payment processing securely.
                    </p>

                    {storeData.stripeConnected ? (
                      <div className="space-y-4">
                        <div className="flex items-center gap-2 text-green-700">
                          <span className="material-symbols-outlined">check_circle</span>
                          <span className="font-medium">Stripe account connected</span>
                        </div>
                        
                        <div className="p-4 bg-white rounded-lg">
                          <p className="text-sm text-sage-green mb-1">Account ID:</p>
                          <p className="font-mono text-deep-sage">{storeData.stripeAccountId}</p>
                        </div>

                        <div className="flex gap-3">
                          <a
                            href="https://dashboard.stripe.com"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-deep-sage text-white hover:bg-sage-green transition-all"
                          >
                            <span className="material-symbols-outlined text-sm">open_in_new</span>
                            Stripe Dashboard
                          </a>
                          <button
                            onClick={handleStripeDisconnect}
                            disabled={isSaving}
                            className="px-4 py-2 rounded-lg border border-red-300 text-red-600 hover:bg-red-50 transition-all disabled:opacity-50"
                          >
                            Disconnect
                          </button>
                        </div>
                      </div>
                    ) : (
                      <button
                        onClick={handleStripeConnect}
                        className="flex items-center gap-2 px-6 py-3 rounded-lg bg-accent-peach text-white hover:bg-deep-sage transition-all"
                      >
                        <span className="material-symbols-outlined">link</span>
                        Connect with Stripe
                      </button>
                    )}
                  </div>
                </div>
              </div>

              {storeData.stripeConnected && (
                <>
                  {/* Payout Schedule */}
                  <div className="bg-white border border-gray-200 rounded-xl p-6">
                    <h3 className="text-lg font-semibold text-deep-sage mb-4">Payout Schedule</h3>
                    <p className="text-sage-green text-sm mb-4">
                      Payouts are automatically sent to your bank account based on Stripe's payout schedule. You can view and manage your payout settings in the Stripe Dashboard.
                    </p>
                    <a
                      href="https://dashboard.stripe.com/settings/payouts"
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-accent-peach hover:text-deep-sage text-sm font-medium inline-flex items-center gap-1"
                    >
                      Manage Payout Settings
                      <span className="material-symbols-outlined text-sm">arrow_forward</span>
                    </a>
                  </div>

                  {/* Recent Payouts (Mock) */}
                  <div className="bg-white border border-gray-200 rounded-xl p-6">
                    <h3 className="text-lg font-semibold text-deep-sage mb-4">Recent Payouts</h3>
                    <div className="text-center py-8 text-sage-green">
                      <span className="material-symbols-outlined text-4xl mb-2">payments</span>
                      <p className="text-sm">View your payout history in the Stripe Dashboard</p>
                    </div>
                  </div>
                </>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
