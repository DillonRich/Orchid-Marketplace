'use client'

import { useState, useRef } from 'react'
import Image from 'next/image'

interface ImageUploadProps {
  images: File[]
  onImagesChange: (files: File[]) => void
  maxImages?: number
  maxSizeMB?: number
}

export default function ImageUpload({ 
  images, 
  onImagesChange, 
  maxImages = 10,
  maxSizeMB = 5 
}: ImageUploadProps) {
  const fileInputRef = useRef<HTMLInputElement>(null)
  const [dragActive, setDragActive] = useState(false)
  const [error, setError] = useState('')
  
  const validateFile = (file: File): boolean => {
    // Check file type
    if (!file.type.startsWith('image/')) {
      setError('Please upload only image files')
      return false
    }
    
    // Check file size
    const maxSize = maxSizeMB * 1024 * 1024
    if (file.size > maxSize) {
      setError(`File size must be less than ${maxSizeMB}MB`)
      return false
    }
    
    return true
  }
  
  const handleFiles = (files: FileList | null) => {
    if (!files) return
    
    setError('')
    const validFiles: File[] = []
    
    for (let i = 0; i < files.length; i++) {
      const file = files[i]
      if (validateFile(file)) {
        validFiles.push(file)
      }
    }
    
    // Check total count
    if (images.length + validFiles.length > maxImages) {
      setError(`Maximum ${maxImages} images allowed`)
      return
    }
    
    onImagesChange([...images, ...validFiles])
  }
  
  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true)
    } else if (e.type === 'dragleave') {
      setDragActive(false)
    }
  }
  
  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)
    
    handleFiles(e.dataTransfer.files)
  }
  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    handleFiles(e.target.files)
  }
  
  const removeImage = (index: number) => {
    onImagesChange(images.filter((_, i) => i !== index))
  }
  
  const moveImage = (fromIndex: number, toIndex: number) => {
    const newImages = [...images]
    const [movedImage] = newImages.splice(fromIndex, 1)
    newImages.splice(toIndex, 0, movedImage)
    onImagesChange(newImages)
  }
  
  return (
    <div className="space-y-4">
      {/* Upload Area */}
      <div
        onClick={() => fileInputRef.current?.click()}
        onDragEnter={handleDrag}
        onDragLeave={handleDrag}
        onDragOver={handleDrag}
        onDrop={handleDrop}
        className={`border-2 border-dashed rounded-xl p-12 text-center cursor-pointer transition-all ${
          dragActive 
            ? 'border-accent-peach bg-soft-peach' 
            : 'border-gray-300 hover:border-accent-peach hover:bg-soft-peach'
        }`}
      >
        <input
          ref={fileInputRef}
          type="file"
          multiple
          accept="image/*"
          onChange={handleChange}
          className="hidden"
        />
        
        <span className="material-symbols-outlined text-6xl text-sage-green mb-4 block">
          add_photo_alternate
        </span>
        <p className="text-sage-green mb-2 font-medium">
          {dragActive ? 'Drop images here' : 'Click to upload or drag and drop'}
        </p>
        <p className="text-sm text-sage-green">
          PNG, JPG or WEBP (max. {maxSizeMB}MB each, up to {maxImages} images)
        </p>
      </div>
      
      {/* Error Message */}
      {error && (
        <div className="bg-red-50 text-red-600 px-4 py-3 rounded-lg flex items-center gap-2">
          <span className="material-symbols-outlined">error</span>
          <p className="text-sm">{error}</p>
        </div>
      )}
      
      {/* Image Preview Grid */}
      {images.length > 0 && (
        <div>
          <p className="text-sm text-sage-green mb-3">
            {images.length} {images.length === 1 ? 'image' : 'images'} selected
            {images.length > 0 && <span className="ml-1">(first image will be the primary)</span>}
          </p>
          
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {images.map((file, index) => (
              <div key={index} className="relative group">
                <div className="relative aspect-square rounded-lg overflow-hidden bg-gray-100 border-2 border-gray-200">
                  <Image
                    src={URL.createObjectURL(file)}
                    alt={`Upload ${index + 1}`}
                    fill
                    className="object-cover"
                  />
                  
                  {/* Primary Badge */}
                  {index === 0 && (
                    <div className="absolute top-2 left-2 bg-deep-sage text-white text-xs px-2 py-1 rounded-full font-medium">
                      Primary
                    </div>
                  )}
                  
                  {/* Actions Overlay */}
                  <div className="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2">
                    {index > 0 && (
                      <button
                        type="button"
                        onClick={() => moveImage(index, index - 1)}
                        className="bg-white text-deep-sage p-2 rounded-full hover:bg-soft-peach transition-all"
                        title="Move left"
                      >
                        <span className="material-symbols-outlined text-lg">arrow_back</span>
                      </button>
                    )}
                    
                    <button
                      type="button"
                      onClick={() => removeImage(index)}
                      className="bg-white text-red-600 p-2 rounded-full hover:bg-red-50 transition-all"
                      title="Remove"
                    >
                      <span className="material-symbols-outlined text-lg">delete</span>
                    </button>
                    
                    {index < images.length - 1 && (
                      <button
                        type="button"
                        onClick={() => moveImage(index, index + 1)}
                        className="bg-white text-deep-sage p-2 rounded-full hover:bg-soft-peach transition-all"
                        title="Move right"
                      >
                        <span className="material-symbols-outlined text-lg">arrow_forward</span>
                      </button>
                    )}
                  </div>
                </div>
                
                <p className="text-xs text-sage-green mt-1 truncate">
                  {file.name}
                </p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
