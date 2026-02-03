'use client'

import { useState, useEffect } from 'react'
import { validateField, type ValidationRule } from '@/lib/validation'

interface FormFieldProps {
  name: string
  label: string
  type?: 'text' | 'email' | 'password' | 'tel' | 'number' | 'url' | 'textarea'
  value: any
  onChange: (name: string, value: any) => void
  rules?: ValidationRule[]
  placeholder?: string
  disabled?: boolean
  required?: boolean
  className?: string
  rows?: number
  formData?: Record<string, any>
  validateOnBlur?: boolean
  validateOnChange?: boolean
}

export default function FormField({
  name,
  label,
  type = 'text',
  value,
  onChange,
  rules = [],
  placeholder,
  disabled = false,
  required = false,
  className = '',
  rows = 4,
  formData,
  validateOnBlur = true,
  validateOnChange = false,
}: FormFieldProps) {
  const [error, setError] = useState<string | null>(null)
  const [touched, setTouched] = useState(false)

  const validateValue = (val: any) => {
    if (!rules || rules.length === 0) return
    
    const errorMessage = validateField(val, rules, formData)
    setError(errorMessage)
  }

  useEffect(() => {
    if (validateOnChange && touched) {
      validateValue(value)
    }
  }, [value, formData])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const newValue = type === 'number' ? parseFloat(e.target.value) : e.target.value
    onChange(name, newValue)
    
    if (validateOnChange && touched) {
      validateValue(newValue)
    }
  }

  const handleBlur = () => {
    setTouched(true)
    if (validateOnBlur) {
      validateValue(value)
    }
  }

  const inputClasses = `
    w-full px-4 py-3 rounded-lg border 
    ${error && touched ? 'border-red-500 focus:ring-red-500' : 'border-gray-300 focus:ring-accent-peach'}
    bg-warm-cream focus:outline-none focus:ring-2 transition-all
    disabled:bg-gray-100 disabled:cursor-not-allowed
    ${className}
  `

  return (
    <div className="space-y-2">
      <label htmlFor={name} className="block text-sm font-medium text-deep-sage">
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </label>
      
      {type === 'textarea' ? (
        <textarea
          id={name}
          name={name}
          value={value}
          onChange={handleChange}
          onBlur={handleBlur}
          placeholder={placeholder}
          disabled={disabled}
          required={required}
          rows={rows}
          className={inputClasses}
        />
      ) : (
        <input
          id={name}
          name={name}
          type={type}
          value={value}
          onChange={handleChange}
          onBlur={handleBlur}
          placeholder={placeholder}
          disabled={disabled}
          required={required}
          className={inputClasses}
        />
      )}
      
      {error && touched && (
        <div className="flex items-start gap-2 text-red-600 text-sm">
          <span className="material-symbols-outlined text-base">error</span>
          <span>{error}</span>
        </div>
      )}
      
      {!error && touched && value && (
        <div className="flex items-center gap-2 text-green-600 text-sm">
          <span className="material-symbols-outlined text-base">check_circle</span>
          <span>Looks good!</span>
        </div>
      )}
    </div>
  )
}

/**
 * Example Usage:
 * 
 * import FormField from '@/components/FormField'
 * import { productValidation } from '@/lib/validation'
 * 
 * const [formData, setFormData] = useState({ title: '', price: '' })
 * 
 * const handleChange = (name: string, value: any) => {
 *   setFormData(prev => ({ ...prev, [name]: value }))
 * }
 * 
 * <FormField
 *   name="title"
 *   label="Product Title"
 *   value={formData.title}
 *   onChange={handleChange}
 *   rules={productValidation.title}
 *   required
 *   validateOnChange
 * />
 * 
 * <FormField
 *   name="price"
 *   label="Price"
 *   type="number"
 *   value={formData.price}
 *   onChange={handleChange}
 *   rules={productValidation.price}
 *   required
 * />
 */
