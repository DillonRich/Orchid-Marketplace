import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function ShippingPage() {
  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1000px] mx-auto px-4 py-16">
          <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
            Shipping Policy
          </h1>
          
          <p className="text-center text-sage-green mb-12">
            Last Updated: January 22, 2026
          </p>
          
          <div className="bg-white rounded-xl p-12 space-y-8">
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                1. Shipping Overview
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                Orchidillo is a marketplace platform connecting buyers with independent sellers. Each seller manages their own inventory and shipping. Shipping policies, rates, and delivery times vary by seller and are displayed on each product listing.
              </p>
              <p className="text-sage-green leading-relaxed">
                Most orders ship within 1-3 business days of purchase, but processing times may vary. You will receive a shipping confirmation email with tracking information once your order ships.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                2. Shipping Rates
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                Shipping costs are calculated based on:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Item weight and dimensions</li>
                <li>Shipping destination</li>
                <li>Selected shipping method</li>
                <li>Seller's shipping policies</li>
              </ul>
              <p className="text-sage-green leading-relaxed mt-3">
                Each seller sets their own shipping rates and policies. Combined shipping discounts may be available when purchasing multiple items from the same seller. Contact sellers directly for specific shipping information.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                3. Shipping Methods
              </h2>
              <div className="space-y-4">
                <div>
                  <h3 className="font-medium text-deep-sage mb-2">Standard Shipping</h3>
                  <p className="text-sage-green leading-relaxed">
                    Typical delivery in 5-7 business days. Most economical option for non-urgent orders. Tracking included with all standard shipments.
                  </p>
                </div>
                <div>
                  <h3 className="font-medium text-deep-sage mb-2">Expedited Shipping</h3>
                  <p className="text-sage-green leading-relaxed">
                    Delivery in 2-3 business days. Recommended for temperature-sensitive orchids or time-sensitive gifts. Additional charges apply.
                  </p>
                </div>
                <div>
                  <h3 className="font-medium text-deep-sage mb-2">Express Shipping</h3>
                  <p className="text-sage-green leading-relaxed">
                    Next-day or 2-day delivery available from select sellers. Best for heat-sensitive shipments or last-minute gifts. Premium rates apply.
                  </p>
                </div>
              </div>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                4. International Shipping
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                International shipping availability varies by seller. Not all sellers can ship plants internationally due to phytosanitary regulations and import restrictions.
              </p>
              <p className="text-sage-green leading-relaxed mb-3">
                International orders may be subject to:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Customs duties and import taxes (buyer's responsibility)</li>
                <li>Extended delivery times (10-21 business days typical)</li>
                <li>Additional documentation and phytosanitary certificates</li>
                <li>Inspection and quarantine requirements</li>
              </ul>
              <p className="text-sage-green leading-relaxed mt-3">
                Please check your country's import regulations for live plants before ordering. Orchidillo and our sellers are not responsible for plants seized by customs.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                5. Plant Packaging & Care
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                Our sellers are experienced in shipping live orchids safely. All plants are carefully packaged to survive transit:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Plants are secured to prevent movement and damage</li>
                <li>Protective wrapping shields leaves and flowers</li>
                <li>Heat packs (winter) or cool packs (summer) included when necessary</li>
                <li>Boxes are marked "Live Plants" and "Handle With Care"</li>
                <li>Shipped early in the week to avoid weekend delays</li>
              </ul>
              <p className="text-sage-green leading-relaxed mt-3">
                Upon arrival, unbox immediately, water if needed, and allow plants to acclimate to their new environment for a few days before repotting.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                6. Tracking Your Order
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                Once your order ships, you'll receive:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Shipping confirmation email with tracking number</li>
                <li>Link to carrier's tracking page</li>
                <li>Estimated delivery date</li>
              </ul>
              <p className="text-sage-green leading-relaxed mt-3">
                You can also track orders from your account dashboard. Tracking typically updates within 24 hours of shipment. Contact the seller directly if tracking hasn't updated after 48 hours.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                7. Delivery Issues
              </h2>
              <div className="space-y-4">
                <div>
                  <h3 className="font-medium text-deep-sage mb-2">Damaged in Transit</h3>
                  <p className="text-sage-green leading-relaxed">
                    If your orchid arrives damaged, photograph the damage immediately and contact the seller within 24 hours. Most sellers offer replacements or refunds for transit damage. Do not discard packaging until the issue is resolved.
                  </p>
                </div>
                <div>
                  <h3 className="font-medium text-deep-sage mb-2">Lost or Stolen Packages</h3>
                  <p className="text-sage-green leading-relaxed">
                    If tracking shows delivered but you didn't receive your package, check with neighbors and building management. Contact the seller immediately. Sellers may file claims with carriers for lost packages.
                  </p>
                </div>
                <div>
                  <h3 className="font-medium text-deep-sage mb-2">Delivery Delays</h3>
                  <p className="text-sage-green leading-relaxed">
                    Weather, carrier delays, or other circumstances may affect delivery times. If your order is significantly delayed, contact the seller. Heat or cold stress during prolonged transit may affect plant health—sellers may offer remedies.
                  </p>
                </div>
              </div>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                8. Refused or Undeliverable Packages
              </h2>
              <p className="text-sage-green leading-relaxed">
                If a package is returned as undeliverable due to incorrect address, refused delivery, or failed delivery attempts, the buyer is responsible for return shipping costs. Sellers are not obligated to reship at their expense. Ensure your shipping address is correct before completing your order.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                9. Address Changes
              </h2>
              <p className="text-sage-green leading-relaxed">
                Contact the seller immediately if you need to change your shipping address. Address changes are only possible before the order ships. Once tracking shows the package in transit, address changes must be made through the shipping carrier's website or customer service.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                10. Contact for Shipping Questions
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                For questions about:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li><strong>Specific orders:</strong> Contact the seller directly through your order page</li>
                <li><strong>General shipping:</strong> Email support@orchidillo.com</li>
                <li><strong>Carrier issues:</strong> Contact the shipping carrier using your tracking number</li>
              </ul>
            </section>
          </div>
          
          {/* Shipping Tips Box */}
          <div className="mt-8 bg-soft-peach rounded-xl p-8">
            <div className="flex items-start gap-4">
              <span className="material-symbols-outlined text-deep-sage text-4xl">
                local_shipping
              </span>
              <div>
                <h3 className="font-playfair text-xl font-bold text-deep-sage mb-2">
                  Pro Shipping Tips
                </h3>
                <ul className="text-sage-green space-y-2">
                  <li>• Order early in the week for safest arrival</li>
                  <li>• Avoid shipping to extreme climates in summer/winter without expedited shipping</li>
                  <li>• Be available to receive packages—don't let orchids sit in hot/cold conditions</li>
                  <li>• Consider a business address for more reliable delivery</li>
                  <li>• Unbox immediately upon arrival and inspect plants</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
