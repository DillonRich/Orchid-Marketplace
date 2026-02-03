export default function Footer() {
  return (
    <footer className="bg-warm-cream border-t border-primary-peach/20 pt-12 pb-8 mt-12">
      <div className="max-w-[1280px] mx-auto px-4 md:px-0 flex gap-32">
        <div className="flex-shrink-0">
          <div className="flex items-center gap-2 text-deep-sage mb-6">
            <h2 className="text-xl font-bold font-serif">Orchidillo</h2>
          </div>
          <p className="text-sage-green font-medium max-w-[280px] leading-relaxed mb-6">
            The premier curated marketplace for orchid enthusiasts. We bridge the gap between nurseries, trait breeders, and passionate collectors across the US.
          </p>
        </div>

        <div className="flex-1 flex gap-24">
          <div>
            <h3 className="font-bold text-deep-sage mb-6 uppercase text-xs tracking-widest">Shop All</h3>
            <ul className="flex flex-col gap-4 text-sm text-sage-green font-medium">
              <li><a className="hover:text-accent-peach transition-colors">Trending Now</a></li>
              <li><a className="hover:text-accent-peach transition-colors">New Arrivals</a></li>
              <li><a className="hover:text-accent-peach transition-colors">Rare & Exotic</a></li>
              <li><a className="hover:text-accent-peach transition-colors">Orchid Supplies</a></li>
            </ul>
          </div>

          <div>
            <h3 className="font-bold text-deep-sage mb-6 uppercase text-xs tracking-widest">Help & Info</h3>
            <ul className="flex flex-col gap-4 text-sm text-sage-green font-medium">
              <li><a className="hover:text-accent-peach transition-colors">Shipping Policy</a></li>
              <li><a className="hover:text-accent-peach transition-colors">Returns & Refunds</a></li>
              <li><a className="hover:text-accent-peach transition-colors">Nursery Partner FAQ</a></li>
            </ul>
          </div>

          <div>
            <h3 className="font-bold text-deep-sage mb-6 uppercase text-xs tracking-widest">The Company</h3>
            <ul className="flex flex-col gap-4 text-sm text-sage-green font-medium">
              <li><a className="hover:text-accent-peach transition-colors">Our Story</a></li>
              <li><a className="hover:text-accent-peach transition-colors">Terms</a></li>
              <li><a className="hover:text-accent-peach transition-colors">Privacy</a></li>
            </ul>
          </div>
        </div>
      </div>

      <div className="max-w-[1280px] mx-auto px-4 md:px-0 mt-16 pt-8 border-t border-primary-peach/10 flex flex-col md:flex-row justify-between items-center gap-6">
        <p className="text-xs text-deep-sage font-medium">© 2026 Orchidillo Marketplace. All rights reserved.</p>
        <div className="flex gap-6 text-xs text-sage-green font-medium">
          <a className="hover:text-accent-peach hover:underline transition-colors">Cookie Settings</a>
          <a className="hover:text-accent-peach hover:underline transition-colors">Privacy Policy</a>
          <a className="hover:text-accent-peach hover:underline transition-colors">Terms of Service</a>
        </div>
      </div>
    </footer>
  )
}
