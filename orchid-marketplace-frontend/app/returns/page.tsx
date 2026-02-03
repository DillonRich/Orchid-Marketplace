export default function ReturnsPage() {
  return (
    <div className="bg-warm-cream min-h-screen">
      <div className="max-w-[1000px] mx-auto px-4 py-16">
        <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
          Returns & Refunds Policy
        </h1>
        <p className="text-lg text-sage-green text-center mb-12">
          Last updated: January 27, 2026
        </p>

        {/* Overview */}
        <div className="bg-white rounded-xl p-12 mb-8">
          <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-6">
            Our Commitment to Quality
          </h2>
          <p className="text-sage-green leading-relaxed mb-4">
            At Orchidillo, we understand that purchasing live plants online requires trust. We are committed to ensuring that every orchid arrives in perfect condition and meets your expectations. Due to the delicate nature of live plants, we have established clear guidelines for returns and refunds.
          </p>
        </div>

        {/* Quality Guarantee */}
        <div className="bg-white rounded-xl p-12 mb-8">
          <div className="flex items-start gap-4 mb-6">
            <span className="material-symbols-outlined text-4xl text-deep-sage">
              workspace_premium
            </span>
            <div>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                7-Day Quality Guarantee
              </h2>
              <p className="text-sage-green leading-relaxed mb-4">
                All orchids purchased through Orchidillo are covered by our 7-Day Quality Guarantee from the date of delivery. If your plant arrives damaged, diseased, or not as described, we will provide a full refund or replacement.
              </p>
              <div className="bg-soft-peach p-4 rounded-lg">
                <p className="text-deep-sage font-medium mb-2">Covered Issues:</p>
                <ul className="list-disc list-inside text-sage-green space-y-1">
                  <li>Physical damage during shipping</li>
                  <li>Plant arrives diseased or pest-infested</li>
                  <li>Significantly different from description or photos</li>
                  <li>Wrong plant or variety shipped</li>
                  <li>Missing plant or accessories</li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        {/* Return Process */}
        <div className="bg-white rounded-xl p-12 mb-8">
          <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
            How to Request a Return or Refund
          </h2>
          
          <div className="space-y-6">
            <div className="flex items-start gap-4">
              <div className="bg-accent-peach text-white rounded-full w-8 h-8 flex items-center justify-center font-bold flex-shrink-0">
                1
              </div>
              <div>
                <h3 className="font-medium text-deep-sage text-lg mb-2">
                  Document the Issue
                </h3>
                <p className="text-sage-green">
                  Take clear photos of the plant showing the issue (damage, disease, wrong variety, etc.). Include photos of the packaging if damage occurred during shipping.
                </p>
              </div>
            </div>

            <div className="flex items-start gap-4">
              <div className="bg-accent-peach text-white rounded-full w-8 h-8 flex items-center justify-center font-bold flex-shrink-0">
                2
              </div>
              <div>
                <h3 className="font-medium text-deep-sage text-lg mb-2">
                  Contact Within 7 Days
                </h3>
                <p className="text-sage-green">
                  Submit a return request through your order page within 7 days of delivery. Provide your order number, description of the issue, and upload your photos.
                </p>
              </div>
            </div>

            <div className="flex items-start gap-4">
              <div className="bg-accent-peach text-white rounded-full w-8 h-8 flex items-center justify-center font-bold flex-shrink-0">
                3
              </div>
              <div>
                <h3 className="font-medium text-deep-sage text-lg mb-2">
                  Review & Resolution
                </h3>
                <p className="text-sage-green">
                  Our team will review your request within 24 hours. If approved, you'll choose between a full refund or a replacement plant (subject to availability).
                </p>
              </div>
            </div>

            <div className="flex items-start gap-4">
              <div className="bg-accent-peach text-white rounded-full w-8 h-8 flex items-center justify-center font-bold flex-shrink-0">
                4
              </div>
              <div>
                <h3 className="font-medium text-deep-sage text-lg mb-2">
                  Receive Refund or Replacement
                </h3>
                <p className="text-sage-green">
                  Refunds are processed to your original payment method within 5-7 business days. Replacement plants are shipped as soon as available.
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Not Covered */}
        <div className="bg-white rounded-xl p-12 mb-8">
          <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
            What's Not Covered
          </h2>
          <p className="text-sage-green mb-4">
            Our guarantee does not cover issues resulting from improper care after delivery:
          </p>
          <ul className="list-disc list-inside text-sage-green space-y-2 mb-4">
            <li>Damage caused by incorrect watering, lighting, or temperature</li>
            <li>Natural leaf yellowing or flower wilting after blooming period</li>
            <li>Changes of mind or personal preference</li>
            <li>Issues reported more than 7 days after delivery</li>
            <li>Plants purchased from other platforms or sellers</li>
          </ul>
          <div className="bg-blue-50 border border-blue-200 p-4 rounded-lg">
            <p className="text-blue-900">
              <span className="font-medium">💡 Care Tip:</span> Every orchid comes with detailed care instructions. Following these guidelines will help ensure your plant thrives. For additional help, visit our Care Guides section or contact the seller directly.
            </p>
          </div>
        </div>

        {/* Seller Policies */}
        <div className="bg-white rounded-xl p-12 mb-8">
          <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
            Individual Seller Policies
          </h2>
          <p className="text-sage-green leading-relaxed mb-4">
            While Orchidillo provides the above guarantee for all purchases, individual sellers may offer additional return or exchange options beyond our standard policy. Check the seller's profile or product listing for their specific policies.
          </p>
          <p className="text-sage-green leading-relaxed">
            If you have questions about a seller's policy before purchasing, we recommend contacting them directly through their store page.
          </p>
        </div>

        {/* Refund Processing */}
        <div className="bg-white rounded-xl p-12 mb-8">
          <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
            Refund Processing Times
          </h2>
          <div className="space-y-4">
            <div>
              <h3 className="font-medium text-deep-sage mb-2">
                Orchidillo Processing: 1-2 Business Days
              </h3>
              <p className="text-sage-green">
                Once your return is approved, we initiate the refund immediately.
              </p>
            </div>
            <div>
              <h3 className="font-medium text-deep-sage mb-2">
                Payment Provider Processing: 3-5 Business Days
              </h3>
              <p className="text-sage-green">
                Your bank or credit card company may take additional time to post the refund to your account.
              </p>
            </div>
            <div className="bg-yellow-50 border border-yellow-200 p-4 rounded-lg">
              <p className="text-yellow-900">
                <span className="font-medium">⏱️ Note:</span> Total refund time is typically 5-7 business days from approval, but may vary by payment method.
              </p>
            </div>
          </div>
        </div>

        {/* Damaged Shipments */}
        <div className="bg-white rounded-xl p-12 mb-8">
          <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
            Damaged or Lost Shipments
          </h2>
          <p className="text-sage-green leading-relaxed mb-4">
            If your package appears damaged upon delivery or doesn't arrive at all:
          </p>
          <ul className="list-disc list-inside text-sage-green space-y-2 mb-4">
            <li>Take photos of the damaged box before opening (if applicable)</li>
            <li>Contact us immediately with your order number and photos</li>
            <li>We will file a claim with the shipping carrier</li>
            <li>You will receive a replacement or full refund</li>
          </ul>
          <p className="text-sage-green">
            We work with trusted shipping partners and all orders are insured for their full value.
          </p>
        </div>

        {/* Contact Support */}
        <div className="bg-gradient-to-r from-deep-sage to-sage-green text-white rounded-xl p-12 text-center">
          <span className="material-symbols-outlined text-6xl mb-4">
            support_agent
          </span>
          <h2 className="font-playfair text-3xl font-bold mb-4">
            Need Help with a Return?
          </h2>
          <p className="text-lg mb-6 opacity-90">
            Our customer support team is here to assist you with any questions about returns or refunds.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a
              href="/contact"
              className="inline-block bg-white text-deep-sage px-8 py-3 rounded-full font-medium hover:bg-soft-peach transition-all"
            >
              Contact Support
            </a>
            <a
              href="/faq"
              className="inline-block bg-transparent border-2 border-white text-white px-8 py-3 rounded-full font-medium hover:bg-white hover:text-deep-sage transition-all"
            >
              View FAQ
            </a>
          </div>
        </div>
      </div>
    </div>
  )
}
