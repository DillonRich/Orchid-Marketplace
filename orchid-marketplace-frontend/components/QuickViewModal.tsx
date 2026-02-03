'use client';

import { Dialog, DialogPanel, Transition, TransitionChild } from '@headlessui/react';
import { Fragment, useState } from 'react';
import { X, ShoppingCart, ExternalLink, Star, Heart } from 'lucide-react';
import Image from 'next/image';
import Link from 'next/link';
import { useCartStore } from '@/lib/cart-store';

interface QuickViewModalProps {
  isOpen: boolean;
  onClose: () => void;
  product: {
    id: string;
    name: string;
    price: number;
    description: string;
    images: { url: string; altText: string }[];
    rating?: number;
    reviewCount?: number;
    stock: number;
    category?: string;
  };
}

export default function QuickViewModal({ isOpen, onClose, product }: QuickViewModalProps) {
  const [quantity, setQuantity] = useState(1);
  const [selectedImage, setSelectedImage] = useState(0);
  const [isFavorite, setIsFavorite] = useState(false);
  const addItem = useCartStore((state) => state.addItem);

  const handleAddToCart = () => {
    addItem({
      id: product.id,
      title: product.name,
      price: product.price,
      quantity,
      imageUrl: product.images[0]?.url || '/images/placeholder.jpg',
    });
    onClose();
  };

  const incrementQuantity = () => {
    if (quantity < product.stock) {
      setQuantity(quantity + 1);
    }
  };

  const decrementQuantity = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
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
              <DialogPanel className="w-full max-w-4xl transform overflow-hidden rounded-2xl bg-white shadow-2xl transition-all">
                {/* Close Button */}
                <button
                  onClick={onClose}
                  className="absolute right-4 top-4 z-10 rounded-full bg-white/90 p-2 text-neutral-stone-600 hover:bg-white hover:text-deep-sage transition-colors shadow-lg"
                  aria-label="Close quick view"
                >
                  <X className="h-5 w-5" />
                </button>

                <div className="grid md:grid-cols-2 gap-8 p-8">
                  {/* Image Gallery */}
                  <div className="space-y-4">
                    <div className="aspect-square relative rounded-xl overflow-hidden bg-neutral-stone-50">
                      <Image
                        src={product.images[selectedImage]?.url || '/images/placeholder.jpg'}
                        alt={product.images[selectedImage]?.altText || product.name}
                        fill
                        className="object-cover"
                      />
                    </div>

                    {/* Thumbnail Strip */}
                    {product.images.length > 1 && (
                      <div className="flex gap-2 overflow-x-auto">
                        {product.images.map((image, index) => (
                          <button
                            key={index}
                            onClick={() => setSelectedImage(index)}
                            className={`flex-shrink-0 w-20 h-20 rounded-lg overflow-hidden border-2 transition-colors ${
                              selectedImage === index
                                ? 'border-primary-sage'
                                : 'border-transparent hover:border-neutral-stone-300'
                            }`}
                          >
                            <Image
                              src={image.url}
                              alt={image.altText || `${product.name} ${index + 1}`}
                              width={80}
                              height={80}
                              className="object-cover w-full h-full"
                            />
                          </button>
                        ))}
                      </div>
                    )}
                  </div>

                  {/* Product Info */}
                  <div className="flex flex-col">
                    {/* Category */}
                    {product.category && (
                      <p className="text-sm text-neutral-stone-500 mb-2">{product.category}</p>
                    )}

                    {/* Title */}
                    <h2 className="text-2xl font-semibold text-deep-sage mb-2">{product.name}</h2>

                    {/* Rating */}
                    {product.rating && (
                      <div className="flex items-center gap-2 mb-4">
                        <div className="flex items-center">
                          {[...Array(5)].map((_, i) => (
                            <Star
                              key={i}
                              className={`h-4 w-4 ${
                                i < Math.floor(product.rating!)
                                  ? 'fill-yellow-400 text-yellow-400'
                                  : 'text-neutral-stone-300'
                              }`}
                            />
                          ))}
                        </div>
                        {product.reviewCount && (
                          <span className="text-sm text-neutral-stone-600">
                            ({product.reviewCount} reviews)
                          </span>
                        )}
                      </div>
                    )}

                    {/* Price */}
                    <p className="text-3xl font-bold text-deep-sage mb-4">
                      ${product.price.toFixed(2)}
                    </p>

                    {/* Description */}
                    <p className="text-neutral-stone-600 mb-6 line-clamp-4">{product.description}</p>

                    {/* Stock Status */}
                    <p className={`text-sm mb-4 ${product.stock > 0 ? 'text-green-600' : 'text-red-600'}`}>
                      {product.stock > 0 ? `${product.stock} in stock` : 'Out of stock'}
                    </p>

                    {/* Quantity Selector */}
                    {product.stock > 0 && (
                      <div className="flex items-center gap-4 mb-6">
                        <span className="text-sm font-medium text-neutral-stone-700">Quantity:</span>
                        <div className="flex items-center border border-neutral-stone-300 rounded-full">
                          <button
                            onClick={decrementQuantity}
                            disabled={quantity <= 1}
                            className="px-4 py-2 text-neutral-stone-600 hover:text-deep-sage disabled:opacity-30 disabled:cursor-not-allowed transition-colors"
                          >
                            −
                          </button>
                          <span className="px-4 py-2 min-w-[3rem] text-center font-medium">
                            {quantity}
                          </span>
                          <button
                            onClick={incrementQuantity}
                            disabled={quantity >= product.stock}
                            className="px-4 py-2 text-neutral-stone-600 hover:text-deep-sage disabled:opacity-30 disabled:cursor-not-allowed transition-colors"
                          >
                            +
                          </button>
                        </div>
                      </div>
                    )}

                    {/* Action Buttons */}
                    <div className="flex gap-3 mb-4">
                      <button
                        onClick={handleAddToCart}
                        disabled={product.stock === 0}
                        className="flex-1 flex items-center justify-center gap-2 bg-primary-sage text-white rounded-full px-6 py-3 font-medium hover:bg-deep-sage disabled:bg-neutral-stone-300 disabled:cursor-not-allowed transition-colors"
                      >
                        <ShoppingCart className="h-5 w-5" />
                        Add to Cart
                      </button>
                      <button
                        onClick={() => setIsFavorite(!isFavorite)}
                        className={`p-3 rounded-full border-2 transition-colors ${
                          isFavorite
                            ? 'bg-red-50 border-red-500 text-red-500'
                            : 'border-neutral-stone-300 text-neutral-stone-600 hover:border-red-300 hover:text-red-500'
                        }`}
                        aria-label="Add to favorites"
                      >
                        <Heart className={`h-5 w-5 ${isFavorite ? 'fill-current' : ''}`} />
                      </button>
                    </div>

                    {/* View Full Details Link */}
                    <Link
                      href={`/product/${product.id}`}
                      onClick={onClose}
                      className="flex items-center justify-center gap-2 text-primary-sage hover:text-deep-sage font-medium transition-colors"
                    >
                      View Full Details
                      <ExternalLink className="h-4 w-4" />
                    </Link>
                  </div>
                </div>
              </DialogPanel>
            </TransitionChild>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
}
