import type { Metadata } from "next";
import "./globals.css";
import dynamic from 'next/dynamic'
import { ToastProvider } from '@/lib/toast-context'
import ToastContainer from '@/components/ToastContainer'

const CartSlide = dynamic(() => import('@/components/CartSlide'), { ssr: false })

export const metadata: Metadata = {
  title: "Orchidillo - Premium Orchid Marketplace",
  description: "Discover rare and beautiful orchids from trusted sellers across the country",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className="h-full">
      <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
        <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet" />
      </head>
      <body className="min-h-screen bg-soft-peach text-gray-900 antialiased">
        <ToastProvider>
          {children}
          <CartSlide />
          <ToastContainer />
        </ToastProvider>
      </body>
    </html>
  );
}
