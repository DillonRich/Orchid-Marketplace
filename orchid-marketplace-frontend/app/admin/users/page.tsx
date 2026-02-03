'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { useSearchParams } from 'next/navigation'
import { apiClient } from '@/lib/api-client'

interface User {
  id: string
  name: string
  email: string
  role: string
  status: string
  createdAt: string
  storeId?: string
  storeName?: string
}

export default function AdminUsersPage() {
  const searchParams = useSearchParams()
  const roleFilter = searchParams.get('role')

  const [users, setUsers] = useState<User[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [filter, setFilter] = useState(roleFilter || 'all')
  const [searchQuery, setSearchQuery] = useState('')

  useEffect(() => {
    fetchUsers()
  }, [filter])

  const fetchUsers = async () => {
    try {
      setIsLoading(true)
      const roleParam = filter === 'all' ? undefined : filter.toUpperCase()
      const data = await apiClient.getAllUsers(roleParam)
      setUsers(data.users || data)
    } catch (err) {
      console.error('Failed to fetch users:', err)
    } finally {
      setIsLoading(false)
    }
  }

  const handleStatusUpdate = async (userId: string, newStatus: string) => {
    try {
      await apiClient.updateUserStatus(userId, newStatus)
      await fetchUsers()
      alert(`User ${newStatus} successfully`)
    } catch (err) {
      console.error('Failed to update user status:', err)
      alert('Failed to update user status')
    }
  }

  const handleRoleUpdate = async (userId: string, newRole: string) => {
    if (!confirm(`Change user role to ${newRole}?`)) return

    try {
      await apiClient.updateUserRole(userId, newRole)
      await fetchUsers()
      alert('User role updated successfully')
    } catch (err) {
      console.error('Failed to update user role:', err)
      alert('Failed to update user role')
    }
  }

  const filteredUsers = users.filter(user => {
    const matchesFilter = filter === 'all' || user.role.toLowerCase() === filter.toLowerCase()
    const matchesSearch =
      user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
      (user.storeName && user.storeName.toLowerCase().includes(searchQuery.toLowerCase()))

    return matchesFilter && matchesSearch
  })

  const filterOptions = [
    { value: 'all', label: 'All Users', count: users.length },
    { value: 'customer', label: 'Customers', count: users.filter(u => u.role.toLowerCase() === 'customer').length },
    { value: 'seller', label: 'Sellers', count: users.filter(u => u.role.toLowerCase() === 'seller').length },
    { value: 'admin', label: 'Admins', count: users.filter(u => u.role.toLowerCase() === 'admin').length },
  ]

  const getRoleColor = (role: string) => {
    switch (role.toLowerCase()) {
      case 'admin': return 'bg-purple-100 text-purple-700'
      case 'seller': return 'bg-green-100 text-green-700'
      case 'customer': return 'bg-blue-100 text-blue-700'
      default: return 'bg-gray-100 text-gray-700'
    }
  }

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'active': return 'bg-green-100 text-green-700'
      case 'suspended': return 'bg-red-100 text-red-700'
      case 'pending': return 'bg-yellow-100 text-yellow-700'
      default: return 'bg-gray-100 text-gray-700'
    }
  }

  if (isLoading) {
    return (
      <div className="space-y-8">
        <div className="animate-pulse">
          <div className="h-12 bg-gray-200 rounded w-1/4 mb-2"></div>
          <div className="h-6 bg-gray-200 rounded w-1/3"></div>
        </div>
        <div className="bg-white rounded-xl p-6 h-64 animate-pulse"></div>
      </div>
    )
  }

  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="font-playfair text-4xl font-bold text-deep-sage mb-2">
          User Management
        </h1>
        <p className="text-sage-green">
          Manage all users, roles, and permissions
        </p>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl p-6">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Search by name, email, or store..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full px-4 py-3 rounded-lg border border-gray-300 bg-warm-cream focus:outline-none focus:ring-2 focus:ring-accent-peach"
            />
          </div>

          <div className="flex gap-2 overflow-x-auto">
            {filterOptions.map(option => (
              <button
                key={option.value}
                onClick={() => setFilter(option.value)}
                className={`px-4 py-3 rounded-lg font-medium whitespace-nowrap transition-all ${
                  filter === option.value
                    ? 'bg-deep-sage text-white'
                    : 'bg-gray-100 text-sage-green hover:bg-soft-peach'
                }`}
              >
                {option.label}
                <span className="ml-2 text-sm opacity-75">
                  {option.count}
                </span>
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Users Table */}
      {filteredUsers.length > 0 ? (
        <div className="bg-white rounded-xl overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-soft-peach">
                <tr className="text-left text-sm text-deep-sage">
                  <th className="px-6 py-4 font-medium">User</th>
                  <th className="px-6 py-4 font-medium">Role</th>
                  <th className="px-6 py-4 font-medium">Store</th>
                  <th className="px-6 py-4 font-medium">Status</th>
                  <th className="px-6 py-4 font-medium">Joined</th>
                  <th className="px-6 py-4 font-medium">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredUsers.map(user => (
                  <tr key={user.id} className="border-b border-gray-200 hover:bg-soft-peach transition-all">
                    <td className="px-6 py-4">
                      <div>
                        <p className="font-medium text-deep-sage">{user.name}</p>
                        <p className="text-xs text-sage-green">{user.email}</p>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className={`text-xs px-3 py-1 rounded-full ${getRoleColor(user.role)}`}>
                        {user.role}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sage-green">
                      {user.storeName ? (
                        <Link href={`/store/${user.storeId}`} className="hover:text-accent-peach">
                          {user.storeName}
                        </Link>
                      ) : (
                        '-'
                      )}
                    </td>
                    <td className="px-6 py-4">
                      <span className={`text-xs px-3 py-1 rounded-full ${getStatusColor(user.status)}`}>
                        {user.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sage-green text-sm">
                      {new Date(user.createdAt).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <select
                          value={user.status}
                          onChange={(e) => handleStatusUpdate(user.id, e.target.value)}
                          className="text-xs px-2 py-1 rounded border border-gray-300 text-sage-green"
                        >
                          <option value="active">Active</option>
                          <option value="suspended">Suspended</option>
                        </select>
                        
                        <button
                          onClick={() => {
                            const newRole = prompt('Enter new role (CUSTOMER/SELLER/ADMIN):', user.role)
                            if (newRole) handleRoleUpdate(user.id, newRole)
                          }}
                          className="text-accent-peach hover:text-deep-sage"
                          title="Change Role"
                        >
                          <span className="material-symbols-outlined text-sm">edit</span>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="bg-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl text-gray-300 mb-4">people</span>
          <p className="text-sage-green">No users found</p>
        </div>
      )}
    </div>
  )
}
