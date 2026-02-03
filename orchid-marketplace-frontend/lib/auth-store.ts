import { create } from "zustand";
import { persist } from "zustand/middleware";
import { apiClient } from "./api-client";

export type UserRole = "CUSTOMER" | "SELLER" | "ADMIN" | "SUPPORT_AGENT";

export interface User {
  id: string;
  email: string;
  fullName: string;
  role: UserRole;
  storeId?: string;
  createdAt?: string;
}

interface AuthStore {
  user: User | null;
  accessToken: string | null;
  isLoading: boolean;
  error: string | null;
  isAuthenticated: boolean;

  // Actions
  setUser: (user: User | null) => void;
  setAccessToken: (token: string) => void;
  setLoading: (isLoading: boolean) => void;
  setError: (error: string | null) => void;

  // Auth methods
  register: (email: string, fullName: string, password: string, role?: UserRole) => Promise<void>;
  login: (email: string, password: string) => Promise<void>;
  loginWithAzure: (idToken: string) => Promise<void>;
  logout: () => void;
  checkAuth: () => Promise<void>;

  // Helper methods
  hasRole: (role: UserRole | UserRole[]) => boolean;
  isSeller: () => boolean;
  isAdmin: () => boolean;
  isCustomer: () => boolean;
}

export const useAuthStore = create<AuthStore>()(
  persist(
    (set, get) => ({
      user: null,
      accessToken: null,
      isLoading: false,
      error: null,
      isAuthenticated: false,

      setUser: (user) => set({ user }),
      setAccessToken: (token) => {
        set({ accessToken: token });
        apiClient.setTokens({ accessToken: token });
      },
      setLoading: (isLoading) => set({ isLoading }),
      setError: (error) => set({ error }),

      register: async (email, fullName, password, role) => {
        set({ isLoading: true, error: null });
        try {
          const response = await apiClient.registerUser(email, fullName, password, role);
          // Backend returns { token, username, email, role, message }
          // Transform to User object
          const user: User = {
            id: response.email, // Use email as ID temporarily until we get proper user ID
            email: response.email,
            fullName: response.username || fullName,
            role: response.role as UserRole,
          };
          set({ user, accessToken: response.token, isAuthenticated: true });
          apiClient.setTokens({ accessToken: response.token });
        } catch (error: any) {
          const message = error.response?.data?.message || "Registration failed";
          set({ error: message });
          throw error;
        } finally {
          set({ isLoading: false });
        }
      },

      login: async (email, password) => {
        set({ isLoading: true, error: null });
        try {
          const response = await apiClient.loginUser(email, password);
          // Backend returns { token, username, email, role, message }
          // Transform to User object
          const user: User = {
            id: response.email, // Use email as ID temporarily until we get proper user ID
            email: response.email,
            fullName: response.username || email,
            role: response.role as UserRole,
          };
          set({ user, accessToken: response.token, isAuthenticated: true });
          apiClient.setTokens({ accessToken: response.token });
        } catch (error: any) {
          const message = error.response?.data?.message || "Login failed";
          set({ error: message });
          throw error;
        } finally {
          set({ isLoading: false });
        }
      },

      loginWithAzure: async (idToken: string) => {
        set({ isLoading: true, error: null });
        try {
          const response = await apiClient.loginWithAzure(idToken);
          // apiClient already returns response.data, so response is { user, token }
          const { user, token } = response;
          set({ user, accessToken: token, isAuthenticated: true });
          apiClient.setTokens({ accessToken: token });
        } catch (error: any) {
          const message = error.response?.data?.message || "Azure login failed";
          set({ error: message });
          throw error;
        } finally {
          set({ isLoading: false });
        }
      },

      logout: () => {
        set({
          user: null,
          accessToken: null,
          isAuthenticated: false,
          error: null,
        });
        apiClient.clearTokens();
      },

      checkAuth: async () => {
        const { accessToken, user } = get();
        if (!accessToken || !user) {
          set({ isAuthenticated: false });
          return;
        }
        try {
          const userData = await apiClient.getUserProfile(user.id);
          set({ user: userData, isAuthenticated: true });
        } catch (error) {
          set({ isAuthenticated: false, user: null, accessToken: null });
          apiClient.clearTokens();
        }
      },

      hasRole: (roles) => {
        const { user } = get();
        if (!user) return false;
        const roleArray = Array.isArray(roles) ? roles : [roles];
        return roleArray.includes(user.role);
      },

      isSeller: () => get().user?.role === "SELLER",
      isAdmin: () => get().user?.role === "ADMIN",
      isCustomer: () => get().user?.role === "CUSTOMER",
    }),
    {
      name: "auth-storage",
      partialize: (state) => ({
        user: state.user,
        accessToken: state.accessToken,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
);
