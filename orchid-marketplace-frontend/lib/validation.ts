/**
 * Form Validation Library
 * Provides reusable validation functions and helpers for all forms in the application
 */

export interface ValidationResult {
  isValid: boolean
  errors: Record<string, string>
}

export interface ValidationRule {
  validator: (value: any, formData?: any) => boolean
  message: string
}

/**
 * Common validation rules
 */
export const validators = {
  required: (message: string = 'This field is required'): ValidationRule => ({
    validator: (value) => {
      if (typeof value === 'string') return value.trim().length > 0
      if (typeof value === 'number') return !isNaN(value)
      if (Array.isArray(value)) return value.length > 0
      return value !== null && value !== undefined
    },
    message,
  }),

  email: (message: string = 'Please enter a valid email address'): ValidationRule => ({
    validator: (value) => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return emailRegex.test(String(value).toLowerCase())
    },
    message,
  }),

  minLength: (length: number, message?: string): ValidationRule => ({
    validator: (value) => String(value).length >= length,
    message: message || `Must be at least ${length} characters`,
  }),

  maxLength: (length: number, message?: string): ValidationRule => ({
    validator: (value) => String(value).length <= length,
    message: message || `Must be no more than ${length} characters`,
  }),

  pattern: (regex: RegExp, message: string = 'Invalid format'): ValidationRule => ({
    validator: (value) => regex.test(String(value)),
    message,
  }),

  min: (minValue: number, message?: string): ValidationRule => ({
    validator: (value) => Number(value) >= minValue,
    message: message || `Must be at least ${minValue}`,
  }),

  max: (maxValue: number, message?: string): ValidationRule => ({
    validator: (value) => Number(value) <= maxValue,
    message: message || `Must be no more than ${maxValue}`,
  }),

  match: (fieldName: string, label: string): ValidationRule => ({
    validator: (value, formData) => {
      return formData && value === formData[fieldName]
    },
    message: `Must match ${label}`,
  }),

  url: (message: string = 'Please enter a valid URL'): ValidationRule => ({
    validator: (value) => {
      try {
        new URL(String(value))
        return true
      } catch {
        return false
      }
    },
    message,
  }),

  phone: (message: string = 'Please enter a valid phone number'): ValidationRule => ({
    validator: (value) => {
      const phoneRegex = /^[\d\s\-\+\(\)]+$/
      return phoneRegex.test(String(value)) && String(value).replace(/\D/g, '').length >= 10
    },
    message,
  }),

  price: (message: string = 'Please enter a valid price'): ValidationRule => ({
    validator: (value) => {
      const num = Number(value)
      return !isNaN(num) && num >= 0 && Number(num.toFixed(2)) === num
    },
    message,
  }),

  zipCode: (message: string = 'Please enter a valid ZIP code'): ValidationRule => ({
    validator: (value) => {
      const zipRegex = /^\d{5}(-\d{4})?$/
      return zipRegex.test(String(value))
    },
    message,
  }),
}

/**
 * Product-specific validations
 */
export const productValidation = {
  title: [
    validators.required('Product title is required'),
    validators.minLength(3, 'Title must be at least 3 characters'),
    validators.maxLength(100, 'Title must be no more than 100 characters'),
  ],
  description: [
    validators.required('Product description is required'),
    validators.minLength(20, 'Description must be at least 20 characters'),
    validators.maxLength(2000, 'Description must be no more than 2000 characters'),
  ],
  price: [
    validators.required('Price is required'),
    validators.min(0.01, 'Price must be greater than 0'),
    validators.max(100000, 'Price seems unreasonably high'),
    validators.price(),
  ],
  stock: [
    validators.required('Stock quantity is required'),
    validators.min(0, 'Stock cannot be negative'),
  ],
  category: [
    validators.required('Category is required'),
  ],
}

/**
 * Address validations
 */
export const addressValidation = {
  fullName: [
    validators.required('Full name is required'),
    validators.minLength(2, 'Name must be at least 2 characters'),
  ],
  addressLine1: [
    validators.required('Street address is required'),
    validators.minLength(5, 'Address must be at least 5 characters'),
  ],
  city: [
    validators.required('City is required'),
    validators.minLength(2, 'City must be at least 2 characters'),
  ],
  state: [
    validators.required('State is required'),
  ],
  zipCode: [
    validators.required('ZIP code is required'),
    validators.zipCode(),
  ],
  country: [
    validators.required('Country is required'),
  ],
  phone: [
    validators.required('Phone number is required'),
    validators.phone(),
  ],
}

/**
 * Review validations
 */
export const reviewValidation = {
  rating: [
    validators.required('Rating is required'),
    validators.min(1, 'Rating must be at least 1'),
    validators.max(5, 'Rating must be no more than 5'),
  ],
  title: [
    validators.required('Review title is required'),
    validators.minLength(5, 'Title must be at least 5 characters'),
    validators.maxLength(100, 'Title must be no more than 100 characters'),
  ],
  comment: [
    validators.required('Review comment is required'),
    validators.minLength(20, 'Comment must be at least 20 characters'),
    validators.maxLength(1000, 'Comment must be no more than 1000 characters'),
  ],
}

/**
 * Auth validations
 */
export const authValidation = {
  email: [
    validators.required('Email is required'),
    validators.email(),
  ],
  password: [
    validators.required('Password is required'),
    validators.minLength(8, 'Password must be at least 8 characters'),
    validators.pattern(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/,
      'Password must contain uppercase, lowercase, and number'
    ),
  ],
  confirmPassword: (passwordField: string = 'password') => [
    validators.required('Please confirm your password'),
    validators.match(passwordField, 'password'),
  ],
}

/**
 * Main validation function
 * @param formData - The form data to validate
 * @param schema - Validation schema with field names and rules
 * @returns ValidationResult with isValid flag and errors object
 */
export function validate(
  formData: Record<string, any>,
  schema: Record<string, ValidationRule[]>
): ValidationResult {
  const errors: Record<string, string> = {}

  for (const [fieldName, rules] of Object.entries(schema)) {
    const value = formData[fieldName]

    // Skip validation if field is not required and empty
    const isRequired = rules.some(rule => rule.message.toLowerCase().includes('required'))
    const isEmpty = value === '' || value === null || value === undefined

    if (!isRequired && isEmpty) {
      continue
    }

    // Run all validation rules for this field
    for (const rule of rules) {
      if (!rule.validator(value, formData)) {
        errors[fieldName] = rule.message
        break // Stop at first error for this field
      }
    }
  }

  return {
    isValid: Object.keys(errors).length === 0,
    errors,
  }
}

/**
 * Validate a single field
 * Useful for real-time validation
 */
export function validateField(
  value: any,
  rules: ValidationRule[],
  formData?: Record<string, any>
): string | null {
  for (const rule of rules) {
    if (!rule.validator(value, formData)) {
      return rule.message
    }
  }
  return null
}

/**
 * Hook-friendly validator that can be used in React components
 */
export function createValidator(schema: Record<string, ValidationRule[]>) {
  return (formData: Record<string, any>) => validate(formData, schema)
}

/**
 * Example usage:
 * 
 * // In a component:
 * const handleSubmit = (e) => {
 *   e.preventDefault()
 *   const result = validate(formData, productValidation)
 *   if (!result.isValid) {
 *     setErrors(result.errors)
 *     return
 *   }
 *   // Submit form
 * }
 * 
 * // Real-time field validation:
 * const handleFieldBlur = (fieldName) => {
 *   const error = validateField(
 *     formData[fieldName],
 *     productValidation[fieldName],
 *     formData
 *   )
 *   setErrors(prev => ({ ...prev, [fieldName]: error || undefined }))
 * }
 */
