"use client"

import { useState } from 'react'

export default function SellerFinancesPage() {
  const [transactions, setTransactions] = useState([
    { id: '1', date: '2026-01-30', type: 'sale', description: 'Purple Phalaenopsis', amount: 45.00, status: 'completed' },
    { id: '2', date: '2026-01-29', type: 'sale', description: 'White Orchid Set', amount: 89.99, status: 'completed' },
    { id: '3', date: '2026-01-28', type: 'payout', description: 'Weekly Payout', amount: -523.50, status: 'completed' },
    { id: '4', date: '2026-01-27', type: 'sale', description: 'Pink Cattleya', amount: 65.00, status: 'pending' },
  ])
  
  const totalBalance = 676.49
  const pendingBalance = 65.00
  const lifetimeEarnings = 2156.50
  
  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-serif font-bold text-deep-sage mb-2">Finances</h1>
        <p className="text-sage-green">Track your earnings and payouts</p>
      </div>
      
      {/* Balance Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white rounded-xl p-6 shadow-sm">
          <div className="flex items-center gap-3 mb-3">
            <span className="material-symbols-outlined text-3xl text-green-500">account_balance_wallet</span>
            <h3 className="text-sm font-medium text-sage-green">Available Balance</h3>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-2">
            ${totalBalance.toFixed(2)}
          </div>
          <button className="text-sm text-accent-peach hover:text-deep-sage font-medium">
            Request Payout →
          </button>
        </div>
        
        <div className="bg-white rounded-xl p-6 shadow-sm">
          <div className="flex items-center gap-3 mb-3">
            <span className="material-symbols-outlined text-3xl text-yellow-500">pending</span>
            <h3 className="text-sm font-medium text-sage-green">Pending Balance</h3>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-2">
            ${pendingBalance.toFixed(2)}
          </div>
          <p className="text-xs text-sage-green">Processing orders</p>
        </div>
        
        <div className="bg-white rounded-xl p-6 shadow-sm">
          <div className="flex items-center gap-3 mb-3">
            <span className="material-symbols-outlined text-3xl text-purple-500">trending_up</span>
            <h3 className="text-sm font-medium text-sage-green">Lifetime Earnings</h3>
          </div>
          <div className="text-3xl font-bold text-deep-sage mb-2">
            ${lifetimeEarnings.toFixed(2)}
          </div>
          <p className="text-xs text-sage-green">All time</p>
        </div>
      </div>
      
      {/* Payout Settings */}
      <div className="bg-white rounded-xl p-6">
        <h3 className="text-xl font-serif font-bold text-deep-sage mb-4">Payout Settings</h3>
        <div className="space-y-4">
          <div className="flex items-center justify-between p-4 bg-warm-cream rounded-lg">
            <div>
              <div className="font-medium text-deep-sage">Bank Account</div>
              <div className="text-sm text-sage-green">••••••1234</div>
            </div>
            <button className="text-accent-peach hover:text-deep-sage font-medium text-sm">
              Update
            </button>
          </div>
          
          <div className="flex items-center justify-between p-4 bg-warm-cream rounded-lg">
            <div>
              <div className="font-medium text-deep-sage">Payout Schedule</div>
              <div className="text-sm text-sage-green">Weekly on Fridays</div>
            </div>
            <button className="text-accent-peach hover:text-deep-sage font-medium text-sm">
              Change
            </button>
          </div>
        </div>
      </div>
      
      {/* Transaction History */}
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-xl font-serif font-bold text-deep-sage">Transaction History</h3>
        </div>
        
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-soft-peach">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase">Date</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase">Type</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase">Description</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase">Amount</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-deep-sage uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {transactions.map((txn) => (
                <tr key={txn.id} className="hover:bg-warm-cream transition-colors">
                  <td className="px-6 py-4 text-sm text-sage-green">{txn.date}</td>
                  <td className="px-6 py-4 text-sm">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      txn.type === 'sale' ? 'bg-green-100 text-green-800' : 'bg-blue-100 text-blue-800'
                    }`}>
                      {txn.type.charAt(0).toUpperCase() + txn.type.slice(1)}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-sm text-sage-green">{txn.description}</td>
                  <td className="px-6 py-4 text-sm font-medium">
                    <span className={txn.amount > 0 ? 'text-green-600' : 'text-deep-sage'}>
                      {txn.amount > 0 ? '+' : ''}${Math.abs(txn.amount).toFixed(2)}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-sm">
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      txn.status === 'completed' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {txn.status.charAt(0).toUpperCase() + txn.status.slice(1)}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
