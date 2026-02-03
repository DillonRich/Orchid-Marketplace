import Header from '@/components/Header'
import Footer from '@/components/Footer'

export default function CareGuidesPage() {
  const guides = [
    {
      title: 'Light Requirements',
      icon: 'light_mode',
      content: [
        {
          subtitle: 'Understanding Orchid Light Needs',
          text: 'Most orchids thrive in bright, indirect light. Direct sunlight can scorch leaves, while insufficient light prevents blooming. East or west-facing windows are ideal for most varieties.'
        },
        {
          subtitle: 'Signs of Proper Lighting',
          text: 'Healthy orchid leaves should be bright green. Dark green leaves indicate insufficient light, while reddish or yellow leaves suggest too much direct sun. Adjust placement accordingly.'
        },
        {
          subtitle: 'Artificial Lighting',
          text: 'If natural light is limited, LED grow lights work excellently. Place lights 6-12 inches above orchids and provide 12-14 hours of light daily for optimal growth and flowering.'
        }
      ]
    },
    {
      title: 'Watering Techniques',
      icon: 'water_drop',
      content: [
        {
          subtitle: 'The Golden Rule',
          text: 'Water thoroughly when the potting medium is nearly dry. Most orchids prefer to dry out slightly between waterings. Overwatering is the leading cause of orchid death, causing root rot and fungal issues.'
        },
        {
          subtitle: 'Watering Frequency',
          text: 'Typically, water once every 7-10 days in summer and every 10-14 days in winter. Factors like pot size, medium type, humidity, and temperature affect frequency. Always check the medium before watering.'
        },
        {
          subtitle: 'Best Practices',
          text: 'Water in the morning to allow excess moisture to evaporate. Use room-temperature water and avoid getting water in the crown (center of leaves) to prevent rot. Ensure pots have drainage holes.'
        },
        {
          subtitle: 'Ice Cube Method',
          text: 'While popular, the ice cube method is controversial. Many experts recommend room-temperature water instead. If using ice, place 2-3 cubes on the medium weekly, but monitor plant health closely.'
        }
      ]
    },
    {
      title: 'Temperature & Humidity',
      icon: 'thermostat',
      content: [
        {
          subtitle: 'Temperature Ranges',
          text: 'Most common orchids (Phalaenopsis) prefer daytime temperatures of 70-80°F and nighttime temperatures of 60-65°F. A 10-15 degree night drop encourages blooming in many species.'
        },
        {
          subtitle: 'Humidity Requirements',
          text: 'Orchids thrive in 40-70% humidity. In dry environments, place orchids on humidity trays (trays filled with pebbles and water), use a humidifier, or group plants together to increase local humidity.'
        },
        {
          subtitle: 'Air Circulation',
          text: 'Good air movement is crucial to prevent fungal and bacterial diseases. Use a small fan on low speed to keep air circulating around your orchids, especially in high-humidity environments.'
        }
      ]
    },
    {
      title: 'Potting & Repotting',
      icon: 'potted_plant',
      content: [
        {
          subtitle: 'When to Repot',
          text: 'Repot every 1-2 years or when the potting medium breaks down and no longer drains well. The best time is after flowering when new roots appear, typically in spring.'
        },
        {
          subtitle: 'Choosing the Right Medium',
          text: 'Most orchids are epiphytes and need a chunky, well-draining medium. Bark-based mixes are most common. Phalaenopsis prefer fine to medium bark, while Cattleyas need coarser bark. Avoid regular potting soil.'
        },
        {
          subtitle: 'Pot Selection',
          text: 'Clear plastic pots allow you to monitor root health and moisture levels. Ensure pots have multiple drainage holes. Size the pot just large enough to accommodate roots—orchids prefer being slightly root-bound.'
        },
        {
          subtitle: 'Repotting Steps',
          text: 'Remove the orchid carefully, trim dead or mushy roots with sterilized scissors, and place in fresh medium. Firm gently but don\'t pack tightly. Water lightly after a few days to allow cut roots to heal.'
        }
      ]
    },
    {
      title: 'Fertilizing',
      icon: 'science',
      content: [
        {
          subtitle: 'Fertilizer Types',
          text: 'Use a balanced orchid fertilizer (20-20-20) or a bloom-boosting formula (10-30-20) during flowering season. "Weakly, weekly" is the mantra—dilute fertilizer to 1/4 strength and apply weekly during active growth.'
        },
        {
          subtitle: 'Application Schedule',
          text: 'Fertilize every week during spring and summer (active growth), reduce to every 2-3 weeks in fall, and monthly in winter. Always water first, then fertilize to prevent root burn.'
        },
        {
          subtitle: 'Salt Buildup Prevention',
          text: 'Flush pots monthly with plain water to remove fertilizer salt buildup. Pour water through the pot 2-3 times to leach out accumulated salts that can damage roots.'
        }
      ]
    },
    {
      title: 'Blooming & Reblooming',
      icon: 'local_florist',
      content: [
        {
          subtitle: 'Encouraging Blooms',
          text: 'Consistent care, proper light, and a temperature drop at night (10-15°F) trigger blooming. For Phalaenopsis, maintain cooler nighttime temperatures (55-60°F) for 2-4 weeks to initiate spikes.'
        },
        {
          subtitle: 'After Flowering',
          text: 'Once blooms fade, you can cut the spike above a node (bump on the stem) to encourage a secondary bloom, or cut at the base to allow the plant to conserve energy for the next flowering cycle.'
        },
        {
          subtitle: 'Bloom Duration',
          text: 'Phalaenopsis orchids can bloom for 2-4 months. Proper care during and after blooming ensures the plant has energy for reblooming. Most orchids bloom once or twice yearly with proper conditions.'
        }
      ]
    },
    {
      title: 'Common Problems & Solutions',
      icon: 'medical_services',
      content: [
        {
          subtitle: 'Yellow Leaves',
          text: 'Lower leaves naturally yellow with age. Sudden yellowing indicates overwatering, underwatering, or too much direct sun. Check roots for rot and adjust care accordingly.'
        },
        {
          subtitle: 'No Blooms',
          text: 'Insufficient light is the most common cause. Lack of temperature differential, improper fertilization, or stress from recent repotting can also prevent blooming. Ensure proper light and cooler nights.'
        },
        {
          subtitle: 'Root Rot',
          text: 'Caused by overwatering or poor drainage. Healthy roots are firm and white/green; rotten roots are brown/black and mushy. Remove affected roots, repot in fresh medium, and reduce watering.'
        },
        {
          subtitle: 'Pests',
          text: 'Common pests include scale, mealybugs, and spider mites. Isolate affected plants immediately. Treat with insecticidal soap, neem oil, or isopropyl alcohol on a cotton swab for targeted removal.'
        },
        {
          subtitle: 'Leaf Spots & Fungus',
          text: 'Usually caused by water on leaves, poor air circulation, or high humidity. Remove affected leaves, improve air flow, avoid overhead watering, and apply a fungicide if necessary.'
        }
      ]
    },
    {
      title: 'Orchid Varieties & Their Needs',
      icon: 'category',
      content: [
        {
          subtitle: 'Phalaenopsis (Moth Orchids)',
          text: 'Easiest for beginners. Low to medium light, warm temperatures (65-80°F), water weekly. Most common in stores with long-lasting flowers. Perfect for homes and offices.'
        },
        {
          subtitle: 'Cattleya',
          text: 'Bright light lovers needing more intense light than Phalaenopsis. Prefer intermediate temperatures and slightly drier conditions. Known for large, fragrant blooms. Need coarse bark medium.'
        },
        {
          subtitle: 'Dendrobium',
          text: 'Diverse genus with varying care needs. Most need bright light, moderate water, and a dry winter rest period. Deciduous types drop leaves naturally in winter.'
        },
        {
          subtitle: 'Oncidium (Dancing Lady)',
          text: 'Need bright light and good air circulation. Prefer to dry out between waterings. Produce abundant small, cheerful flowers on branching sprays. Tolerant of temperature fluctuations.'
        },
        {
          subtitle: 'Paphiopedilum (Lady Slipper)',
          text: 'Terrestrial orchids preferring lower light than most. Enjoy consistent moisture (not wet). No pseudobulbs means they can\'t store water. Unique pouch-shaped flowers last months.'
        },
        {
          subtitle: 'Cymbidium',
          text: 'Require cool nights (40-55°F) for blooming. Need bright light and regular watering. Best suited for cooler climates or outdoors in mild areas. Large plants with long-lasting sprays.'
        }
      ]
    }
  ]

  return (
    <>
      <Header />
      <div className="bg-warm-cream min-h-screen">
        <div className="max-w-[1200px] mx-auto px-4 py-16">
          <h1 className="font-playfair text-5xl font-bold text-deep-sage mb-6 text-center">
            Orchid Care Guides
          </h1>
          
          <p className="text-center text-sage-green text-lg mb-12 max-w-[800px] mx-auto">
            Everything you need to know to keep your orchids healthy, happy, and blooming beautifully. 
            From beginner basics to advanced techniques, we've got you covered.
          </p>
          
          {/* Quick Tips Banner */}
          <div className="bg-gradient-to-r from-deep-sage to-sage-green text-white rounded-xl p-8 mb-16">
            <h2 className="font-playfair text-2xl font-bold mb-4">Quick Care Tips</h2>
            <div className="grid md:grid-cols-3 gap-6">
              <div>
                <span className="material-symbols-outlined text-4xl mb-2 block">light_mode</span>
                <h3 className="font-medium mb-1">Bright, Indirect Light</h3>
                <p className="text-sm opacity-90">East or west windows are ideal</p>
              </div>
              <div>
                <span className="material-symbols-outlined text-4xl mb-2 block">water_drop</span>
                <h3 className="font-medium mb-1">Water Wisely</h3>
                <p className="text-sm opacity-90">Let medium dry between waterings</p>
              </div>
              <div>
                <span className="material-symbols-outlined text-4xl mb-2 block">thermostat</span>
                <h3 className="font-medium mb-1">Cooler Nights</h3>
                <p className="text-sm opacity-90">10-15°F drop encourages blooming</p>
              </div>
            </div>
          </div>
          
          {/* Comprehensive Guides */}
          <div className="space-y-8">
            {guides.map((guide, index) => (
              <div key={index} className="bg-white rounded-xl p-10">
                <div className="flex items-center gap-4 mb-8">
                  <div className="w-16 h-16 bg-soft-peach rounded-full flex items-center justify-center">
                    <span className="material-symbols-outlined text-deep-sage text-3xl">
                      {guide.icon}
                    </span>
                  </div>
                  <h2 className="font-playfair text-3xl font-bold text-deep-sage">
                    {guide.title}
                  </h2>
                </div>
                
                <div className="space-y-6">
                  {guide.content.map((section, sectionIndex) => (
                    <div key={sectionIndex}>
                      <h3 className="font-medium text-lg text-deep-sage mb-2">
                        {section.subtitle}
                      </h3>
                      <p className="text-sage-green leading-relaxed">
                        {section.text}
                      </p>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
          
          {/* Additional Resources */}
          <div className="mt-16 bg-accent-peach/10 border border-accent-peach rounded-xl p-8">
            <h2 className="font-playfair text-2xl font-bold text-deep-sage mb-4 text-center">
              Need More Help?
            </h2>
            <p className="text-sage-green text-center mb-6">
              Our community and support team are here to help with all your orchid care questions
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <a
                href="/contact"
                className="inline-block bg-soft-peach hover:bg-accent-peach text-deep-sage px-8 py-3 rounded-full font-medium transition-all text-center"
              >
                Contact Support
              </a>
              <a
                href="/faq"
                className="inline-block bg-white hover:bg-soft-peach text-deep-sage px-8 py-3 rounded-full font-medium border-2 border-accent-peach transition-all text-center"
              >
                View FAQs
              </a>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  )
}
