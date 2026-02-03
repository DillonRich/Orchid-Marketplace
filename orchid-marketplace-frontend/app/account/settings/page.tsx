'use client'

import { useState } from 'react'

export default function SettingsPage() {
  const [emailNotifs, setEmailNotifs] = useState({
    orderUpdates: true,
    shipping: true,
    promotional: false,
    messages: true,
    priceDrops: true,
  })
  
  const [inAppNotifs, setInAppNotifs] = useState({
    orderUpdates: true,
    shipping: true,
    promotional: true,
    messages: true,
    priceDrops: false,
  })
  
  const [privacy, setPrivacy] = useState({
    profileVisible: true,
    purchaseHistoryVisible: false,
    personalizedRecommendations: true,
  })
  
  const [isSaving, setIsSaving] = useState(false)
  
  const handleSave = async () => {
    setIsSaving(true)
    try {
      // API call to save settings
      await new Promise(resolve => setTimeout(resolve, 1000))
      alert('Settings saved successfully!')
    } catch (error) {
      console.error('Error saving settings:', error)
    } finally {
      setIsSaving(false)
    }
  }
  
  return (
    <div className="space-y-8">
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Settings
        </h1>
        <p className="text-sage-green">
          Manage your account preferences
        </p>
      </div>
      
      {/* Email Notifications */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">
          Email Notifications
        </h2>
        <div className="space-y-4">
          {Object.entries(emailNotifs).map(([key, value]) => (
            <div key={key} className="flex items-center justify-between py-3 border-b border-gray-200 last:border-0">
              <div>
                <p className="font-medium text-deep-sage">
                  {key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}
                </p>
                <p className="text-sm text-sage-green">
                  {key === 'orderUpdates' && 'Receive emails about your order status changes'}
                  {key === 'shipping' && 'Get notified when your items are shipped'}
                  {key === 'promotional' && 'Receive special offers and promotions'}
                  {key === 'messages' && 'Get notified of new messages from sellers'}
                  {key === 'priceDrops' && 'Alert when items in your wishlist go on sale'}
                </p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  checked={value}
                  onChange={(e) => setEmailNotifs(prev => ({ ...prev, [key]: e.target.checked }))}
                  className="sr-only peer"
                />
                <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-accent-peach rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-deep-sage"></div>
              </label>
            </div>
          ))}
        </div>
      </div>
      
      {/* In-App Notifications */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">
          In-App Notifications
        </h2>
        <div className="space-y-4">
          {Object.entries(inAppNotifs).map(([key, value]) => (
            <div key={key} className="flex items-center justify-between py-3 border-b border-gray-200 last:border-0">
              <div>
                <p className="font-medium text-deep-sage">
                  {key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}
                </p>
                <p className="text-sm text-sage-green">
                  {key === 'orderUpdates' && 'See in-app notifications for order status'}
                  {key === 'shipping' && 'Get in-app shipping notifications'}
                  {key === 'promotional' && 'See promotional offers in notifications'}
                  {key === 'messages' && 'Get in-app message notifications'}
                  {key === 'priceDrops' && 'In-app alerts for wishlist price drops'}
                </p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  checked={value}
                  onChange={(e) => setInAppNotifs(prev => ({ ...prev, [key]: e.target.checked }))}
                  className="sr-only peer"
                />
                <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-accent-peach rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-deep-sage"></div>
              </label>
            </div>
          ))}
        </div>
      </div>
      
      {/* Privacy Settings */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">
          Privacy Settings
        </h2>
        <div className="space-y-4">
          {Object.entries(privacy).map(([key, value]) => (
            <div key={key} className="flex items-center justify-between py-3 border-b border-gray-200 last:border-0">
              <div>
                <p className="font-medium text-deep-sage">
                  {key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}
                </p>
                <p className="text-sm text-sage-green">
                  {key === 'profileVisible' && 'Allow other users to view your public profile'}
                  {key === 'purchaseHistoryVisible' && 'Show your purchase history on your profile'}
                  {key === 'personalizedRecommendations' && 'Use your activity to personalize product recommendations'}
                </p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  checked={value}
                  onChange={(e) => setPrivacy(prev => ({ ...prev, [key]: e.target.checked }))}
                  className="sr-only peer"
                />
                <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-accent-peach rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-deep-sage"></div>
              </label>
            </div>
          ))}
        </div>
      </div>
      
      {/* Language & Region */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">
          Language & Region
        </h2>
        <div className="space-y-6 max-w-md">
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Language
            </label>
            <select className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach">
              <option value="en">English</option>
              <option value="es">Español</option>
              <option value="fr">Français</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Currency
            </label>
            <select className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach">
              <option value="USD">USD ($)</option>
              <option value="EUR">EUR (€)</option>
              <option value="GBP">GBP (£)</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Timezone
            </label>
            <select className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach">
              <option value="EST">Eastern Time (ET)</option>
              <option value="CST">Central Time (CT)</option>
              <option value="MST">Mountain Time (MT)</option>
              <option value="PST">Pacific Time (PT)</option>
            </select>
          </div>
        </div>
      </div>
      
      {/* Seller Account */}
      <div className="bg-soft-peach rounded-xl p-8">
        <div className="flex items-start gap-4">
          <span className="material-symbols-outlined text-4xl text-deep-sage">
            storefront
          </span>
          <div className="flex-1">
            <h2 className="font-medium text-deep-sage text-lg mb-2">
              Become a Seller
            </h2>
            <p className="text-sage-green mb-4">
              Start selling your own orchids and botanical products on Orchidillo. Reach thousands of plant enthusiasts!
            </p>
            <button className="bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all">
              Upgrade to Seller Account
            </button>
          </div>
        </div>
      </div>
      
      {/* Save Button */}
      <div className="flex justify-end">
        <button
          onClick={handleSave}
          disabled={isSaving}
          className="bg-deep-sage text-white py-3 px-12 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50"
        >
          {isSaving ? 'Saving...' : 'Save All Changes'}
        </button>
      </div>
    </div>
  )
}
