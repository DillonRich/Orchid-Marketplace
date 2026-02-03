import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function AboutPage() {
  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1000px] mx-auto px-4 py-16">
        <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
          About Orchidillo
        </h1>
        
        <div className="bg-white rounded-xl p-12 mb-12">
          <p className="text-lg text-sage-green leading-relaxed mb-6">
            Welcome to Orchidillo, your premier destination for exquisite orchids and botanical treasures. Founded with a passion for these magnificent flowers, we've created a marketplace that connects orchid enthusiasts with trusted sellers from around the world.
          </p>
          
          <p className="text-lg text-sage-green leading-relaxed mb-6">
            Our mission is simple: to make the beauty and elegance of orchids accessible to everyone, whether you're a seasoned collector or just beginning your orchid journey.
          </p>
        </div>
        
        {/* Our Story */}
        <div className="bg-white rounded-xl p-12 mb-12">
          <h2 className="font-playfair text-3xl font-bold text-deep-sage mb-6">
            Our Story
          </h2>
          <p className="text-sage-green leading-relaxed mb-4">
            Orchidillo was born from a love of orchids and a desire to create a trusted marketplace where collectors and enthusiasts could discover rare and beautiful varieties. We've built relationships with vetted sellers who share our commitment to quality and customer satisfaction.
          </p>
          <p className="text-sage-green leading-relaxed">
            Today, we're proud to serve thousands of orchid lovers worldwide, offering an ever-growing selection of Phalaenopsis, Cattleya, Dendrobium, and many other stunning varieties.
          </p>
        </div>
        
        {/* Our Values */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-12">
          <div className="bg-white rounded-xl p-8 text-center">
            <span className="material-symbols-outlined text-6xl text-deep-sage mb-4">
              verified
            </span>
            <h3 className="font-medium text-deep-sage text-xl mb-3">Quality First</h3>
            <p className="text-sage-green">
              Every seller is carefully vetted to ensure you receive only the healthiest, most beautiful orchids.
            </p>
          </div>
          
          <div className="bg-white rounded-xl p-8 text-center">
            <span className="material-symbols-outlined text-6xl text-deep-sage mb-4">
              favorite
            </span>
            <h3 className="font-medium text-deep-sage text-xl mb-3">Customer Care</h3>
            <p className="text-sage-green">
              Your satisfaction is our priority. We're here to help with every step of your orchid journey.
            </p>
          </div>
          
          <div className="bg-white rounded-xl p-8 text-center">
            <span className="material-symbols-outlined text-6xl text-deep-sage mb-4">
              eco
            </span>
            <h3 className="font-medium text-deep-sage text-xl mb-3">Sustainability</h3>
            <p className="text-sage-green">
              We promote ethical sourcing and sustainable growing practices among all our sellers.
            </p>
          </div>
        </div>
        
        {/* Contact CTA */}
        <div className="bg-gradient-to-r from-deep-sage to-sage-green text-white rounded-xl p-12 text-center">
          <h2 className="font-playfair text-3xl font-bold mb-4">
            Have Questions?
          </h2>
          <p className="text-lg mb-6 opacity-90">
            We'd love to hear from you. Reach out anytime!
          </p>
          <a
            href="/contact"
            className="inline-block bg-white text-deep-sage px-8 py-3 rounded-full font-medium hover:bg-soft-peach transition-all"
          >
            Contact Us
          </a>
        </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
