'use client'

import { useState } from 'react'
import { apiClient } from '@/lib/api-client'

export default function ContactPage() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: '',
    message: '',
  })
  
  const [isSending, setIsSending] = useState(false)
  const [successMessage, setSuccessMessage] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }))
    // Clear messages on input change
    setSuccessMessage('')
    setErrorMessage('')
  }
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsSending(true)
    setSuccessMessage('')
    setErrorMessage('')
    
    try {
      await apiClient.sendContactMessage(formData)
      setSuccessMessage('Message sent successfully! We\'ll respond within 24 hours.')
      setFormData({ name: '', email: '', subject: '', message: '' })
    } catch (error: any) {
      console.error('Error sending message:', error)
      setErrorMessage(error?.response?.data?.message || 'Failed to send message. Please try again.')
    } finally {
      setIsSending(false)
    }
  }
  
  return (
    <div className="bg-warm-cream min-h-screen">
      <div className="max-w-[1000px] mx-auto px-4 py-16">
        <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
          Contact Us
        </h1>
        <p className="text-lg text-sage-green text-center mb-12">
          Have a question or need assistance? We're here to help!
        </p>
        
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
          {/* Contact Form */}
          <div className="bg-white rounded-xl p-8">
            <h2 className="font-medium text-deep-sage text-xl mb-6">Send Us a Message</h2>
            
            <form onSubmit={handleSubmit} className="space-y-6">
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Your Name *
                </label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Email Address *
                </label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Subject *
                </label>
                <select
                  name="subject"
                  value={formData.subject}
                  onChange={handleChange}
                  required
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                >
                  <option value="">Select a subject</option>
                  <option value="general">General Inquiry</option>
                  <option value="order">Order Support</option>
                  <option value="seller">Seller Inquiry</option>
                  <option value="technical">Technical Issue</option>
                  <option value="other">Other</option>
                </select>
              </div>
              
              <div>
                <label className="block text-sm font-medium text-deep-sage mb-2">
                  Message *
                </label>
                <textarea
                  name="message"
                  value={formData.message}
                  onChange={handleChange}
                  required
                  rows={6}
                  className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
                />
              </div>
              
              {/* Success Message */}
              {successMessage && (
                <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg">
                  <div className="flex items-center gap-2">
                    <span className="material-symbols-outlined text-sm">check_circle</span>
                    <span>{successMessage}</span>
                  </div>
                </div>
              )}
              
              {/* Error Message */}
              {errorMessage && (
                <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
                  <div className="flex items-center gap-2">
                    <span className="material-symbols-outlined text-sm">error</span>
                    <span>{errorMessage}</span>
                  </div>
                </div>
              )}
              
              <button
                type="submit"
                disabled={isSending}
                className="w-full bg-deep-sage text-white py-3 px-6 rounded-full font-medium hover:bg-sage-green transition-all disabled:opacity-50"
              >
                {isSending ? 'Sending...' : 'Send Message'}
              </button>
            </form>
          </div>
          
          {/* Contact Info */}
          <div className="space-y-8">
            <div className="bg-white rounded-xl p-8">
              <div className="flex items-start gap-4 mb-6">
                <span className="material-symbols-outlined text-3xl text-deep-sage">
                  mail
                </span>
                <div>
                  <h3 className="font-medium text-deep-sage text-lg mb-2">Email Us</h3>
                  <p className="text-sage-green">support@orchidillo.com</p>
                  <p className="text-sm text-sage-green mt-1">We'll respond within 24 hours</p>
                </div>
              </div>
            </div>
            
            <div className="bg-white rounded-xl p-8">
              <div className="flex items-start gap-4 mb-6">
                <span className="material-symbols-outlined text-3xl text-deep-sage">
                  schedule
                </span>
                <div>
                  <h3 className="font-medium text-deep-sage text-lg mb-2">Business Hours</h3>
                  <p className="text-sage-green">Monday - Friday: 9am - 6pm EST</p>
                  <p className="text-sage-green">Saturday - Sunday: 10am - 4pm EST</p>
                </div>
              </div>
            </div>
            
            <div className="bg-white rounded-xl p-8">
              <div className="flex items-start gap-4">
                <span className="material-symbols-outlined text-3xl text-deep-sage">
                  help
                </span>
                <div>
                  <h3 className="font-medium text-deep-sage text-lg mb-2">Help Center</h3>
                  <p className="text-sage-green mb-3">
                    Find answers to common questions in our FAQ
                  </p>
                  <a
                    href="/faq"
                    className="text-accent-peach hover:text-deep-sage font-medium"
                  >
                    Visit FAQ →
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
