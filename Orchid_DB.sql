---------------------
-- 1. CORE USER & AUTHENTICATION TABLES
---------------------
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    role VARCHAR(20) NOT NULL CHECK (role IN ('CUSTOMER', 'SELLER', 'ADMIN', 'SUPPORT_AGENT')),
    stripe_connect_account_id VARCHAR(255),
    is_phone_verified BOOLEAN DEFAULT FALSE,
    is_email_verified BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    deleted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT stripe_id_for_sellers_only CHECK (
        (role = 'SELLER' AND stripe_connect_account_id IS NOT NULL) OR
        (role != 'SELLER' AND stripe_connect_account_id IS NULL)
    )
);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

CREATE TABLE user_verification_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    code VARCHAR(10) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('PHONE_2FA', 'EMAIL_VERIFICATION')),
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_verification_codes_user_type ON user_verification_codes(user_id, type);

---------------------
-- 2. SELLER STORE PROFILES
---------------------
CREATE TABLE stores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seller_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    store_name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(110) UNIQUE NOT NULL,
    profile_image_url TEXT,
    banner_image_url TEXT,
    about_text TEXT,
    return_policy_text TEXT,
    total_sales INTEGER DEFAULT 0,
    average_rating DECIMAL(3,2),
    is_public BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

---------------------
-- 3. PRODUCT CATALOG, TAGS & INVENTORY
---------------------
-- Categories for hierarchical navigation (Hamburger Menu)
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(110) UNIQUE NOT NULL,
    description TEXT,
    parent_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    image_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Tags for search and discovery
CREATE TABLE tags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) UNIQUE NOT NULL,
    display_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Main listing table
CREATE TABLE listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id UUID NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(210) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(12,2) NOT NULL CHECK (price > 0),
    compare_at_price DECIMAL(12,2) CHECK (compare_at_price IS NULL OR compare_at_price > price),
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity >= 0),
    attributes JSONB DEFAULT '{}'::jsonb,
    status VARCHAR(20) DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'ACTIVE', 'SOLD_OUT', 'ARCHIVED')),
    -- Generated column for tag text (for search)
    tags_search_text TEXT,
    -- Full-text search vector (Title: Weight A, Tags: Weight B, Description: Weight C)
    search_vector TSVECTOR GENERATED ALWAYS AS (
        setweight(to_tsvector('english', COALESCE(title, '')), 'A') ||
        setweight(to_tsvector('english', COALESCE(tags_search_text, '')), 'B') ||
        setweight(to_tsvector('english', COALESCE(description, '')), 'C')
    ) STORED,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT unique_slug_per_store UNIQUE (store_id, slug)
);
-- Listing indexes for performance
CREATE INDEX idx_listings_store ON listings(store_id);
CREATE INDEX idx_listings_category ON listings(category_id);
CREATE INDEX idx_listings_status ON listings(status) WHERE status = 'ACTIVE';
CREATE INDEX idx_listings_attributes ON listings USING GIN (attributes);
CREATE INDEX idx_listings_search ON listings USING GIN (search_vector);
-- Composite GiST index for fast filtering/sorting on search pages
CREATE INDEX idx_listings_filter_sort ON listings USING GiST (price, created_at, category_id) WHERE status = 'ACTIVE';

-- Link listings to tags (Many-to-Many)
CREATE TABLE listing_tags (
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    tag_id UUID NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (listing_id, tag_id)
);
CREATE INDEX idx_listing_tags_tag ON listing_tags(tag_id);

-- Listing images
CREATE TABLE listing_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    alt_text VARCHAR(255),
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_listing_images_listing ON listing_images(listing_id);

-- User favorites
CREATE TABLE user_favorites (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (user_id, listing_id)
);

---------------------
-- 4. ORDERS & PAYMENTS
---------------------
CREATE TABLE carts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    guest_token UUID,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT cart_owner_check CHECK (
        (user_id IS NOT NULL AND guest_token IS NULL) OR
        (user_id IS NULL AND guest_token IS NOT NULL)
    )
);
CREATE INDEX idx_carts_user ON carts(user_id);
CREATE INDEX idx_carts_guest ON carts(guest_token);

CREATE TABLE cart_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_id UUID NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(cart_id, listing_id)
);

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_number VARCHAR(50) UNIQUE NOT NULL,
    buyer_id UUID REFERENCES users(id) ON DELETE SET NULL,
    buyer_email VARCHAR(255) NOT NULL,
    shipping_address JSONB NOT NULL,
    items_total DECIMAL(12,2) NOT NULL,
    shipping_total DECIMAL(12,2) NOT NULL DEFAULT 0,
    tax_total DECIMAL(12,2) NOT NULL DEFAULT 0,
    order_total DECIMAL(12,2) NOT NULL,
    stripe_payment_intent_id VARCHAR(255) UNIQUE,
    stripe_charge_id VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'PROCESSING' CHECK (
        status IN ('PROCESSING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED')
    ),
    cancellation_reason TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_orders_buyer ON orders(buyer_id);
CREATE INDEX idx_orders_buyer_email ON orders(buyer_email);
CREATE INDEX idx_orders_status ON orders(status);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    listing_id UUID NOT NULL REFERENCES listings(id),
    store_id UUID NOT NULL REFERENCES stores(id),
    title VARCHAR(200) NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    quantity INTEGER NOT NULL,
    attributes_snapshot JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_store ON order_items(store_id);

CREATE TABLE order_fulfillments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_item_id UUID NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    carrier VARCHAR(100),
    tracking_number VARCHAR(255),
    shipped_at TIMESTAMPTZ,
    delivered_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE platform_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id),
    store_id UUID NOT NULL REFERENCES stores(id),
    gross_amount INTEGER NOT NULL,
    platform_fee_amount INTEGER NOT NULL,
    net_seller_amount INTEGER NOT NULL,
    stripe_transfer_id VARCHAR(255),
    status VARCHAR(30) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID_OUT', 'REFUNDED')),
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_transactions_store ON platform_transactions(store_id);

---------------------
-- 5. REVIEWS & RATINGS
---------------------
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_item_id UUID UNIQUE NOT NULL REFERENCES order_items(id),
    rating DECIMAL(2,1) NOT NULL CHECK (rating >= 1.0 AND rating <= 5.0),
    comment TEXT,
    is_approved BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

---------------------
-- 6. MESSAGING & SUPPORT
---------------------
CREATE TABLE conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID REFERENCES listings(id) ON DELETE CASCADE,
    buyer_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    seller_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    support_agent_id UUID REFERENCES users(id) ON DELETE SET NULL,
    status VARCHAR(20) DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'CLOSED')),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT conversation_type_check CHECK (
        (listing_id IS NOT NULL AND support_agent_id IS NULL) OR
        (listing_id IS NULL AND support_agent_id IS NOT NULL)
    )
);
CREATE INDEX idx_conversations_buyer ON conversations(buyer_id);
CREATE INDEX idx_conversations_seller ON conversations(seller_id);

CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    is_flagged BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_messages_conversation ON messages(conversation_id);
CREATE INDEX idx_messages_sender ON messages(sender_id);

CREATE TABLE moderated_keywords (
    keyword VARCHAR(100) PRIMARY KEY,
    action VARCHAR(20) DEFAULT 'FLAG' CHECK (action IN ('FLAG', 'BLOCK')),
    created_at TIMESTAMPTZ DEFAULT NOW()
);
-- Initial data for message moderation
INSERT INTO moderated_keywords (keyword, action) VALUES
('venmo', 'FLAG'),
('cashapp', 'FLAG'),
('paypal', 'FLAG'),
('zelle', 'FLAG'),
('@gmail.com', 'FLAG'),
('@yahoo.com', 'FLAG'),
('phone number', 'FLAG');

---------------------
-- 7. SEARCH SYNC LOG (For Azure AI Search Integration)
---------------------
-- Tracks listing changes for external search index syncing
CREATE TABLE search_index_sync_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    operation VARCHAR(10) NOT NULL CHECK (operation IN ('INSERT', 'UPDATE', 'DELETE')),
    is_processed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_search_sync_pending ON search_index_sync_log(is_processed) WHERE is_processed = FALSE;

---------------------
-- 8. ADMIN AUDIT LOG
---------------------
CREATE TABLE admin_audit_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    admin_id UUID NOT NULL REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id UUID,
    old_values JSONB,
    new_values JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

---------------------
-- 9. DATABASE TRIGGERS
---------------------
-- Trigger to automatically populate tags_search_text
CREATE OR REPLACE FUNCTION update_listing_tags_search_text()
RETURNS TRIGGER AS $$
BEGIN
    -- Update the tags_search_text for the listing when tags change
    UPDATE listings
    SET tags_search_text = (
        SELECT STRING_AGG(t.name, ' ')
        FROM listing_tags lt
        JOIN tags t ON lt.tag_id = t.id
        WHERE lt.listing_id = NEW.listing_id
    )
    WHERE id = NEW.listing_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_tags_search
    AFTER INSERT OR DELETE ON listing_tags
    FOR EACH ROW
    EXECUTE FUNCTION update_listing_tags_search_text();

-- Trigger to log changes for external search sync (Azure AI Search)
CREATE OR REPLACE FUNCTION log_listing_for_search_sync()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO search_index_sync_log (listing_id, operation)
        VALUES (OLD.id, 'DELETE');
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO search_index_sync_log (listing_id, operation)
        VALUES (NEW.id, 'UPDATE');
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_listing_search_sync
    AFTER INSERT OR UPDATE OR DELETE ON listings
    FOR EACH ROW
    EXECUTE FUNCTION log_listing_for_search_sync();