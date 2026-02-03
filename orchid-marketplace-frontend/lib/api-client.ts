import axios, { AxiosInstance } from "axios";

interface AuthTokens {
  accessToken: string;
  refreshToken?: string;
}

export class ApiClient {
  private client: AxiosInstance;
  private tokens: AuthTokens | null = null;

  constructor(baseURL: string = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api") {
    this.client = axios.create({
      baseURL,
      headers: {
        "Content-Type": "application/json",
      },
    });

    // Add auth token to requests
    this.client.interceptors.request.use((config) => {
      if (this.tokens?.accessToken) {
        config.headers.Authorization = `Bearer ${this.tokens.accessToken}`;
      }
      return config;
    });
  }

  setTokens(tokens: AuthTokens) {
    this.tokens = tokens;
  }

  clearTokens() {
    this.tokens = null;
  }

  // User endpoints
  async registerUser(email: string, fullName: string, password: string, role?: string) {
    const payload = {
      email,
      fullName,
      password,
      role: role || "CUSTOMER",
    };
    console.log('API Client: Sending registration request to /api/users/register with payload:', payload);
    try {
      const response = await this.client.post("/users/register", payload);
      console.log('API Client: Registration response:', response.data);
      return response.data;
    } catch (error: any) {
      console.error('API Client: Registration error:', error);
      console.error('API Client: Error response:', error.response);
      throw error;
    }
  }

  async loginUser(email: string, password: string) {
    const response = await this.client.post("/users/login", { email, password });
    return response.data;
  }

  async loginWithAzure(idToken: string) {
    const response = await this.client.post("/auth/azure-login", { idToken });
    return response.data;
  }

  // Aliases for backwards compatibility
  async register(data: { email: string; fullName: string; password: string }) {
    return this.registerUser(data.email, data.fullName, data.password);
  }

  async login(data: { email: string; password: string }) {
    return this.loginUser(data.email, data.password);
  }

  async getUserProfile(userId: string) {
    const response = await this.client.get(`/users/${userId}`);
    return response.data;
  }

  async updateUserProfile(userId: string, data: any) {
    const response = await this.client.put(`/users/${userId}`, data);
    return response.data;
  }

  // Password reset endpoints
  async forgotPassword(email: string) {
    const response = await this.client.post("/users/forgot-password", { email });
    return response.data;
  }

  async resetPassword(token: string, newPassword: string) {
    const response = await this.client.post("/users/reset-password", { 
      token, 
      newPassword 
    });
    return response.data;
  }

  // Promo code validation
  async validatePromoCode(code: string, subtotal: number) {
    const response = await this.client.post("/promo-codes/validate", { 
      code, 
      subtotal 
    });
    return response.data;
  }

  // Product endpoints
  async getProducts(params?: { limit?: number; offset?: number }) {
    const response = await this.client.get("/products", { params });
    return response.data;
  }

  async getProductById(productId: string) {
    const response = await this.client.get(`/products/${productId}`);
    return response.data;
  }

  async searchProducts(query: string) {
    const response = await this.client.get("/products/search-basic", { params: { q: query } });
    return response.data;
  }

  async getProductsByStore(storeId: string) {
    const response = await this.client.get(`/products/by-store/${storeId}`);
    return response.data;
  }

  async getProductsByCategory(categoryId: string, params?: { limit?: number }) {
    const response = await this.client.get(`/products/by-category/${categoryId}`, { params });
    return response.data;
  }

  async createProduct(data: any) {
    const response = await this.client.post("/products", data);
    return response.data;
  }

  async updateProduct(productId: string, data: any) {
    const response = await this.client.put(`/products/${productId}`, data);
    return response.data;
  }

  async deleteProduct(productId: string) {
    const response = await this.client.delete(`/products/${productId}`);
    return response.data;
  }

  // Category endpoints
  async getCategories() {
    const response = await this.client.get("/categories");
    return response.data;
  }

  async getCategoryById(categoryId: string) {
    const response = await this.client.get(`/categories/${categoryId}`);
    return response.data;
  }

  async createCategory(data: any) {
    const response = await this.client.post("/categories", data);
    return response.data;
  }

  // Cart endpoints
  async getCart() {
    const response = await this.client.get("/cart");
    return response.data;
  }

  async addToCart(productId: string, quantity: number, shippingOptionId?: string) {
    const response = await this.client.post("/cart/add", {
      productId,
      quantity,
      shippingOptionId,
    });
    return response.data;
  }

  async updateCartItemQuantity(cartItemId: string, quantity: number) {
    const response = await this.client.put(`/cart/items/${cartItemId}`, null, {
      params: { quantity },
    });
    return response.data;
  }

  async updateCartItemShipping(cartItemId: string, shippingOptionId?: string) {
    const response = await this.client.put(`/cart/items/${cartItemId}/shipping`, null, {
      params: { shippingOptionId },
    });
    return response.data;
  }

  async removeFromCart(cartItemId: string) {
    const response = await this.client.delete(`/cart/items/${cartItemId}`);
    return response.data;
  }

  async clearCart() {
    const response = await this.client.delete("/cart");
    return response.data;
  }

  async validateCart() {
    const response = await this.client.post("/cart/validate", {});
    return response.data;
  }

  // Address endpoints
  async getAddresses(userId: string = 'me') {
    const response = await this.client.get(`/addresses/user/${userId}`);
    return response.data;
  }

  async createAddress(data: any) {
    const response = await this.client.post("/addresses", data);
    return response.data;
  }

  async updateAddress(addressId: string, data: any) {
    const response = await this.client.put(`/addresses/${addressId}`, data);
    return response.data;
  }

  async deleteAddress(addressId: string) {
    const response = await this.client.delete(`/addresses/${addressId}`);
    return response.data;
  }

  // Payment Method endpoints
  async getPaymentMethods(userId: string) {
    const response = await this.client.get(`/payment-methods/user/${userId}`);
    return response.data;
  }

  async createPaymentMethod(data: any) {
    const response = await this.client.post("/payment-methods", data);
    return response.data;
  }

  async updatePaymentMethod(paymentMethodId: string, data: any) {
    const response = await this.client.put(`/payment-methods/${paymentMethodId}`, data);
    return response.data;
  }

  async deletePaymentMethod(paymentMethodId: string) {
    const response = await this.client.delete(`/payment-methods/${paymentMethodId}`);
    return response.data;
  }

  // Checkout endpoints
  async checkout(shippingAddressId: string, billingAddressId: string) {
    const response = await this.client.post("/checkout", {
      shippingAddressId,
      billingAddressId,
    });
    return response.data;
  }

  async initiatePayment(
    orderId: string,
    successUrl: string,
    cancelUrl: string
  ) {
    const response = await this.client.post(
      `/checkout/${orderId}/initiate-payment`,
      {},
      {
        params: { successUrl, cancelUrl },
      }
    );
    return response.data;
  }

  async createPaymentIntent(data: { amount: number; currency: string; metadata?: any }) {
    const response = await this.client.post('/payment/create-intent', data);
    return response.data;
  }
  async getOrder(orderId: string) {
    const response = await this.client.get(`/checkout/${orderId}`);
    return response.data;
  }

  async confirmPayment(orderId: string) {
    const response = await this.client.post(`/checkout/${orderId}/confirm-payment`, {});
    return response.data;
  }

  async updateCartItem(itemId: string, data: { quantity: number }) {
    const response = await this.client.put(`/cart/items/${itemId}`, data);
    return response.data;
  }

  async removeCartItem(itemId: string) {
    const response = await this.client.delete(`/cart/items/${itemId}`);
    return response.data;
  }

  async checkoutCreateOrder(data: any) {
    const response = await this.client.post("/checkout/create-order", data);
    return response.data;
  }

  async checkoutCreateOrderGuest(data: any) {
    const response = await this.client.post("/checkout/create-order-guest", data);
    return response.data;
  }

  // Order endpoints
  async getOrders(limit?: number, offset?: number) {
    const response = await this.client.get("/orders", { params: { limit, offset } });
    return response.data;
  }

  async getOrderById(orderId: string) {
    const response = await this.client.get(`/orders/${orderId}`);
    return response.data;
  }

  // Seller Order endpoints
  async getSellerOrders(storeId: string, status?: string) {
    const response = await this.client.get(`/stores/${storeId}/orders`, { 
      params: { status } 
    });
    return response.data;
  }

  async updateOrderStatus(orderId: string, status: string) {
    const response = await this.client.patch(`/orders/${orderId}/status`, { status });
    return response.data;
  }

  async addOrderTracking(orderId: string, data: { trackingNumber: string; carrier: string; estimatedDelivery?: string }) {
    const response = await this.client.post(`/orders/${orderId}/tracking`, data);
    return response.data;
  }

  async sendOrderMessage(orderId: string, message: string) {
    const response = await this.client.post(`/orders/${orderId}/messages`, { message });
    return response.data;
  }

  // Shipping Option endpoints
  async getShippingOptions(productId: string) {
    const response = await this.client.get("/shipping-options", { params: { productId } });
    return response.data;
  }

  async getShippingOption(shippingOptionId: string) {
    const response = await this.client.get(`/shipping-options/${shippingOptionId}`);
    return response.data;
  }

  async createShippingOption(data: any) {
    const response = await this.client.post("/shipping-options", data);
    return response.data;
  }

  async updateShippingOption(shippingOptionId: string, data: any) {
    const response = await this.client.put(`/shipping-options/${shippingOptionId}`, data);
    return response.data;
  }

  async deleteShippingOption(shippingOptionId: string) {
    const response = await this.client.delete(`/shipping-options/${shippingOptionId}`);
    return response.data;
  }

  // Store endpoints
  async getStores(limit?: number, offset?: number) {
    const response = await this.client.get("/stores", { params: { limit, offset } });
    return response.data;
  }

  async getStoreById(storeId: string) {
    const response = await this.client.get(`/stores/${storeId}`);
    return response.data;
  }

  async getStoreBySlug(slug: string) {
    const response = await this.client.get(`/stores/slug/${slug}`);
    return response.data;
  }

  async createStore(data: any) {
    const response = await this.client.post("/stores", data);
    return response.data;
  }

  async updateStore(storeId: string, data: any) {
    const response = await this.client.put(`/stores/${storeId}`, data);
    return response.data;
  }

  // Product Review endpoints
  async getProductReviews(productId: string) {
    const response = await this.client.get(`/product-reviews/product/${productId}`);
    return response.data;
  }

  async createReview(productId: string, data: any) {
    const response = await this.client.post("/product-reviews", {
      ...data,
      productId,
    });
    return response.data;
  }

  // Product Image endpoints
  async getProductImages(productId: string) {
    const response = await this.client.get(`/product-images/product/${productId}`);
    return response.data;
  }

  async uploadProductImage(productId: string, file: File) {
    const formData = new FormData();
    formData.append("file", file);
    const response = await this.client.post(`/product-images/product/${productId}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  }

  // Seller Dashboard endpoints
  async getSellerDashboard(storeId: string) {
    const response = await this.client.get(`/seller-dashboard/${storeId}`);
    return response.data;
  }

  // Seller Finance endpoints
  async getSellerLedgerEntries(
    storeId: string,
    from: string,
    to: string
  ) {
    const response = await this.client.get(`/seller-finance/${storeId}/entries`, {
      params: { from, to },
    });
    return response.data;
  }

  async getSellerFinanceSummary(storeId: string, from: string, to: string) {
    const response = await this.client.get(`/seller-finance/${storeId}/summary`, {
      params: { from, to },
    });
    return response.data;
  }

  // Stripe Connect endpoints
  async initiateStripeConnect() {
    const response = await this.client.post("/stripe/connect/authorize", {});
    return response.data;
  }

  async getStripeConnectStatus() {
    const response = await this.client.get("/stripe/connect/connected");
    return response.data;
  }

  // Admin endpoints
  async getAdminStats() {
    const response = await this.client.get("/admin/stats");
    return response.data;
  }

  async getAllUsers(role?: string, page = 1, limit = 50) {
    const response = await this.client.get("/admin/users", {
      params: { role, page, limit }
    });
    return response.data;
  }

  async updateUserStatus(userId: string, status: string) {
    const response = await this.client.patch(`/admin/users/${userId}/status`, { status });
    return response.data;
  }

  async updateUserRole(userId: string, role: string) {
    const response = await this.client.patch(`/admin/users/${userId}/role`, { role });
    return response.data;
  }

  async getAllProductsAdmin(status?: string, page = 1, limit = 50) {
    const response = await this.client.get("/admin/products", {
      params: { status, page, limit }
    });
    return response.data;
  }

  async approveProduct(productId: string) {
    const response = await this.client.post(`/admin/products/${productId}/approve`);
    return response.data;
  }

  async rejectProduct(productId: string, reason: string) {
    const response = await this.client.post(`/admin/products/${productId}/reject`, { reason });
    return response.data;
  }

  async deleteProductAdmin(productId: string) {
    const response = await this.client.delete(`/admin/products/${productId}`);
    return response.data;
  }

  async getAllOrdersAdmin(status?: string, page = 1, limit = 50) {
    const response = await this.client.get("/admin/orders", {
      params: { status, page, limit }
    });
    return response.data;
  }

  // Message endpoints
  async getMessages(conversationId?: string) {
    const response = await this.client.get("/messages", { params: { conversationId } });
    return response.data;
  }

  async sendMessage(data: any) {
    const response = await this.client.post("/messages", data);
    return response.data;
  }

  // Contact form
  async sendContactMessage(data: { name: string; email: string; subject: string; message: string }) {
    const response = await this.client.post("/contact", data);
    return response.data;
  }

  // Notification endpoints
  async getNotifications(params?: { unreadOnly?: boolean; page?: number; size?: number }) {
    const response = await this.client.get("/notifications", { params });
    return response.data;
  }

  async markNotificationAsRead(notificationId: number) {
    const response = await this.client.put(`/notifications/${notificationId}/read`);
    return response.data;
  }

  async markAllNotificationsAsRead() {
    const response = await this.client.put("/notifications/read-all");
    return response.data;
  }

  async deleteNotification(notificationId: number) {
    const response = await this.client.delete(`/notifications/${notificationId}`);
    return response.data;
  }

  async getUnreadNotificationCount() {
    const response = await this.client.get("/notifications/unread-count");
    return response.data;
  }
}

export const apiClient = new ApiClient();
