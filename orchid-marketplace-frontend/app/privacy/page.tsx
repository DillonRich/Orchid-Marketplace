import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function PrivacyPage() {
  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1000px] mx-auto px-4 py-16">
          <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
            Privacy Policy
          </h1>
          
          <p className="text-center text-sage-green mb-12">
            Last Updated: January 22, 2026
          </p>
          
          <div className="bg-white rounded-xl p-12 space-y-8">
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                1. Information We Collect
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                We collect information that you provide directly to us, including when you create an account, make a purchase, communicate with sellers, or contact our support team. This may include:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Name, email address, phone number, and mailing address</li>
                <li>Payment information (processed securely through our payment provider)</li>
                <li>Profile information, preferences, and communication history</li>
                <li>Product reviews, ratings, and other user-generated content</li>
                <li>Information about your transactions and order history</li>
              </ul>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                2. Automatically Collected Information
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                When you use our Service, we automatically collect certain information about your device and usage patterns:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Device information (IP address, browser type, operating system)</li>
                <li>Usage data (pages visited, time spent, click patterns)</li>
                <li>Cookies and similar tracking technologies</li>
                <li>Location data (with your permission)</li>
              </ul>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                3. How We Use Your Information
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                We use the information we collect for various purposes, including:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li>Providing, maintaining, and improving our Service</li>
                <li>Processing your orders and facilitating transactions</li>
                <li>Sending order confirmations, shipping updates, and customer support messages</li>
                <li>Personalizing your experience and providing relevant recommendations</li>
                <li>Sending marketing communications (with your consent)</li>
                <li>Detecting and preventing fraud, abuse, and security incidents</li>
                <li>Complying with legal obligations and enforcing our Terms of Service</li>
              </ul>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                4. Information Sharing
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                We may share your information in the following circumstances:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li><strong>With Sellers:</strong> We share necessary information with sellers to fulfill your orders</li>
                <li><strong>Service Providers:</strong> We work with third-party companies for payment processing, shipping, analytics, and customer support</li>
                <li><strong>Business Transfers:</strong> In the event of a merger, acquisition, or sale of assets</li>
                <li><strong>Legal Requirements:</strong> When required by law or to protect our rights and safety</li>
                <li><strong>With Your Consent:</strong> When you explicitly agree to share information</li>
              </ul>
              <p className="text-sage-green leading-relaxed mt-3">
                We do not sell your personal information to third parties for their marketing purposes.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                5. Cookies and Tracking Technologies
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                We use cookies and similar tracking technologies to track activity on our Service and hold certain information. Cookies are files with small amounts of data that are sent to your browser from a website and stored on your device.
              </p>
              <p className="text-sage-green leading-relaxed">
                You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our Service. We use both session cookies (which expire when you close your browser) and persistent cookies (which remain on your device).
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                6. Data Security
              </h2>
              <p className="text-sage-green leading-relaxed">
                We implement appropriate technical and organizational security measures to protect your personal information against unauthorized access, alteration, disclosure, or destruction. This includes encryption of data in transit and at rest, secure payment processing, regular security assessments, and employee training. However, no method of transmission over the Internet or electronic storage is 100% secure, and we cannot guarantee absolute security.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                7. Your Rights and Choices
              </h2>
              <p className="text-sage-green leading-relaxed mb-3">
                You have certain rights regarding your personal information:
              </p>
              <ul className="list-disc pl-8 text-sage-green space-y-2">
                <li><strong>Access:</strong> Request a copy of the personal information we hold about you</li>
                <li><strong>Correction:</strong> Update or correct inaccurate information</li>
                <li><strong>Deletion:</strong> Request deletion of your account and associated data</li>
                <li><strong>Opt-Out:</strong> Unsubscribe from marketing emails at any time</li>
                <li><strong>Data Portability:</strong> Request your data in a portable format</li>
                <li><strong>Restrict Processing:</strong> Request that we limit how we use your data</li>
              </ul>
              <p className="text-sage-green leading-relaxed mt-3">
                To exercise these rights, please contact us at privacy@orchidillo.com or through your account settings.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                8. Children's Privacy
              </h2>
              <p className="text-sage-green leading-relaxed">
                Our Service is not intended for children under the age of 13. We do not knowingly collect personal information from children under 13. If you are a parent or guardian and believe your child has provided us with personal information, please contact us so we can delete the information.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                9. International Data Transfers
              </h2>
              <p className="text-sage-green leading-relaxed">
                Your information may be transferred to and maintained on computers located outside of your state, province, country, or other governmental jurisdiction where data protection laws may differ. By using our Service, you consent to the transfer of your information to facilities located in the United States and other countries where we operate.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                10. Data Retention
              </h2>
              <p className="text-sage-green leading-relaxed">
                We retain your personal information for as long as necessary to fulfill the purposes outlined in this Privacy Policy, unless a longer retention period is required or permitted by law. When you delete your account, we will delete or anonymize your personal information, though some information may be retained for legal, security, or operational purposes.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                11. Changes to This Privacy Policy
              </h2>
              <p className="text-sage-green leading-relaxed">
                We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page and updating the "Last Updated" date. You are advised to review this Privacy Policy periodically for any changes. Changes to this Privacy Policy are effective when they are posted on this page.
              </p>
            </section>
            
            <section>
              <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4">
                12. Contact Us
              </h2>
              <p className="text-sage-green leading-relaxed">
                If you have any questions about this Privacy Policy or our data practices, please contact us at:
              </p>
              <div className="mt-4 text-sage-green">
                <p>Email: privacy@orchidillo.com</p>
                <p>Phone: 1-800-ORCHID-0</p>
                <p>Address: 123 Garden Way, Plant City, PC 12345</p>
              </div>
            </section>
          </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
