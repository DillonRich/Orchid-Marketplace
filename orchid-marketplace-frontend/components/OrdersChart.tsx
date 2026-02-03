'use client'

import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts'

interface OrdersChartProps {
  data: Array<{
    date: string
    orders: number
    completed: number
    cancelled: number
  }>
}

export default function OrdersChart({ data }: OrdersChartProps) {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <BarChart data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
        <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
        <XAxis 
          dataKey="date" 
          stroke="#5C7A67"
          style={{ fontSize: '12px' }}
        />
        <YAxis 
          stroke="#5C7A67"
          style={{ fontSize: '12px' }}
        />
        <Tooltip 
          contentStyle={{
            backgroundColor: '#ffffff',
            border: '1px solid #e5e7eb',
            borderRadius: '8px',
            boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
          }}
          labelStyle={{ color: '#2F4538', fontWeight: 'bold' }}
        />
        <Legend />
        <Bar dataKey="completed" fill="#5C7A67" name="Completed" radius={[8, 8, 0, 0]} />
        <Bar dataKey="cancelled" fill="#F4A261" name="Cancelled" radius={[8, 8, 0, 0]} />
      </BarChart>
    </ResponsiveContainer>
  )
}
