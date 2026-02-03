'use client'

import Link from 'next/link'
import { useRouter } from 'next/navigation'

interface EmptyStateProps {
  icon?: string
  title: string
  description: string
  actionLabel?: string
  actionHref?: string
  onAction?: () => void
  secondaryActionLabel?: string
  secondaryActionHref?: string
}

export default function EmptyState({
  icon = 'inbox',
  title,
  description,
  actionLabel,
  actionHref,
  onAction,
  secondaryActionLabel,
  secondaryActionHref,
}: EmptyStateProps) {
  const router = useRouter()

  const handleAction = () => {
    if (onAction) {
      onAction()
    } else if (actionHref) {
      router.push(actionHref)
    }
  }

  return (
    <div className="bg-white rounded-xl p-12 text-center">
      <span className="material-symbols-outlined text-6xl text-sage-green mb-4 block">
        {icon}
      </span>
      <h3 className="font-playfair text-2xl font-bold text-deep-sage mb-2">
        {title}
      </h3>
      <p className="text-sage-green mb-6 max-w-md mx-auto">
        {description}
      </p>
      
      {(actionLabel || secondaryActionLabel) && (
        <div className="flex items-center justify-center gap-4">
          {actionLabel && (
            actionHref ? (
              <Link
                href={actionHref}
                className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all inline-block"
              >
                {actionLabel}
              </Link>
            ) : (
              <button
                onClick={handleAction}
                className="bg-deep-sage text-white py-3 px-8 rounded-full font-medium hover:bg-sage-green transition-all"
              >
                {actionLabel}
              </button>
            )
          )}
          
          {secondaryActionLabel && secondaryActionHref && (
            <Link
              href={secondaryActionHref}
              className="py-3 px-8 border-2 border-deep-sage text-deep-sage rounded-full font-medium hover:bg-soft-peach transition-all inline-block"
            >
              {secondaryActionLabel}
            </Link>
          )}
        </div>
      )}
    </div>
  )
}
