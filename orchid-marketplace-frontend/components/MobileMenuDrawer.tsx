'use client';

import { Dialog, DialogPanel, Transition, TransitionChild } from '@headlessui/react';
import { Fragment } from 'react';
import { X, Home, ShoppingBag, Heart, User, MessageCircle, Search, ChevronRight } from 'lucide-react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

interface MobileMenuDrawerProps {
  isOpen: boolean;
  onClose: () => void;
  isAuthenticated?: boolean;
  userRole?: 'customer' | 'seller' | 'admin';
}

export default function MobileMenuDrawer({
  isOpen,
  onClose,
  isAuthenticated = false,
  userRole = 'customer',
}: MobileMenuDrawerProps) {
  const router = useRouter();

  const categories = [
    { name: 'Phalaenopsis', slug: 'phalaenopsis' },
    { name: 'Cattleya', slug: 'cattleya' },
    { name: 'Dendrobium', slug: 'dendrobium' },
    { name: 'Oncidium', slug: 'oncidium' },
    { name: 'Vanda', slug: 'vanda' },
    { name: 'Paphiopedilum', slug: 'paphiopedilum' },
  ];

  const handleNavigation = (href: string) => {
    onClose();
    router.push(href);
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

        <div className="fixed inset-0 overflow-hidden">
          <div className="absolute inset-0 overflow-hidden">
            <div className="pointer-events-none fixed inset-y-0 left-0 flex max-w-full">
              <TransitionChild
                as={Fragment}
                enter="transform transition ease-in-out duration-300"
                enterFrom="-translate-x-full"
                enterTo="translate-x-0"
                leave="transform transition ease-in-out duration-300"
                leaveFrom="translate-x-0"
                leaveTo="-translate-x-full"
              >
                <DialogPanel className="pointer-events-auto w-screen max-w-sm">
                  <div className="flex h-full flex-col bg-white shadow-xl">
                    {/* Header */}
                    <div className="flex items-center justify-between border-b border-neutral-stone-200 px-6 py-4">
                      <h2 className="text-xl font-semibold text-deep-sage">Menu</h2>
                      <button
                        onClick={onClose}
                        className="rounded-full p-1 text-neutral-stone-400 hover:bg-neutral-stone-100 hover:text-deep-sage transition-colors"
                        aria-label="Close menu"
                      >
                        <X className="h-6 w-6" />
                      </button>
                    </div>

                    {/* Content */}
                    <div className="flex-1 overflow-y-auto">
                      {/* Search Bar */}
                      <div className="p-4 border-b border-neutral-stone-200">
                        <div className="relative">
                          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-neutral-stone-400" />
                          <input
                            type="search"
                            placeholder="Search orchids..."
                            className="w-full pl-10 pr-4 py-2 border border-neutral-stone-300 rounded-full focus:outline-none focus:ring-2 focus:ring-primary-sage"
                            onKeyDown={(e) => {
                              if (e.key === 'Enter') {
                                handleNavigation(`/products?q=${e.currentTarget.value}`);
                              }
                            }}
                          />
                        </div>
                      </div>

                      {/* Main Navigation */}
                      <nav className="p-4 space-y-1">
                        <Link
                          href="/"
                          onClick={onClose}
                          className="flex items-center gap-3 px-4 py-3 text-neutral-stone-700 hover:bg-neutral-stone-50 hover:text-deep-sage rounded-lg transition-colors"
                        >
                          <Home className="h-5 w-5" />
                          <span className="font-medium">Home</span>
                        </Link>

                        <Link
                          href="/products"
                          onClick={onClose}
                          className="flex items-center gap-3 px-4 py-3 text-neutral-stone-700 hover:bg-neutral-stone-50 hover:text-deep-sage rounded-lg transition-colors"
                        >
                          <ShoppingBag className="h-5 w-5" />
                          <span className="font-medium">Shop All</span>
                        </Link>

                        {isAuthenticated && (
                          <>
                            <Link
                              href="/favorites"
                              onClick={onClose}
                              className="flex items-center gap-3 px-4 py-3 text-neutral-stone-700 hover:bg-neutral-stone-50 hover:text-deep-sage rounded-lg transition-colors"
                            >
                              <Heart className="h-5 w-5" />
                              <span className="font-medium">Favorites</span>
                            </Link>

                            <Link
                              href="/messages"
                              onClick={onClose}
                              className="flex items-center gap-3 px-4 py-3 text-neutral-stone-700 hover:bg-neutral-stone-50 hover:text-deep-sage rounded-lg transition-colors"
                            >
                              <MessageCircle className="h-5 w-5" />
                              <span className="font-medium">Messages</span>
                            </Link>

                            <Link
                              href="/account"
                              onClick={onClose}
                              className="flex items-center gap-3 px-4 py-3 text-neutral-stone-700 hover:bg-neutral-stone-50 hover:text-deep-sage rounded-lg transition-colors"
                            >
                              <User className="h-5 w-5" />
                              <span className="font-medium">My Account</span>
                            </Link>
                          </>
                        )}
                      </nav>

                      {/* Categories */}
                      <div className="p-4 border-t border-neutral-stone-200">
                        <h3 className="px-4 mb-2 text-sm font-semibold text-neutral-stone-500 uppercase tracking-wide">
                          Shop by Type
                        </h3>
                        <div className="space-y-1">
                          {categories.map((category) => (
                            <Link
                              key={category.slug}
                              href={`/products?category=${category.slug}`}
                              onClick={onClose}
                              className="flex items-center justify-between px-4 py-2.5 text-neutral-stone-700 hover:bg-neutral-stone-50 hover:text-deep-sage rounded-lg transition-colors"
                            >
                              <span>{category.name}</span>
                              <ChevronRight className="h-4 w-4" />
                            </Link>
                          ))}
                        </div>
                      </div>
                    </div>

                    {/* Footer Actions */}
                    <div className="border-t border-neutral-stone-200 p-4 space-y-2">
                      {!isAuthenticated ? (
                        <>
                          <button
                            onClick={() => handleNavigation('/login')}
                            className="w-full bg-primary-sage text-white rounded-full py-2.5 font-medium hover:bg-deep-sage transition-colors"
                          >
                            Sign In
                          </button>
                          <button
                            onClick={() => handleNavigation('/register')}
                            className="w-full border-2 border-neutral-stone-300 text-neutral-stone-700 rounded-full py-2.5 font-medium hover:bg-neutral-stone-50 transition-colors"
                          >
                            Create Account
                          </button>
                        </>
                      ) : (
                        <>
                          {userRole === 'seller' && (
                            <button
                              onClick={() => handleNavigation('/seller/dashboard')}
                              className="w-full bg-primary-peach text-white rounded-full py-2.5 font-medium hover:bg-primary-peach/90 transition-colors"
                            >
                              Seller Dashboard
                            </button>
                          )}
                          {userRole === 'admin' && (
                            <button
                              onClick={() => handleNavigation('/admin/dashboard')}
                              className="w-full bg-deep-sage text-white rounded-full py-2.5 font-medium hover:bg-deep-sage/90 transition-colors"
                            >
                              Admin Dashboard
                            </button>
                          )}
                        </>
                      )}
                    </div>
                  </div>
                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
}
