'use client'

import { useState } from 'react'
import { useAuthStore } from '@/lib/auth-store'

export default function ProfilePage() {
  const { user } = useAuthStore()
  
  const [formData, setFormData] = useState({
    firstName: user?.fullName?.split(' ')[0] || '',
    lastName: user?.fullName?.split(' ')[1] || '',
    email: user?.email || '',
    phone: '',
    dateOfBirth: '',
  })
  
  const [isEditing, setIsEditing] = useState(false)
  const [isSaving, setIsSaving] = useState(false)
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }
  
  const handleSave = async () => {
    setIsSaving(true)
    try {
      // API call to update profile
      await new Promise(resolve => setTimeout(resolve, 1000))
      setIsEditing(false)
    } catch (error) {
      console.error('Error saving profile:', error)
    } finally {
      setIsSaving(false)
    }
  }
  
  return (
    <div className="space-y-8">
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          Profile Settings
        </h1>
        <p className="text-sage-green">
          Manage your personal information
        </p>
      </div>
      
      {/* Profile Photo */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Profile Photo</h2>
        <div className="flex items-center gap-6">
          <div className="w-24 h-24 rounded-full bg-soft-peach flex items-center justify-center">
            <span className="material-symbols-outlined text-5xl text-deep-sage">
              person
            </span>
          </div>
          <div>
            <button className="bg-deep-sage text-white py-2 px-6 rounded-full font-medium hover:bg-sage-green transition-all mb-2">
              Upload Photo
            </button>
            <p className="text-sm text-sage-green">
              JPG, PNG or GIF. Max size 2MB
            </p>
          </div>
        </div>
      </div>
      
      {/* Personal Information */}
      <div className="bg-white rounded-xl p-8">
        <div className="flex items-center justify-between mb-6">
          <h2 className="font-medium text-deep-sage text-lg">Personal Information</h2>
          {!isEditing && (
            <button
              onClick={() => setIsEditing(true)}
              className="text-accent-peach hover:text-deep-sage transition-colors text-sm font-medium"
            >
              Edit
            </button>
          )}
        </div>
        
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                First Name
              </label>
              <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                disabled={!isEditing}
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach disabled:opacity-60"
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Last Name
              </label>
              <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                disabled={!isEditing}
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach disabled:opacity-60"
              />
            </div>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Email Address
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              disabled
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-gray-100 opacity-60"
            />
            <p className="mt-1 text-sm text-sage-green">
              Email cannot be changed. Contact support if needed.
            </p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Phone Number
              </label>
              <input
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                disabled={!isEditing}
                placeholder="+1 (555) 000-0000"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach disabled:opacity-60"
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Date of Birth
              </label>
              <input
                type="date"
                name="dateOfBirth"
                value={formData.dateOfBirth}
                onChange={handleChange}
                disabled={!isEditing}
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach disabled:opacity-60"
              />
            </div>
          </div>
          
          {isEditing && (
            <div className="flex gap-3 pt-4">
              <button
                onClick={handleSave}
                disabled={isSaving}
                className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50"
              >
                {isSaving ? 'Saving...' : 'Save Changes'}
              </button>
              <button
                onClick={() => setIsEditing(false)}
                className="px-8 py-3 border border-gray-300 text-sage-green rounded-full font-medium hover:bg-soft-peach transition-all"
              >
                Cancel
              </button>
            </div>
          )}
        </div>
      </div>
      
      {/* Change Password */}
      <div className="bg-white rounded-xl p-8">
        <h2 className="font-medium text-deep-sage text-lg mb-6">Change Password</h2>
        <div className="space-y-4 max-w-md">
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Current Password
            </label>
            <input
              type="password"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              New Password
            </label>
            <input
              type="password"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-deep-sage mb-2">
              Confirm New Password
            </label>
            <input
              type="password"
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>
          
          <button className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all">
            Update Password
          </button>
        </div>
      </div>
      
      {/* Danger Zone */}
      <div className="bg-white rounded-xl p-6 border-2 border-red-200">
        <h2 className="font-medium text-red-600 text-lg mb-2">Danger Zone</h2>
        <p className="text-sage-green text-sm mb-4">
          Once you delete your account, there is no going back. Please be certain.
        </p>
        <button 
          onClick={() => {
            if (window.confirm('Are you absolutely sure you want to delete your account? This action cannot be undone and all your data will be permanently removed.')) {
              // Handle account deletion
              alert('Account deletion will be implemented')
            }
          }}
          className="bg-red-500 text-white py-2 px-6 rounded-full text-sm font-medium hover:bg-red-600 transition-all"
        >
          Delete Account
        </button>
      </div>
    </div>
  )
}
