'use client';

import { Dialog, DialogPanel, DialogTitle, Transition, TransitionChild } from '@headlessui/react';
import { Fragment, useState } from 'react';
import { X } from 'lucide-react';

interface Address {
  id?: string;
  label?: string;
  fullName: string;
  streetAddress: string;
  city: string;
  stateProvince: string;
  postalCode: string;
  country: string;
  phoneNumber: string;
  isDefault?: boolean;
}

interface AddressFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (address: Address) => Promise<void>;
  initialData?: Address;
  mode?: 'add' | 'edit';
}

export default function AddressFormModal({
  isOpen,
  onClose,
  onSave,
  initialData,
  mode = 'add',
}: AddressFormModalProps) {
  const [formData, setFormData] = useState<Address>(
    initialData || {
      label: '',
      fullName: '',
      streetAddress: '',
      city: '',
      stateProvince: '',
      postalCode: '',
      country: 'United States',
      phoneNumber: '',
      isDefault: false,
    }
  );
  const [errors, setErrors] = useState<Partial<Record<keyof Address, string>>>({});
  const [loading, setLoading] = useState(false);

  const handleChange = (field: keyof Address, value: string | boolean) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: undefined }));
    }
  };

  const validate = (): boolean => {
    const newErrors: Partial<Record<keyof Address, string>> = {};

    if (!formData.fullName.trim()) newErrors.fullName = 'Full name is required';
    if (!formData.streetAddress.trim()) newErrors.streetAddress = 'Street address is required';
    if (!formData.city.trim()) newErrors.city = 'City is required';
    if (!formData.stateProvince.trim()) newErrors.stateProvince = 'State/Province is required';
    if (!formData.postalCode.trim()) newErrors.postalCode = 'Postal code is required';
    if (!formData.country.trim()) newErrors.country = 'Country is required';
    if (!formData.phoneNumber.trim()) {
      newErrors.phoneNumber = 'Phone number is required';
    } else if (!/^\+?[\d\s\-()]+$/.test(formData.phoneNumber)) {
      newErrors.phoneNumber = 'Invalid phone number format';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    setLoading(true);
    try {
      await onSave(formData);
      onClose();
    } catch (error) {
      console.error('Error saving address:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Transition appear show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-50" onClose={onClose}>
        <TransitionChild
          as={Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-black/40 backdrop-blur-sm" />
        </TransitionChild>

        <div className="fixed inset-0 overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4">
            <TransitionChild
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 scale-95"
              enterTo="opacity-100 scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 scale-100"
              leaveTo="opacity-0 scale-95"
            >
              <DialogPanel className="w-full max-w-lg transform overflow-hidden rounded-2xl bg-white shadow-2xl transition-all">
                {/* Header */}
                <div className="flex items-center justify-between border-b border-neutral-stone-200 px-6 py-4">
                  <DialogTitle className="text-xl font-semibold text-deep-sage">
                    {mode === 'add' ? 'Add New Address' : 'Edit Address'}
                  </DialogTitle>
                  <button
                    onClick={onClose}
                    className="rounded-full p-1 text-neutral-stone-400 hover:bg-neutral-stone-100 hover:text-deep-sage transition-colors"
                    aria-label="Close modal"
                  >
                    <X className="h-5 w-5" />
                  </button>
                </div>

                {/* Form */}
                <form onSubmit={handleSubmit} className="px-6 py-4 space-y-4">
                  {/* Address Label */}
                  <div>
                    <label htmlFor="label" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                      Address Label (Optional)
                    </label>
                    <input
                      type="text"
                      id="label"
                      value={formData.label || ''}
                      onChange={(e) => handleChange('label', e.target.value)}
                      placeholder="e.g., Home, Work, Parents"
                      className="w-full px-4 py-2 border border-neutral-stone-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage"
                    />
                  </div>

                  {/* Full Name */}
                  <div>
                    <label htmlFor="fullName" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                      Full Name *
                    </label>
                    <input
                      type="text"
                      id="fullName"
                      value={formData.fullName}
                      onChange={(e) => handleChange('fullName', e.target.value)}
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage ${
                        errors.fullName ? 'border-red-500' : 'border-neutral-stone-300'
                      }`}
                    />
                    {errors.fullName && <p className="mt-1 text-sm text-red-600">{errors.fullName}</p>}
                  </div>

                  {/* Street Address */}
                  <div>
                    <label htmlFor="streetAddress" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                      Street Address *
                    </label>
                    <input
                      type="text"
                      id="streetAddress"
                      value={formData.streetAddress}
                      onChange={(e) => handleChange('streetAddress', e.target.value)}
                      placeholder="123 Main St, Apt 4B"
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage ${
                        errors.streetAddress ? 'border-red-500' : 'border-neutral-stone-300'
                      }`}
                    />
                    {errors.streetAddress && <p className="mt-1 text-sm text-red-600">{errors.streetAddress}</p>}
                  </div>

                  {/* City */}
                  <div>
                    <label htmlFor="city" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                      City *
                    </label>
                    <input
                      type="text"
                      id="city"
                      value={formData.city}
                      onChange={(e) => handleChange('city', e.target.value)}
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage ${
                        errors.city ? 'border-red-500' : 'border-neutral-stone-300'
                      }`}
                    />
                    {errors.city && <p className="mt-1 text-sm text-red-600">{errors.city}</p>}
                  </div>

                  {/* State/Province and Postal Code */}
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label htmlFor="stateProvince" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                        State/Province *
                      </label>
                      <input
                        type="text"
                        id="stateProvince"
                        value={formData.stateProvince}
                        onChange={(e) => handleChange('stateProvince', e.target.value)}
                        placeholder="CA"
                        className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage ${
                          errors.stateProvince ? 'border-red-500' : 'border-neutral-stone-300'
                        }`}
                      />
                      {errors.stateProvince && <p className="mt-1 text-sm text-red-600">{errors.stateProvince}</p>}
                    </div>

                    <div>
                      <label htmlFor="postalCode" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                        Postal Code *
                      </label>
                      <input
                        type="text"
                        id="postalCode"
                        value={formData.postalCode}
                        onChange={(e) => handleChange('postalCode', e.target.value)}
                        placeholder="90210"
                        className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage ${
                          errors.postalCode ? 'border-red-500' : 'border-neutral-stone-300'
                        }`}
                      />
                      {errors.postalCode && <p className="mt-1 text-sm text-red-600">{errors.postalCode}</p>}
                    </div>
                  </div>

                  {/* Country */}
                  <div>
                    <label htmlFor="country" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                      Country *
                    </label>
                    <select
                      id="country"
                      value={formData.country}
                      onChange={(e) => handleChange('country', e.target.value)}
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage ${
                        errors.country ? 'border-red-500' : 'border-neutral-stone-300'
                      }`}
                    >
                      <option value="United States">United States</option>
                      <option value="Canada">Canada</option>
                      <option value="Mexico">Mexico</option>
                      <option value="United Kingdom">United Kingdom</option>
                      <option value="Australia">Australia</option>
                    </select>
                    {errors.country && <p className="mt-1 text-sm text-red-600">{errors.country}</p>}
                  </div>

                  {/* Phone Number */}
                  <div>
                    <label htmlFor="phoneNumber" className="block text-sm font-medium text-neutral-stone-700 mb-1">
                      Phone Number *
                    </label>
                    <input
                      type="tel"
                      id="phoneNumber"
                      value={formData.phoneNumber}
                      onChange={(e) => handleChange('phoneNumber', e.target.value)}
                      placeholder="+1 (555) 123-4567"
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-sage ${
                        errors.phoneNumber ? 'border-red-500' : 'border-neutral-stone-300'
                      }`}
                    />
                    {errors.phoneNumber && <p className="mt-1 text-sm text-red-600">{errors.phoneNumber}</p>}
                  </div>

                  {/* Set as Default */}
                  <div className="flex items-center">
                    <input
                      type="checkbox"
                      id="isDefault"
                      checked={formData.isDefault || false}
                      onChange={(e) => handleChange('isDefault', e.target.checked)}
                      className="h-4 w-4 text-primary-sage border-neutral-stone-300 rounded focus:ring-primary-sage"
                    />
                    <label htmlFor="isDefault" className="ml-2 text-sm text-neutral-stone-700">
                      Set as default shipping address
                    </label>
                  </div>

                  {/* Actions */}
                  <div className="flex gap-3 pt-4 border-t border-neutral-stone-200 mt-4">
                    <button
                      type="button"
                      onClick={onClose}
                      disabled={loading}
                      className="flex-1 rounded-full border-2 border-neutral-stone-300 bg-white px-4 py-3 text-sm font-semibold text-neutral-stone-700 hover:bg-neutral-stone-50 focus:outline-none focus:ring-2 focus:ring-neutral-stone-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors opacity-100"
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      disabled={loading}
                      className="flex-1 rounded-full bg-primary-sage px-4 py-3 text-sm font-semibold text-white hover:bg-deep-sage focus:outline-none focus:ring-2 focus:ring-primary-sage focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors shadow-sm opacity-100"
                    >
                      {loading ? 'Saving...' : mode === 'add' ? 'Add Address' : 'Save Changes'}
                    </button>
                  </div>
                </form>
              </DialogPanel>
            </TransitionChild>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
}
