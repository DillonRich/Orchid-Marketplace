'use client'

import { useState, useEffect } from 'react'
import AddressFormModal from '@/components/AddressFormModal'
import ConfirmDialog from '@/components/ConfirmDialog'

export default function AddressesPage() {
  const [addresses, setAddresses] = useState<any[]>([])
  const [showForm, setShowForm] = useState(false)
  const [editingId, setEditingId] = useState<string | null>(null)
  const [deleteConfirmId, setDeleteConfirmId] = useState<string | null>(null)
  
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    street: '',
    apt: '',
    city: '',
    state: '',
    zip: '',
    phone: '',
    isDefault: false,
  })
  
  useEffect(() => {
    // Mock addresses
    setAddresses([
      {
        id: '1',
        firstName: 'John',
        lastName: 'Doe',
        street: '123 Main St',
        apt: 'Apt 4B',
        city: 'New York',
        state: 'NY',
        zip: '10001',
        phone: '+1 (555) 123-4567',
        isDefault: true,
      },
      {
        id: '2',
        firstName: 'Jane',
        lastName: 'Doe',
        street: '456 Oak Avenue',
        apt: '',
        city: 'Brooklyn',
        state: 'NY',
        zip: '11201',
        phone: '+1 (555) 987-6543',
        isDefault: false,
      },
    ])
  }, [])
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
    }))
  }
  
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // API call to save address
    setShowForm(false)
    setEditingId(null)
    setFormData({
      firstName: '',
      lastName: '',
      street: '',
      apt: '',
      city: '',
      state: '',
      zip: '',
      phone: '',
      isDefault: false,
    })
  }
  
  const handleEdit = (address: any) => {
    setFormData(address)
    setEditingId(address.id)
    setShowForm(true)
  }
  
  const handleDelete = (id: string) => {
    setAddresses(prev => prev.filter(addr => addr.id !== id))
    setDeleteConfirmId(null)
  }
  
  const handleSetDefault = (id: string) => {
    setAddresses(prev => prev.map(addr => ({
      ...addr,
      isDefault: addr.id === id
    })))
  }
  
  const handleModalSave = async (addressData: any) => {
    // Transform modal format to internal format
    const address = {
      id: editingId || Date.now().toString(),
      firstName: addressData.fullName.split(' ')[0] || '',
      lastName: addressData.fullName.split(' ').slice(1).join(' ') || '',
      street: addressData.streetAddress,
      apt: '',
      city: addressData.city,
      state: addressData.stateProvince,
      zip: addressData.postalCode,
      phone: addressData.phoneNumber,
      isDefault: addressData.isDefault || false,
    }
    
    if (editingId) {
      setAddresses(prev => prev.map(addr => addr.id === editingId ? address : addr))
    } else {
      setAddresses(prev => [...prev, address])
    }
    
    setShowForm(false)
    setEditingId(null)
  }
  
  return (
    <div className="space-y-8">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
            Saved Addresses
          </h1>
          <p className="text-sage-green">
            Manage your shipping and billing addresses
          </p>
        </div>
        
        <button
          onClick={() => setShowForm(true)}
          className="bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all flex items-center gap-2"
        >
          <span className="material-symbols-outlined">add</span>
          Add Address
        </button>
      </div>
      
      {/* Inline form hidden - using modal instead */}
      {false && showForm && (
        <div className="bg-white rounded-xl p-8">
          <h2 className="font-medium text-deep-sage text-lg mb-6">
            {editingId ? 'Edit Address' : 'Add New Address'}
          </h2>
          
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  First Name *
                </label>
                <input
                  type="text"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Last Name *
                </label>
                <input
                  type="text"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                />
              </div>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Street Address *
              </label>
              <input
                type="text"
                name="street"
                value={formData.street}
                onChange={handleChange}
                required
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Apartment, Suite, etc.
              </label>
              <input
                type="text"
                name="apt"
                value={formData.apt}
                onChange={handleChange}
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  City *
                </label>
                <input
                  type="text"
                  name="city"
                  value={formData.city}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  State *
                </label>
                <select
                  name="state"
                  value={formData.state}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                >
                  <option value="">Select</option>
                  <option value="NY">New York</option>
                  <option value="CA">California</option>
                  <option value="TX">Texas</option>
                  {/* Add more states */}
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  ZIP Code *
                </label>
                <input
                  type="text"
                  name="zip"
                  value={formData.zip}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                />
              </div>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-deep-sage mb-2">
                Phone Number *
              </label>
              <input
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                required
                placeholder="+1 (555) 000-0000"
                className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
              />
            </div>
            
            <div className="flex items-center gap-2">
              <input
                type="checkbox"
                id="isDefault"
                name="isDefault"
                checked={formData.isDefault}
                onChange={handleChange}
                className="w-5 h-5 text-accent-peach rounded focus:ring-2 focus:ring-accent-peach"
              />
              <label htmlFor="isDefault" className="text-sm text-sage-green cursor-pointer">
                Set as default address
              </label>
            </div>
            
            <div className="flex gap-3 pt-4">
              <button
                type="submit"
                className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
              >
                {editingId ? 'Save Changes' : 'Add Address'}
              </button>
              <button
                type="button"
                onClick={() => {
                  setShowForm(false)
                  setEditingId(null)
                  setFormData({
                    firstName: '',
                    lastName: '',
                    street: '',
                    apt: '',
                    city: '',
                    state: '',
                    zip: '',
                    phone: '',
                    isDefault: false,
                  })
                }}
                className="px-8 py-3 border border-gray-300 text-sage-green rounded-full font-medium hover:bg-soft-peach transition-all"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}
      
      {/* Addresses Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {addresses.map(address => (
          <div
            key={address.id}
            className="bg-white rounded-xl p-6 relative hover:shadow-lg transition-all"
          >
            {address.isDefault && (
              <span className="absolute top-4 right-4 bg-deep-sage text-white text-xs px-3 py-1 rounded-full">
                Default
              </span>
            )}
            
            <div className="mb-4">
              <p className="font-medium text-deep-sage text-lg mb-2">
                {address.firstName} {address.lastName}
              </p>
              <p className="text-sage-green">{address.street}</p>
              {address.apt && <p className="text-sage-green">{address.apt}</p>}
              <p className="text-sage-green">
                {address.city}, {address.state} {address.zip}
              </p>
              <p className="text-sage-green mt-2">{address.phone}</p>
            </div>
            
            <div className="flex gap-2">
              <button
                onClick={() => handleEdit(address)}
                className="flex-1 py-2 px-4 border border-deep-sage text-deep-sage rounded-full text-sm font-medium hover:bg-soft-peach transition-all"
              >
                Edit
              </button>
              
              {!address.isDefault && (
                <>
                  <button
                    onClick={() => handleSetDefault(address.id)}
                    className="flex-1 py-2 px-4 bg-deep-sage text-white rounded-full text-sm font-medium hover:bg-sage-green transition-all"
                  >
                    Set Default
                  </button>
                  <button
                    onClick={() => setDeleteConfirmId(address.id)}
                    className="p-2 text-red-500 hover:bg-red-50 rounded-full transition-all"
                  >
                    <span className="material-symbols-outlined">delete</span>
                  </button>
                </>
              )}
            </div>
          </div>
        ))}
      </div>
      
      {addresses.length === 0 && !showForm && (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-sage-green mb-4">
            location_on
          </span>
          <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
            No addresses saved
          </h3>
          <p className="text-sage-green mb-6">
            Add your first shipping address to speed up checkout
          </p>
          <button
            onClick={() => setShowForm(true)}
            className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
          >
            Add Address
          </button>
        </div>
      )}
      
      {/* Address Form Modal */}
      <AddressFormModal
        isOpen={showForm}
        onClose={() => {
          setShowForm(false)
          setEditingId(null)
        }}
        onSave={handleModalSave}
        mode={editingId ? 'edit' : 'add'}
        initialData={editingId ? {
          fullName: `${formData.firstName} ${formData.lastName}`,
          streetAddress: formData.street,
          city: formData.city,
          stateProvince: formData.state,
          postalCode: formData.zip,
          country: 'United States',
          phoneNumber: formData.phone,
          isDefault: formData.isDefault,
        } : undefined}
      />
      
      {/* Delete Confirmation Dialog */}
      <ConfirmDialog
        isOpen={!!deleteConfirmId}
        onClose={() => setDeleteConfirmId(null)}
        onConfirm={() => handleDelete(deleteConfirmId!)}
        title="Delete Address?"
        message="Are you sure you want to delete this address? This action cannot be undone."
        confirmText="Delete"
        variant="danger"
      />
    </div>
  )
}
