-- ============================================
-- Orchid Marketplace - Sample Data Seed Script
-- ============================================
-- This script creates sample users, categories, stores, and products for testing

-- Create test users (passwords are 'password123' hashed)
-- Note: In production, use the registration endpoint which properly hashes passwords
INSERT INTO users (id, email, password_hash, name, role, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'customer@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeQ8TwB0FHbqzP3rLa8XDCnCiCVDwN1w2', 'John Customer', 'CUSTOMER', NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'seller@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeQ8TwB0FHbqzP3rLa8XDCnCiCVDwN1w2', 'Jane Seller', 'SELLER', NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeQ8TwB0FHbqzP3rLa8XDCnCiCVDwN1w2', 'Admin User', 'ADMIN', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create categories
INSERT INTO categories (id, name, slug, description, created_at, updated_at) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Phalaenopsis', 'phalaenopsis', 'Moth orchids - perfect for beginners', NOW(), NOW()),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Cattleya', 'cattleya', 'Fragrant orchids with large blooms', NOW(), NOW()),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Dendrobium', 'dendrobium', 'Versatile and easy-care orchids', NOW(), NOW()),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Oncidium', 'oncidium', 'Dancing lady orchids', NOW(), NOW()),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Vanda', 'vanda', 'Tropical hanging orchids', NOW(), NOW()),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Cymbidium', 'cymbidium', 'Cool-growing spray orchids', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create stores
INSERT INTO stores (id, user_id, business_name, slug, description, verified, created_at, updated_at) VALUES
('99999999-9999-9999-9999-999999999999', '22222222-2222-2222-2222-222222222222', 'Orchid Paradise', 'orchid-paradise', 'Premium orchids from expert growers', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create products
INSERT INTO products (id, store_id, category_id, name, slug, description, base_price, stock_quantity, is_active, created_at, updated_at) VALUES
-- Phalaenopsis orchids
('sunset-phalaenopsis-premium-bloom', '99999999-9999-9999-9999-999999999999', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 
 'Sunset Phalaenopsis Premium Bloom', 'sunset-phalaenopsis-premium-bloom',
 'A stunning sunset-colored Phalaenopsis orchid featuring warm peach and golden hues. This premium bloom specimen is currently flowering with 8-12 blooms on a graceful arching spike. Perfect for adding a touch of elegance to any space.

• Mature plant with multiple spikes
• Currently in full bloom
• Easy care - perfect for beginners
• Comes in decorative pot
• Light: Bright, indirect light
• Water: Once per week', 
 59.99, 25, true, NOW(), NOW()),

('white-cascade-phalaenopsis', '99999999-9999-9999-9999-999999999999', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
 'White Cascade Phalaenopsis', 'white-cascade-phalaenopsis',
 'Pure white Phalaenopsis with cascading blooms. Classic elegance with 15+ pristine white flowers on double spikes. A timeless addition to any home or office.

• Double spike for extended blooming
• Pure white petals with yellow center
• Low maintenance
• 4-6 month bloom period
• Pet-friendly
• Includes care guide',
 49.99, 30, true, NOW(), NOW()),

-- Cattleya orchids
('fragrant-cattleya-orchid-hybrid', '99999999-9999-9999-9999-999999999999', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
 'Fragrant Cattleya Orchid Hybrid', 'fragrant-cattleya-orchid-hybrid',
 'Exquisite Cattleya hybrid with intensely fragrant lavender blooms. Large 5-6 inch flowers with ruffled edges and golden throat. This is a collector-grade specimen perfect for experienced orchid enthusiasts.

• Intoxicating natural fragrance
• Large 5-6" blooms
• Ruffled petal edges
• Bright lavender with gold center
• Mature blooming size
• Includes humidity tray',
 89.99, 15, true, NOW(), NOW()),

-- Dendrobium orchids
('purple-dendrobium-cluster', '99999999-9999-9999-9999-999999999999', 'cccccccc-cccc-cccc-cccc-cccccccccccc',
 'Purple Dendrobium Cluster', 'purple-dendrobium-cluster',
 'Vibrant purple Dendrobium with multiple flowering stems. Each stem produces 20-30 blooms creating a spectacular display. Very easy to care for and perfect for intermediate growers.

• Multiple stems in bloom
• 60+ total flowers
• Rich purple color
• Long-lasting blooms (2-3 months)
• Tolerates varying conditions
• Beginner-friendly',
 44.99, 40, true, NOW(), NOW()),

-- Oncidium orchids
('dancing-lady-oncidium', '99999999-9999-9999-9999-999999999999', 'dddddddd-dddd-dddd-dddd-dddddddddddd',
 'Dancing Lady Oncidium', 'dancing-lady-oncidium',
 'Cheerful yellow "dancing lady" Oncidium with cascading sprays of golden flowers. Hundreds of small blooms create a cloud of color. Easy to grow and blooms multiple times per year.

• 100+ blooms per spray
• Bright golden yellow
• Multiple bloom cycles yearly
• Compact growth habit
• Great for small spaces
• Very forgiving care',
 39.99, 35, true, NOW(), NOW()),

-- Vanda orchids
('deep-purple-vanda-special-edition', '99999999-9999-9999-9999-999999999999', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
 'Deep Purple Vanda Special Edition', 'deep-purple-vanda-special-edition',
 'Breathtaking deep purple Vanda orchid with striking tessellated patterns. This special edition cultivar features large 4-5 inch blooms with exceptional color depth and a subtle fragrance. Grown in a hanging basket for optimal root health.

• Rare tessellated pattern
• Deep royal purple
• Large 4-5" blooms
• Subtle sweet fragrance
• Hanging basket included
• Advanced care level',
 129.99, 8, true, NOW(), NOW()),

-- Cymbidium orchids
('mini-cymbidium-orchid-plant', '99999999-9999-9999-9999-999999999999', 'ffffffff-ffff-ffff-ffff-ffffffffffff',
 'Mini Cymbidium Orchid Plant', 'mini-cymbidium-orchid-plant',
 'Compact miniature Cymbidium with cascading spikes of pink and cream flowers. Perfect size for desks, shelves, or small spaces. Cool-tolerant variety that blooms in winter months.

• Miniature variety
• Cascading flower spikes
• Pink and cream bicolor
• Winter blooming
• Cold tolerant
• Perfect gift size',
 54.99, 20, true, NOW(), NOW()),

-- Additional popular varieties
('spotted-paphiopedilum', '99999999-9999-9999-9999-999999999999', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
 'Spotted Paphiopedilum Slipper Orchid', 'spotted-paphiopedilum',
 'Unique lady slipper orchid with distinctive spotted petals and pouch-shaped lip. Terrestrial orchid with fascinating form and long-lasting blooms. Perfect for collectors seeking something different.

• Unique pouch-shaped flower
• Spotted patterns
• Long-lasting blooms (6-8 weeks)
• Terrestrial growing habit
• Low light tolerant
• Conversation starter',
 69.99, 12, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create product images
INSERT INTO product_images (id, product_id, url, alt_text, display_order, created_at) VALUES
-- Sunset Phalaenopsis images
('img-001', 'sunset-phalaenopsis-premium-bloom', '/images/orchid1.jpg', 'Sunset Phalaenopsis main view', 1, NOW()),
('img-002', 'sunset-phalaenopsis-premium-bloom', '/images/orchid2.jpg', 'Close-up of blooms', 2, NOW()),
('img-003', 'sunset-phalaenopsis-premium-bloom', '/images/orchid3.jpg', 'Side profile', 3, NOW()),
('img-004', 'sunset-phalaenopsis-premium-bloom', '/images/orchid4.jpg', 'In decorative setting', 4, NOW()),

-- White Cascade images
('img-011', 'white-cascade-phalaenopsis', '/images/orchid5.jpg', 'White Cascade main view', 1, NOW()),
('img-012', 'white-cascade-phalaenopsis', '/images/orchid2.jpg', 'Double spike detail', 2, NOW()),

-- Fragrant Cattleya images
('img-021', 'fragrant-cattleya-orchid-hybrid', '/images/orchid3.jpg', 'Cattleya main bloom', 1, NOW()),
('img-022', 'fragrant-cattleya-orchid-hybrid', '/images/orchid4.jpg', 'Detail of ruffled edges', 2, NOW()),
('img-023', 'fragrant-cattleya-orchid-hybrid', '/images/orchid1.jpg', 'Full plant view', 3, NOW()),

-- Purple Dendrobium images
('img-031', 'purple-dendrobium-cluster', '/images/orchid4.jpg', 'Purple Dendrobium cluster', 1, NOW()),
('img-032', 'purple-dendrobium-cluster', '/images/orchid5.jpg', 'Multiple stems', 2, NOW()),

-- Dancing Lady images
('img-041', 'dancing-lady-oncidium', '/images/orchid1.jpg', 'Dancing Lady spray', 1, NOW()),
('img-042', 'dancing-lady-oncidium', '/images/orchid2.jpg', 'Golden blooms close-up', 2, NOW()),

-- Deep Purple Vanda images
('img-051', 'deep-purple-vanda-special-edition', '/images/orchid3.jpg', 'Vanda main view', 1, NOW()),
('img-052', 'deep-purple-vanda-special-edition', '/images/orchid4.jpg', 'Tessellated pattern detail', 2, NOW()),
('img-053', 'deep-purple-vanda-special-edition', '/images/orchid5.jpg', 'Hanging basket display', 3, NOW()),

-- Mini Cymbidium images
('img-061', 'mini-cymbidium-orchid-plant', '/images/orchid2.jpg', 'Mini Cymbidium main', 1, NOW()),
('img-062', 'mini-cymbidium-orchid-plant', '/images/orchid1.jpg', 'Pink and cream blooms', 2, NOW()),

-- Spotted Paphiopedilum images
('img-071', 'spotted-paphiopedilum', '/images/orchid5.jpg', 'Slipper orchid main', 1, NOW()),
('img-072', 'spotted-paphiopedilum', '/images/orchid3.jpg', 'Spotted pattern detail', 2, NOW())
ON CONFLICT (id) DO NOTHING;

-- Add some reviews for realism
INSERT INTO product_reviews (id, product_id, user_id, rating, title, comment, created_at, updated_at) VALUES
('rev-001', 'sunset-phalaenopsis-premium-bloom', '11111111-1111-1111-1111-111111111111', 5, 'Absolutely stunning!', 
 'This orchid exceeded my expectations. Arrived in perfect condition with all blooms intact. The colors are even more beautiful in person.', NOW(), NOW()),
('rev-002', 'white-cascade-phalaenopsis', '11111111-1111-1111-1111-111111111111', 5, 'Perfect gift',
 'Bought this as a gift for my mother and she loves it. The double spike is gorgeous and the blooms have lasted for weeks.', NOW(), NOW()),
('rev-003', 'fragrant-cattleya-orchid-hybrid', '11111111-1111-1111-1111-111111111111', 5, 'Amazing fragrance!',
 'The scent fills the entire room. Absolutely beautiful and well worth the price. This is a collector-quality plant.', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Summary
SELECT 'Sample data seed completed!' as message;
SELECT COUNT(*) as user_count FROM users WHERE id IN ('11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333');
SELECT COUNT(*) as category_count FROM categories;
SELECT COUNT(*) as store_count FROM stores;
SELECT COUNT(*) as product_count FROM products;
SELECT COUNT(*) as image_count FROM product_images;
SELECT COUNT(*) as review_count FROM product_reviews;
