export default function FAQPage() {
  const faqs = [
    {
      category: 'Ordering',
      questions: [
        {
          q: 'How do I place an order?',
          a: 'Browse our products, add items to your cart, and proceed to checkout. You\'ll need to create an account or sign in to complete your purchase.',
        },
        {
          q: 'Can I modify or cancel my order?',
          a: 'Orders can be modified or cancelled within 2 hours of placement. After that, please contact the seller directly through your order page.',
        },
        {
          q: 'What payment methods do you accept?',
          a: 'We accept all major credit cards (Visa, Mastercard, American Express) and process payments securely through Stripe.',
        },
      ],
    },
    {
      category: 'Shipping',
      questions: [
        {
          q: 'How long does shipping take?',
          a: 'Shipping times vary by seller and location. Most orders arrive within 3-7 business days. You\'ll receive tracking information once your order ships.',
        },
        {
          q: 'Do you ship internationally?',
          a: 'Many of our sellers offer international shipping. Check the product page or contact the seller for specific international shipping options.',
        },
        {
          q: 'What if my plant arrives damaged?',
          a: 'All orders are backed by our quality guarantee. If your plant arrives damaged, contact us within 48 hours with photos for a replacement or refund.',
        },
      ],
    },
    {
      category: 'Plant Care',
      questions: [
        {
          q: 'What care instructions come with my orchid?',
          a: 'Each product includes detailed care instructions. You\'ll also find comprehensive care guides in the product description and via email after purchase.',
        },
        {
          q: 'How often should I water my orchid?',
          a: 'Watering frequency depends on the variety. Generally, orchids prefer to dry slightly between waterings. Check the specific care instructions for your orchid type.',
        },
        {
          q: 'Can I get help with my orchid after purchase?',
          a: 'Absolutely! Contact your seller directly through our messaging system, or reach out to our support team for general orchid care advice.',
        },
      ],
    },
    {
      category: 'For Sellers',
      questions: [
        {
          q: 'How do I become a seller on Orchidillo?',
          a: 'Click "Upgrade to Seller Account" in your account settings. You\'ll need to provide business information and complete our seller verification process.',
        },
        {
          q: 'What are the selling fees?',
          a: 'Orchidillo charges a small commission on each sale (typically 10-15%) plus payment processing fees. There are no listing fees or monthly charges.',
        },
        {
          q: 'How do I get paid?',
          a: 'Payments are processed and transferred to your account weekly. You can track your earnings and payouts in your seller dashboard.',
        },
      ],
    },
    {
      category: 'Returns & Refunds',
      questions: [
        {
          q: 'What is your return policy?',
          a: 'Due to the nature of live plants, returns are evaluated case-by-case. We offer refunds or replacements for damaged plants or quality issues within 7 days of delivery.',
        },
        {
          q: 'How do I request a refund?',
          a: 'Go to your order history, select the order, and click "Request Refund". Provide photos and details about the issue. We\'ll review and respond within 24 hours.',
        },
      ],
    },
  ]
  
  return (
    <div className="bg-warm-cream min-h-screen">
      <div className="max-w-[1000px] mx-auto px-4 py-16">
        <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
          Frequently Asked Questions
        </h1>
        <p className="text-lg text-sage-green text-center mb-12">
          Find answers to common questions about Orchidillo
        </p>
        
        <div className="space-y-8">
          {faqs.map((category, idx) => (
            <div key={idx} className="bg-white rounded-xl p-8">
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-6">
                {category.category}
              </h2>
              
              <div className="space-y-6">
                {category.questions.map((faq, qIdx) => (
                  <div key={qIdx} className="pb-6 border-b border-gray-200 last:border-0 last:pb-0">
                    <h3 className="font-medium text-deep-sage text-lg mb-3">
                      {faq.q}
                    </h3>
                    <p className="text-sage-green leading-relaxed">
                      {faq.a}
                    </p>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
        
        {/* Still have questions? */}
        <div className="bg-gradient-to-r from-deep-sage to-sage-green text-white rounded-xl p-12 text-center mt-12">
          <span className="material-symbols-outlined text-6xl mb-4">
            support_agent
          </span>
          <h2 className="font-playfair text-3xl font-bold mb-4">
            Still Have Questions?
          </h2>
          <p className="text-lg mb-6 opacity-90">
            Can't find what you're looking for? Our support team is here to help!
          </p>
          <a
            href="/contact"
            className="inline-block bg-white text-deep-sage px-8 py-3 rounded-full font-medium hover:bg-soft-peach transition-all"
          >
            Contact Support
          </a>
        </div>
      </div>
    </div>
  )
}
