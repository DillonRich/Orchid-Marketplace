'use client'

import { useToast, Toast as ToastType } from '@/lib/toast-context'

export default function ToastContainer() {
  const { toasts, removeToast } = useToast()

  return (
    <div className="fixed bottom-4 right-4 z-50 flex flex-col gap-2 max-w-md">
      {toasts.map((toast: ToastType) => (
        <Toast key={toast.id} toast={toast} onClose={() => removeToast(toast.id)} />
      ))}
    </div>
  )
}

function Toast({ toast, onClose }: { toast: ToastType; onClose: () => void }) {
  const bgColors: Record<string, string> = {
    success: 'bg-green-50 border-green-200',
    error: 'bg-red-50 border-red-200',
    warning: 'bg-yellow-50 border-yellow-200',
    info: 'bg-blue-50 border-blue-200',
  }

  const textColors: Record<string, string> = {
    success: 'text-green-800',
    error: 'text-red-800',
    warning: 'text-yellow-800',
    info: 'text-blue-800',
  }

  const iconColors: Record<string, string> = {
    success: 'text-green-500',
    error: 'text-red-500',
    warning: 'text-yellow-500',
    info: 'text-blue-500',
  }

  const icons: Record<string, string> = {
    success: 'check_circle',
    error: 'error',
    warning: 'warning',
    info: 'info',
  }

  return (
    <div
      className={`${bgColors[toast.type]} ${textColors[toast.type]} border rounded-xl px-4 py-3 shadow-lg animate-slide-in-right flex items-start gap-3`}
      role="alert"
    >
      <span className={`material-symbols-outlined ${iconColors[toast.type]} text-xl`}>
        {icons[toast.type]}
      </span>
      <div className="flex-1 text-sm font-medium">{toast.message}</div>
      <button
        onClick={onClose}
        className={`${textColors[toast.type]} hover:opacity-70 transition-opacity`}
        aria-label="Close notification"
      >
        <span className="material-symbols-outlined text-lg">close</span>
      </button>
    </div>
  )
}
