// User
export interface User {
  id: string;
  email: string;
  fullName: string;
  role: "CUSTOMER" | "SELLER" | "ADMIN" | "SUPPORT_AGENT";
  createdAt: string;
  updatedAt: string;
}

// Address
export type AddressType = "BILLING" | "SHIPPING" | "BOTH";

export interface Address {
  id: string;
  userId: string;
  addressType: AddressType;
  recipientName: string;
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  phoneNumber: string;
  isDefault: boolean;
  createdAt: string;
  updatedAt: string;
}

// Payment Method
export interface PaymentMethod {
  id: string;
  userId: string;
  brand: string;
  last4: string;
  expMonth: number;
  expYear: number;
  isDefault: boolean;
  stripePaymentMethodId: string;
  createdAt: string;
  updatedAt: string;
}

// Store
export interface Store {
  id: string;
  sellerId: string;
  storeName: string;
  slug: string;
  description?: string;
  logoUrl?: string;
  averageRating: number;
  isActive: boolean;
  stripeAccountId?: string;
  createdAt: string;
  updatedAt: string;
}

// Category
export interface Category {
  id: string;
  name: string;
  displayName: string;
  description?: string;
  parentCategoryId?: string;
  iconUrl?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

// Product
export interface Product {
  id: string;
  storeId: string;
  categoryId: string;
  title: string;
  description: string;
  imageUrl: string;
  basePrice: number;
  stockQuantity: number;
  weight?: number;
  dimensions?: string;
  sku: string;
  isActive: boolean;
  averageRating: number;
  reviewCount: number;
  ratingDistribution?: {
    5: number;
    4: number;
    3: number;
    2: number;
    1: number;
  };
  createdAt: string;
  updatedAt: string;
}

// Product Image
export interface ProductImage {
  id: string;
  productId: string;
  imageUrl: string;
  altText?: string;
  displayOrder: number;
  createdAt: string;
}

// Product Review
export interface ProductReview {
  id: string;
  productId: string;
  userId: string;
  rating: number;
  title: string;
  comment: string;
  helpful: number;
  createdAt: string;
  updatedAt: string;
}

// Shipping Option
export type ShippingMethod = "STANDARD" | "EXPEDITED" | "OVERNIGHT" | "PICKUP";

export interface ShippingOption {
  id: string;
  productId: string;
  optionName: string;
  shippingMethod: ShippingMethod;
  shippingCost: number;
  estimatedDays: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

// Cart
export interface CartItem {
  id: string;
  cartId: string;
  productId: string;
  quantity: number;
  shippingOptionId?: string;
  addedAt: string;
}

export interface Cart {
  id: string;
  userId: string;
  items: CartItem[];
  totalPrice: number;
  totalShippingCost: number;
  createdAt: string;
  updatedAt: string;
}

// Order
export type OrderStatus =
  | "PENDING"
  | "CONFIRMED"
  | "PROCESSING"
  | "SHIPPED"
  | "DELIVERED"
  | "CANCELLED"
  | "REFUNDED";

export interface Order {
  id: string;
  customerId: string;
  shippingAddressId: string;
  billingAddressId: string;
  totalAmount: number;
  status: OrderStatus;
  createdAt: string;
  updatedAt: string;
}

// Order Item
export type OrderItemStatus = "PENDING" | "PROCESSING" | "SHIPPED" | "DELIVERED" | "CANCELLED";

export interface OrderItem {
  id: string;
  orderId: string;
  productId: string;
  storeId: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
  shippingCost: number;
  status: OrderItemStatus;
  createdAt: string;
  updatedAt: string;
}

// Order Shipment
export interface OrderShipment {
  id: string;
  orderId: string;
  orderItemId: string;
  trackingNumber: string;
  carrier: string;
  estimatedDeliveryDate: string;
  actualDeliveryDate?: string;
  createdAt: string;
  updatedAt: string;
}

// Seller Ledger
export type SellerLedgerEntryType =
  | "SALE_SUBTOTAL"
  | "REFUND_SUBTOTAL"
  | "PLATFORM_FEE"
  | "SHIPPING_COLLECTED"
  | "ADJUSTMENT"
  | "SETTLEMENT";

export interface SellerLedgerEntry {
  id: string;
  storeId: string;
  type: SellerLedgerEntryType;
  amount: number;
  description: string;
  relatedOrderItemId?: string;
  createdAt: string;
}

// Notifications
export interface Notification {
  id: string;
  userId: string;
  title: string;
  message: string;
  type: string;
  relatedEntityId?: string;
  isRead: boolean;
  createdAt: string;
}

// Messages
export interface Message {
  id: string;
  senderId: string;
  recipientId: string;
  content: string;
  isRead: boolean;
  createdAt: string;
}

// Checkout Request/Response
export interface CheckoutRequest {
  shippingAddressId: string;
  billingAddressId: string;
}

export interface StripeCheckoutSessionResponse {
  sessionId: string;
  url: string;
  orderId: string;
}

// Seller Dashboard
export interface SellerDashboardMetrics {
  totalSales: number;
  totalRevenue: number;
  totalOrders: number;
  totalCustomers: number;
  averageOrderValue: number;
  pendingShipments: number;
  pendingReviews: number;
}

// Seller Finance Summary
export interface SellerFinanceSummary {
  totalRevenue: number;
  totalRefunds: number;
  totalFees: number;
  netAmount: number;
  fromDate: string;
  toDate: string;
}

// Error Response
export interface ErrorResponse {
  status: number;
  message: string;
  details?: Record<string, string>;
}
