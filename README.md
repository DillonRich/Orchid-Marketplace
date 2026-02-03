# 🌸 Orchidillo - Orchid Marketplace Platform

A full-stack e-commerce marketplace platform specifically designed for orchid enthusiasts, collectors, and sellers. Built with modern technologies and scalable architecture to support a niche community of orchid lovers.

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen?logo=springboot)
![Next.js](https://img.shields.io/badge/Next.js-14-black?logo=next.js)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue?logo=typescript)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql)
![Tailwind CSS](https://img.shields.io/badge/Tailwind-3.4-38B2AC?logo=tailwind-css)

---

## 📋 Table of Contents
- [Overview](#-overview)
- [Technical Architecture](#%EF%B8%8F-technical-architecture)
- [Tech Stack](#-tech-stack)
- [Database Schema](#-database-schema)
- [Key Features](#-key-features)
- [API Architecture](#-api-architecture)
- [Frontend Architecture](#-frontend-architecture)
- [Security Implementation](#-security-implementation)
- [Payment Integration](#-payment-integration)
- [Cloud Infrastructure](#%EF%B8%8F-cloud-infrastructure)
- [Development Highlights](#-development-highlights)

---

## 🎯 Overview

Orchidillo is a specialized e-commerce platform that connects orchid sellers with collectors and enthusiasts. The platform addresses the unique needs of the orchid community by providing:

- **Seller-focused marketplace** with dedicated storefronts
- **Advanced search and filtering** for specific orchid varieties
- **Secure payment processing** via Stripe Connect
- **Real-time inventory management**
- **Rating and review system** for quality assurance
- **Multi-role user system** (Customer, Seller, Admin, Support)

**Business Context:** The platform was designed to validate demand in the orchid marketplace niche before full-scale development, following lean startup principles.

---

## 🏗️ Technical Architecture

### System Design
```
┌─────────────────────────────────────────────────────────────┐
│                      Frontend Layer                          │
│  Next.js 14 (React) + TypeScript + Tailwind CSS             │
│  - Server-Side Rendering (SSR)                               │
│  - Client-Side Navigation                                    │
│  - Responsive Design (Mobile-First)                          │
└────────────────────┬────────────────────────────────────────┘
                     │
                     │ REST API (JSON)
                     │
┌────────────────────▼────────────────────────────────────────┐
│                     Backend Layer                            │
│  Spring Boot 3.5.9 (Java 17)                                │
│  - RESTful API Controllers                                   │
│  - JWT Authentication                                        │
│  - Business Logic Services                                   │
│  - Data Validation & Security                                │
└────────────────────┬────────────────────────────────────────┘
                     │
                     │ JPA/Hibernate
                     │
┌────────────────────▼────────────────────────────────────────┐
│                    Data Layer                                │
│  PostgreSQL 17 (Production) / H2 (Development)              │
│  - Normalized Relational Schema                              │
│  - Full-Text Search (PostgreSQL tsvector)                    │
│  - JSONB for flexible attributes                             │
│  - Referential Integrity                                     │
└─────────────────────────────────────────────────────────────┘
```

### Deployment Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    Azure Cloud Platform                      │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌─────────────────┐      ┌──────────────────┐             │
│  │  Static Web App │      │  Container Apps  │             │
│  │  (Next.js)      │◄────►│  (Spring Boot)   │             │
│  └─────────────────┘      └────────┬─────────┘             │
│                                     │                         │
│  ┌─────────────────┐      ┌────────▼─────────┐             │
│  │  Blob Storage   │      │  PostgreSQL      │             │
│  │  (Images)       │      │  Flexible Server │             │
│  └─────────────────┘      └──────────────────┘             │
│                                                               │
│  ┌─────────────────┐      ┌──────────────────┐             │
│  │  Redis Cache    │      │  Key Vault       │             │
│  │  (Sessions)     │      │  (Secrets)       │             │
│  └─────────────────┘      └──────────────────┘             │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Core programming language |
| **Spring Boot** | 3.5.9 | Application framework |
| **Spring Security** | 6.x | Authentication & authorization |
| **Spring Data JPA** | 3.x | Database ORM |
| **Hibernate** | 6.x | JPA implementation |
| **PostgreSQL** | 17 | Production database |
| **H2 Database** | 2.x | Development/testing database |
| **JWT (jjwt)** | 0.12.x | Token-based authentication |
| **Stripe Java SDK** | 24.16.0 | Payment processing |
| **Maven** | 3.9+ | Build & dependency management |
| **SpringDoc OpenAPI** | 2.x | API documentation (Swagger) |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Next.js** | 14 | React framework (App Router) |
| **React** | 18 | UI library |
| **TypeScript** | 5.0 | Type-safe JavaScript |
| **Tailwind CSS** | 3.4 | Utility-first CSS framework |
| **Stripe.js** | Latest | Payment UI components |
| **Axios** | Latest | HTTP client |
| **React Context API** | - | State management (Auth, Cart) |

### DevOps & Cloud
| Technology | Purpose |
|------------|---------|
| **Azure Container Apps** | Backend hosting (containerized) |
| **Azure Static Web Apps** | Frontend hosting (CDN) |
| **Azure PostgreSQL** | Managed database |
| **Azure Blob Storage** | Image & file storage |
| **Azure Redis Cache** | Session & cache management |
| **Azure Key Vault** | Secret management |
| **Docker** | Containerization |

---

## 💾 Database Schema

### Entity Relationship Overview

The database uses a normalized PostgreSQL schema with 16 core tables designed for scalability and data integrity.

#### Core Entities

**1. User Management**
```sql
users (id, email, password_hash, full_name, role, stripe_connect_account_id)
├── roles: CUSTOMER, SELLER, ADMIN, SUPPORT_AGENT
├── authentication: JWT-based with Spring Security
└── verification: user_verification_codes (2FA, email verification)
```

**2. Store Management**
```sql
stores (id, seller_id, store_name, slug, profile_image_url, about_text)
├── One-to-one relationship with users (role=SELLER)
├── Public storefront with customizable branding
└── Sales tracking and ratings
```

**3. Product Catalog**
```sql
listings (id, store_id, category_id, title, slug, description, price, quantity)
├── Full-text search: search_vector (tsvector for PostgreSQL)
├── Attributes: JSONB column for flexible product properties
├── Status: DRAFT, ACTIVE, SOLD_OUT, ARCHIVED
└── Image management: listing_images (multiple images per listing)
```

**4. Categories & Tags**
```sql
categories (id, name, slug, parent_id, image_url)
├── Hierarchical structure (self-referencing parent_id)
└── Used for navigation and filtering

tags (id, name, display_name)
└── listing_tags: Many-to-many relationship with listings
```

**5. Shopping Cart**
```sql
carts (id, user_id, session_id, created_at)
└── cart_items (id, cart_id, listing_id, quantity, price_snapshot)
    ├── Price snapshot: Captures price at time of add-to-cart
    └── Real-time inventory validation
```

**6. Order Management**
```sql
orders (id, user_id, status, total_amount, stripe_payment_intent_id)
├── Status flow: PENDING → PAID → SHIPPED → DELIVERED → COMPLETED
├── order_items (id, order_id, listing_id, quantity, price_at_purchase)
└── order_status_history (audit trail for status changes)
```

**7. Reviews & Ratings**
```sql
reviews (id, user_id, listing_id, rating, title, comment, created_at)
└── Ratings: 1-5 stars with text review
```

### Key Schema Features

**Full-Text Search (PostgreSQL)**
```sql
-- Generated tsvector column for efficient search
search_vector TSVECTOR GENERATED ALWAYS AS (
    setweight(to_tsvector('english', COALESCE(title, '')), 'A') ||
    setweight(to_tsvector('english', COALESCE(tags_search_text, '')), 'B') ||
    setweight(to_tsvector('english', COALESCE(description, '')), 'C')
) STORED

-- GIN index for fast search
CREATE INDEX idx_listings_search_vector ON listings USING GIN(search_vector);
```

**JSONB for Flexible Attributes**
```sql
-- Product attributes stored as JSONB for flexibility
attributes JSONB DEFAULT '{}'::jsonb

-- Example data:
{
  "orchid_type": "Phalaenopsis",
  "bloom_color": "white",
  "pot_size": "4 inch",
  "light_requirements": "medium to bright indirect",
  "care_level": "beginner"
}
```

**Performance Indexing**
```sql
-- Performance-critical indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_listings_store_category ON listings(store_id, category_id);
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
CREATE INDEX idx_cart_items_cart_listing ON cart_items(cart_id, listing_id);
```

---

## ✨ Key Features

### For Customers
- **Advanced Search**: Full-text search with filters (category, price, location, seller rating)
- **Shopping Cart**: Persistent cart (session-based for guests, user-linked for authenticated)
- **Secure Checkout**: Stripe payment integration with saved payment methods
- **Order Tracking**: Real-time order status updates with notification system
- **Review System**: Post reviews and ratings for purchased products

### For Sellers
- **Store Management**: Create and customize branded storefronts
- **Product Listings**: Easy product upload with image gallery and rich descriptions
- **Inventory Management**: Real-time stock tracking with low-stock alerts
- **Sales Dashboard**: Analytics on sales, revenue, and product performance
- **Order Fulfillment**: Manage orders from payment to shipping
- **Stripe Connect**: Direct payouts to seller bank accounts (80% revenue share)

### For Admins
- **User Management**: View, edit, suspend, or delete users
- **Content Moderation**: Review and approve/reject listings
- **Platform Analytics**: Dashboard with key metrics (GMV, active users, conversion rates)
- **System Configuration**: Manage categories, tags, and platform settings

---

## 🔌 API Architecture

### RESTful API Design

**Base URL**: `/api`  
**Authentication**: JWT Bearer token in `Authorization` header

### Core API Endpoints

#### Authentication (`/api/auth`)
```http
POST   /auth/register          # Create new user account
POST   /auth/login             # Authenticate and get JWT token
POST   /auth/logout            # Invalidate session
GET    /auth/me                # Get current user profile
PUT    /auth/me                # Update user profile
```

#### Products (`/api/listings`)
```http
GET    /listings               # List all active listings (paginated)
GET    /listings/{id}          # Get listing by ID
POST   /listings               # Create new listing (SELLER only)
PUT    /listings/{id}          # Update listing (SELLER only)
DELETE /listings/{id}          # Delete listing (SELLER only)
GET    /listings/search        # Search with query params
```

#### Cart (`/api/cart`)
```http
GET    /cart                   # Get current user's cart
POST   /cart/items             # Add item to cart
PUT    /cart/items/{id}        # Update cart item quantity
DELETE /cart/items/{id}        # Remove item from cart
DELETE /cart                   # Clear entire cart
```

#### Orders (`/api/orders`)
```http
GET    /orders                 # Get user's orders (paginated)
GET    /orders/{id}            # Get order details
POST   /orders                 # Create order from cart
PUT    /orders/{id}/status     # Update order status (SELLER/ADMIN)
```

#### Payments (`/api/payments`)
```http
POST   /payments/create-intent      # Create Stripe PaymentIntent
POST   /payments/confirm            # Confirm payment
GET    /payments/{id}/status        # Check payment status
POST   /payments/webhook            # Stripe webhook endpoint
```

### API Response Format

**Success Response**
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "name": "Product Name",
    "price": 29.99
  },
  "message": "Operation successful"
}
```

**Error Response**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [
      {
        "field": "price",
        "message": "Price must be greater than 0"
      }
    ]
  },
  "timestamp": "2026-02-03T10:30:00Z"
}
```

---

## 🎨 Frontend Architecture

### Next.js App Router Structure

```
app/
├── (auth)/                    # Authentication group
│   ├── login/                 # Login page
│   ├── register/              # Registration page
│   └── layout.tsx             # Auth-specific layout
│
├── (shop)/                    # Main shopping experience
│   ├── page.tsx               # Homepage (product grid)
│   ├── products/[id]/         # Product detail page
│   ├── category/[slug]/       # Category browse page
│   └── search/                # Search results page
│
├── cart/                      # Shopping cart page
├── checkout/                  # Checkout flow
│   ├── page.tsx               # Checkout form
│   ├── success/               # Payment success page
│   └── cancel/                # Payment cancelled page
│
├── account/                   # Customer account section
│   ├── profile/               # User profile management
│   ├── orders/                # Order history
│   └── reviews/               # User's reviews
│
└── seller/                    # Seller dashboard
    ├── dashboard/             # Sales overview
    ├── products/              # Product management
    ├── orders/                # Order fulfillment
    └── store/                 # Store settings
```

### Component Architecture

```
components/
├── ui/                        # Base UI components
│   ├── Button.tsx
│   ├── Input.tsx
│   ├── Card.tsx
│   └── Modal.tsx
│
├── product/                   # Product-specific components
│   ├── ProductCard.tsx        # Product preview card
│   ├── ProductGrid.tsx        # Grid layout for products
│   ├── ProductDetail.tsx      # Full product view
│   └── ProductGallery.tsx     # Image gallery
│
├── cart/                      # Cart components
│   ├── CartDrawer.tsx         # Slide-out cart
│   ├── CartItem.tsx           # Individual cart item
│   └── CartSummary.tsx        # Price breakdown
│
└── checkout/                  # Checkout components
    ├── CheckoutForm.tsx       # Multi-step checkout
    ├── AddressForm.tsx        # Shipping address
    └── PaymentForm.tsx        # Stripe Elements integration
```

### State Management

```typescript
// Authentication context
interface AuthContextType {
  user: User | null
  login: (email: string, password: string) => Promise<void>
  logout: () => void
  register: (data: RegisterData) => Promise<void>
  isAuthenticated: boolean
}

// Cart context
interface CartContextType {
  cart: Cart | null
  addItem: (listingId: string, quantity: number) => Promise<void>
  removeItem: (itemId: string) => Promise<void>
  updateQuantity: (itemId: string, quantity: number) => Promise<void>
  itemCount: number
  total: number
}
```

---

## 🔒 Security Implementation

### Backend Security

**1. Authentication: JWT (JSON Web Tokens)**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf().disable() // Disabled for stateless JWT
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/listings").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthFilter, 
                             UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

**2. Password Security**
- **BCrypt Hashing**: `BCryptPasswordEncoder` with 10 rounds
- **Password Requirements**: Minimum 8 characters, complexity rules
- **Password Reset**: Time-limited tokens (15 minutes expiry)

**3. Authorization**
```java
// Method-level security
@PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
@PostMapping("/listings")
public ResponseEntity<Listing> createListing(
    @Valid @RequestBody ListingRequest request) {
    // Only sellers and admins can create listings
}

@PreAuthorize("@listingService.isOwner(authentication, #id)")
@PutMapping("/listings/{id}")
public ResponseEntity<Listing> updateListing(
    @PathVariable String id, 
    @RequestBody ListingRequest request) {
    // Only the listing owner can update it
}
```

**4. Input Validation**
```java
public class ListingRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be 5-200 characters")
    private String title;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least $0.01")
    @DecimalMax(value = "999999.99")
    private BigDecimal price;
    
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}
```

**5. SQL Injection Prevention**
- JPA/Hibernate with parameterized queries (prepared statements)
- No raw SQL or string concatenation for queries

**6. CORS Configuration**
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(
        "http://localhost:3000", 
        "https://orchidillo.com"
    ));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    config.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = 
        new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", config);
    return source;
}
```

---

## 💳 Payment Integration

### Stripe Connect Architecture

**Payment Flow**
```
1. Customer adds products to cart
2. Clicks "Checkout"
3. Frontend calls /api/payments/create-intent
4. Backend creates Stripe PaymentIntent
5. Frontend displays Stripe Elements (card input form)
6. Customer enters card details (PCI-compliant)
7. Stripe validates card and processes payment
8. Webhook confirms payment to backend
9. Backend creates Order record with status=PAID
10. Email confirmation sent to customer and seller
11. Seller receives 80% of sale (platform takes 20% fee)
```

**Backend Implementation**
```java
@Service
public class StripePaymentService {
    
    public PaymentIntent createPaymentIntent(Cart cart) {
        Stripe.apiKey = stripeSecretKey;
        
        // Calculate platform fee (20%)
        long amountInCents = cart.getTotalAmount()
            .multiply(new BigDecimal(100)).longValue();
        long platformFee = (long) (amountInCents * 0.20);
        
        PaymentIntentCreateParams params = 
            PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("usd")
                .setApplicationFeeAmount(platformFee)
                .setTransferData(
                    PaymentIntentCreateParams.TransferData.builder()
                        .setDestination(seller.getStripeConnectAccountId())
                        .build()
                )
                .putMetadata("orderId", order.getId().toString())
                .build();
        
        return PaymentIntent.create(params);
    }
}
```

**Frontend Integration**
```typescript
import { loadStripe } from '@stripe/stripe-js'
import { Elements, CardElement } from '@stripe/react-stripe-js'

const stripePromise = loadStripe(
  process.env.NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY!
)

export default function CheckoutPage() {
  const [clientSecret, setClientSecret] = useState('')
  
  useEffect(() => {
    api.post('/payments/create-intent', { cartId })
       .then(res => setClientSecret(res.data.clientSecret))
  }, [])
  
  return (
    <Elements stripe={stripePromise} options={{ clientSecret }}>
      <PaymentForm />
    </Elements>
  )
}
```

---

## ☁️ Cloud Infrastructure

### Azure Services Configuration

**1. Azure Container Apps (Backend)**
- **Container**: Spring Boot app (Java 17)
- **Scaling**: 0-10 instances based on HTTP traffic
- **Health Check**: `/actuator/health` endpoint
- **Environment Variables**: Injected from Key Vault

**2. Azure Static Web Apps (Frontend)**
- **Framework**: Next.js 14 with App Router
- **CDN**: Global edge network for fast delivery
- **SSL**: Automatic certificate management

**3. Azure PostgreSQL Flexible Server**
- **Version**: PostgreSQL 17
- **SKU**: Burstable B1ms (1 vCore, 2 GB RAM)
- **Storage**: 32 GB with auto-grow enabled
- **Backup**: 7-day retention with point-in-time restore
- **Security**: SSL enforced, firewall rules

**4. Azure Blob Storage**
- **Purpose**: Product images and user uploads
- **Access**: Public read for product images
- **CDN**: Azure CDN integration

**5. Azure Redis Cache**
- **SKU**: Basic C0 (250 MB)
- **Purpose**: Session storage, API response caching
- **Eviction**: Least Recently Used (LRU) policy

**6. Azure Key Vault**
- **Purpose**: Store secrets (DB password, JWT secret, Stripe keys)
- **Access**: Managed Identity for Container Apps

---

## 🚀 Development Highlights

### Technical Achievements

**1. Full-Stack Development**
- Built complete application from database design to frontend UI
- RESTful API with 30+ endpoints
- Type-safe frontend with TypeScript
- Responsive design (mobile-first approach)

**2. Database Design**
- Normalized schema with 16 tables
- Full-text search implementation (PostgreSQL tsvector)
- JSONB for flexible product attributes
- Optimized indexes for query performance

**3. Authentication & Security**
- JWT-based authentication with Spring Security
- Role-based access control (RBAC)
- Password encryption with BCrypt
- Input validation and sanitization

**4. Payment Integration**
- Stripe Connect implementation
- Multi-seller payment splitting (80/20)
- PCI-compliant payment processing
- Webhook handling for async payment confirmation

**5. Cloud Deployment**
- Containerized backend (Docker)
- Azure Container Apps with autoscaling
- Static Web App for frontend (CDN)
- Managed PostgreSQL database

**6. Code Quality**
- Clean architecture (Controller → Service → Repository)
- Dependency injection (Spring IoC)
- RESTful API design principles
- Error handling and logging

### Architectural Decisions

**Why Spring Boot?**
- Robust ecosystem for enterprise Java
- Built-in security and validation
- Excellent database integration (JPA/Hibernate)
- Production-ready features (Actuator, metrics)

**Why Next.js?**
- Server-side rendering for SEO
- File-based routing (App Router)
- Built-in image optimization
- Great developer experience

**Why PostgreSQL?**
- Full-text search capabilities (tsvector)
- JSONB for flexible attributes
- Strong ACID compliance
- Excellent performance at scale

**Why Azure?**
- Integrated PaaS services
- Container Apps for easy scaling
- Managed database with automatic backups
- Cost-effective for startups

### Challenges Overcome

1. **Multi-seller payment architecture**: Implemented Stripe Connect with proper fee splitting
2. **Full-text search**: Optimized PostgreSQL tsvector for fast product search
3. **Cart persistence**: Designed system for both guest and authenticated users
4. **Image handling**: Azure Blob Storage integration with CDN
5. **Authentication flow**: JWT implementation with proper security measures

---

## 📊 Performance Metrics (Target)

- **API Response Time**: < 200ms (average)
- **Frontend Load Time**: < 2s (First Contentful Paint)
- **Database Queries**: Optimized with indexes (< 50ms)
- **Concurrent Users**: Supports 1000+ with autoscaling
- **Uptime**: 99.9% SLA (Azure Container Apps)

---

## 📝 License

This project is for portfolio demonstration purposes only. All rights reserved.

---

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- Next.js team for the amazing framework
- Stripe for comprehensive payment APIs
- Azure for reliable cloud infrastructure

---

**Note**: This is a portfolio project demonstrating technical capabilities. The codebase showcases modern development practices, cloud architecture, and full-stack engineering skills.
