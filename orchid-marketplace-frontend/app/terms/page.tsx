import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function TermsPage() {
  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1000px] mx-auto px-4 py-16">
          <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
            Terms of Service
          </h1>
          
          <p className="text-center text-sage-green mb-12">
            Last Updated: January 22, 2026
          </p>
          
          <div className="bg-white rounded-xl p-12 space-y-8">
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                1. Acceptance of Terms
              </h2>
              <p className="text-sage-green leading-relaxed">
                By accessing and using Orchidillo ("the Service"), you accept and agree to be bound by the terms and provision of this agreement. If you do not agree to abide by the above, please do not use this service. These Terms of Service constitute a legally binding agreement made between you and Orchidillo concerning your access to and use of the Service.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                2. User Accounts
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                When you create an account with us, you must provide information that is accurate, complete, and current at all times. Failure to do so constitutes a breach of the Terms, which may result in immediate termination of your account on our Service.
              </p>
              <p className="text-sage-green leading-relaxed">
                You are responsible for safeguarding the password that you use to access the Service and for any activities or actions under your password, whether your password is with our Service or a third-party service. You agree not to disclose your password to any third party.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                3. Marketplace Rules
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                Orchidillo provides a platform for buyers and sellers to conduct transactions. All sales are directly between buyers and sellers. Orchidillo is not a party to any sale and does not own, sell, resell, or control any products listed on the platform.
              </p>
              <p className="text-sage-green leading-relaxed">
                Sellers are responsible for the accuracy of their listings, timely shipping, and customer service. Buyers are responsible for reading product descriptions carefully and communicating with sellers about any questions or concerns before purchasing.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                4. Payments and Fees
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                All payments are processed through our secure payment processor. Sellers agree to pay applicable listing fees, transaction fees, and other charges as outlined in our fee schedule. Buyers agree to pay the full purchase price plus applicable taxes and shipping costs.
              </p>
              <p className="text-sage-green leading-relaxed">
                Refunds and returns are handled according to individual seller policies and our platform guidelines. Orchidillo reserves the right to refund transactions in cases of fraud or violation of these terms.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                5. Prohibited Conduct
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                You agree not to use the Service to:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Violate any local, state, national, or international law</li>
                <li>Sell counterfeit, stolen, or illegal items</li>
                <li>Infringe on intellectual property rights of others</li>
                <li>Harass, abuse, or harm other users</li>
                <li>Engage in fraudulent activities or misrepresent products</li>
                <li>Attempt to gain unauthorized access to the Service or other accounts</li>
                <li>Transmit viruses, malware, or other harmful code</li>
                <li>Scrape or harvest data from the Service without permission</li>
              </ul>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                6. Intellectual Property
              </h2>
              <p className="text-sage-green leading-relaxed">
                The Service and its original content, features, and functionality are owned by Orchidillo and are protected by international copyright, trademark, patent, trade secret, and other intellectual property laws. You may not reproduce, distribute, modify, create derivative works of, publicly display, or exploit any content from the Service without our express written permission.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                7. Termination
              </h2>
              <p className="text-sage-green leading-relaxed">
                We may terminate or suspend your account immediately, without prior notice or liability, for any reason whatsoever, including without limitation if you breach the Terms. Upon termination, your right to use the Service will immediately cease. All provisions of the Terms which by their nature should survive termination shall survive termination.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                8. Limitation of Liability
              </h2>
              <p className="text-sage-green leading-relaxed">
                In no event shall Orchidillo, nor its directors, employees, partners, agents, suppliers, or affiliates, be liable for any indirect, incidental, special, consequential or punitive damages, including without limitation, loss of profits, data, use, goodwill, or other intangible losses, resulting from your access to or use of or inability to access or use the Service.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                9. Disclaimer
              </h2>
              <p className="text-sage-green leading-relaxed">
                Your use of the Service is at your sole risk. The Service is provided on an "AS IS" and "AS AVAILABLE" basis. The Service is provided without warranties of any kind, whether express or implied, including, but not limited to, implied warranties of merchantability, fitness for a particular purpose, non-infringement or course of performance.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                10. Changes to Terms
              </h2>
              <p className="text-sage-green leading-relaxed">
                We reserve the right, at our sole discretion, to modify or replace these Terms at any time. If a revision is material, we will try to provide at least 30 days' notice prior to any new terms taking effect. What constitutes a material change will be determined at our sole discretion. By continuing to access or use our Service after those revisions become effective, you agree to be bound by the revised terms.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                11. Contact Information
              </h2>
              <p className="text-sage-green leading-relaxed">
                If you have any questions about these Terms, please contact us at legal@orchidillo.com or through our contact page.
              </p>
            </section>
          </div>
          
          {/* Note about acceptance */}
          <div className="mt-8 bg-accent-peach/10 border border-accent-peach rounded-xl p-6">
            <p className="text-sage-green text-sm text-center">
              <strong>Note:</strong> By creating an account or using Orchidillo, you acknowledge that you have read, understood, and agree to be bound by these Terms of Service and our Privacy Policy.
            </p>
          </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
