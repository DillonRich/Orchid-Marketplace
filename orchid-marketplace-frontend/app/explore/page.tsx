import Header from '@/components/Header'
import Footer from '@/components/Footer'
import ExploreCard from '@/components/ExploreCard'
import Link from 'next/link'

const species = [
  { title: 'Phalaenopsis amabilis', common: 'Moon Orchid, Moth Orchid', sub: 'Subspecies amabilis, moluccana, rosenstromii', desc: 'The quintessential "moth orchid," with arching sprays of long-lasting, white, fragrant flowers. It is a parent of many modern hybrids and is widely cultivated globally.' },
  { title: 'Phalaenopsis schilleriana', common: "Schiller's Phalaenopsis", sub: 'Several regional varieties', desc: "Known for its silver-mottled leaves and prolific sprays of pink to lavender flowers. Highly valued for its foliage and floriferousness." },
  { title: 'Dendrobium nobile', common: 'Noble Dendrobium', sub: 'Many hybrids and color forms', desc: 'A classic hard-cane dendrobium with fragrant flowers that appear along the nodes of the canes. Requires a cool, dry winter rest to bloom.' },
  { title: 'Dendrobium bigibbum', common: 'Cooktown Orchid, Mauve Butterfly Orchid', sub: 'Varieties: bigibbum, compactum, schoederianum, superbum', desc: 'The floral emblem of Queensland, Australia. Produces showy sprays of lilac-purple flowers and is a parent of many popular Dendrobium phalaenopsis-type hybrids.' },
  { title: 'Cattleya labiata', common: 'Crimson Cattleya, Ruby-lipped Cattleya', sub: 'Several varieties', desc: 'The type species of the genus, with large, fragrant, lavender-pink flowers and a prominent, richly colored lip. Historically the classic corsage orchid.' },
  { title: 'Oncidium sphacelatum', common: 'Dancing Lady Orchid', sub: 'None widely recognized', desc: 'A robust species with tall, branched inflorescences carrying numerous small, yellow-and-brown flowers with a distinctive "dancing lady" shape.' },
  { title: 'Paphiopedilum insigne', common: 'Slipper Orchid', sub: 'Several varieties', desc: 'One of the first lady\'s slipper orchids to be cultivated. It has a solitary, waxy flower with a brown spotted greenish dorsal sepal and a yellow, pouch-like lip.' },
  { title: 'Cymbidium goeringii', common: 'Spring Cymbidium', sub: 'Several varieties', desc: 'A highly revered, cool-growing species in East Asia, known for its fragrant, modest-sized flowers and grass-like foliage.' },
  { title: 'Vanda coerulea', common: 'Blue Vanda', sub: 'None widely recognized', desc: 'Famous for its striking, long-lasting blue flowers. A highly sought-after species used to create blue-flowered hybrids.' },
  { title: 'Miltonia spectabilis', common: 'Pansy Orchid', sub: 'Varieties spectabilis and moreliana', desc: 'A Brazilian species with large, flat, pansy-like flowers, usually white or pink with a contrasting lip.' },
  { title: 'Brassia arcuigera', common: 'Arching Brassia', sub: 'Subspecies longissima', desc: 'A "spider orchid" with exceptionally long, greenish-yellow sepals and petals that can span over 50 cm. Known for dramatic, arching inflorescences.' },
  { title: 'Zygopetalum intermedium', common: 'Zygopetalum Orchid', sub: 'Related species often sold under this name', desc: 'Known for fragrant, waxy flowers with striking green petals and sepals heavily marked with burgundy-brown, and a white, purple-veined lip.' },
  { title: 'Masdevallia veitchiana', common: 'King of the Masdevallias', sub: 'None widely recognized', desc: 'A stunning, cool-growing species from the Andes, with large, brilliant orange-red, tubular flowers. It is a flagship species for the genus.' },
  { title: 'Ludisia discolor', common: 'Jewel Orchid', sub: 'Varieties with different leaf colors', desc: 'Grown primarily for its stunning velvety foliage—dark green with red or silver veins—rather than its small white flowers.' },
  { title: 'Phaius tankervilleae', common: "Nun's Hood Orchid, Swamp Orchid", sub: 'None widely recognized', desc: 'A terrestrial orchid with tall, dramatic spikes of brown, white, and purple flowers. Easy to grow and popular in garden settings.' },
  { title: 'Maxillaria tenuifolia', common: 'Coconut Orchid', sub: 'None widely recognized', desc: 'A compact species with grass-like leaves and solitary, red-and-yellow flowers that emit a strong coconut fragrance.' },
  { title: 'Laelia anceps', common: 'Laelia', sub: 'Several varieties', desc: 'A resilient, sun-loving orchid similar to Cattleya, producing sprays of pink to lavender flowers in winter. Popular for its reliability and beauty.' },
  { title: 'Catasetum pileatum', common: 'Helmet Orchid', sub: 'None widely recognized', desc: 'Known for its sexually dimorphic flowers and explosive pollen release. The male flowers are large, waxy, and helmet-shaped.' },
  { title: 'Encyclia cordigera', common: 'Chocolate-scented Orchid', sub: 'None widely recognized', desc: 'Produces fragrant, chestnut-brown flowers with a white or pink lip. The flowers have a sweet, chocolate-like scent.' },
  { title: 'Epidendrum ibaguense', common: 'Crucifix Orchid', sub: 'Many color forms', desc: 'A tough, reed-stemmed orchid that produces clusters of bright orange, red, or purple flowers year-round. Extremely easy to grow and propagate.' },
]

export default function ExplorePage() {
  const images = ['/images/orchid1.jpg','/images/Orchid2.jpg','/images/Orchid3.jpg','/images/orchid4.jpg','/images/orchid5.jpg']

  return (
    <div className="min-h-screen bg-soft-peach">
      <Header />

      <main className="max-w-[1280px] mx-auto px-4 md:px-0 py-12">
        <div className="text-center mb-8">
          <h1 className="text-4xl md:text-5xl font-serif text-deep-sage">Explore Species</h1>
          <p className="text-sage-green max-w-2xl mx-auto mt-3">Discover a curated list of orchid species. Click a card to learn more about each type and find related plants in the marketplace.</p>
        </div>

        {/* Species sections: details + dedicated 5x2 gallery per species */}
        <section className="mb-8 space-y-10">
          {species.map((s, i) => (
            <div key={s.title} className="">
              <div className="border border-primary-peach/20 rounded-2xl bg-white/60 relative px-6 py-5 md:px-8 md:py-6">
                <div className="flex-1 min-w-0">
                  <h3 className="font-bold text-deep-sage text-lg">{s.title}</h3>
                  <ul className="mt-3 space-y-3">
                    <li className="flex items-start gap-3">
                      <span className="mt-2 w-2 h-2 bg-deep-sage rounded-full flex-shrink-0" aria-hidden />
                      <div className="min-w-0 flex-1">
                        <div className="flex min-w-0">
                          <strong className="flex-shrink-0 pr-2 text-deep-sage">Common names:</strong>
                          <div className="flex-1 min-w-0 text-sage-green">{s.common}</div>
                        </div>
                      </div>
                    </li>
                    <li className="flex items-start gap-3">
                      <span className="mt-2 w-2 h-2 bg-deep-sage rounded-full flex-shrink-0" aria-hidden />
                      <div className="min-w-0 flex-1">
                        <div className="flex min-w-0">
                          <strong className="flex-shrink-0 pr-2 text-deep-sage">Subcategories:</strong>
                          <div className="flex-1 min-w-0 text-sage-green">{s.sub}</div>
                        </div>
                      </div>
                    </li>
                    <li className="flex items-start gap-3">
                      <span className="mt-2 w-2 h-2 bg-deep-sage rounded-full flex-shrink-0" aria-hidden />
                      <div className="min-w-0 flex-1">
                        <div className="flex min-w-0">
                          <strong className="flex-shrink-0 pr-2 text-deep-sage">Description:</strong>
                          <div className="flex-1 min-w-0 text-sage-green">{s.desc}</div>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>
                <div className="absolute top-5 right-5 hidden sm:block">
                  <Link href={`/products?type=${encodeURIComponent(s.title)}`} className="inline-flex items-center text-deep-sage font-medium hover:text-accent-peach">
                    <span>Shop this species</span>
                    <span className="material-symbols-outlined ml-1">arrow_forward</span>
                  </Link>
                </div>
                {/* Mobile inline link to avoid overlap */}
                <div className="block sm:hidden mt-3">
                  <Link href={`/products?type=${encodeURIComponent(s.title)}`} className="inline-flex items-center text-deep-sage font-medium hover:text-accent-peach">
                    <span>Shop this species</span>
                    <span className="material-symbols-outlined ml-1">arrow_forward</span>
                  </Link>
                </div>
              </div>

              <div className="mt-6">
                <h4 className="text-lg font-medium text-deep-sage mb-4">{s.title} — Gallery</h4>
                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
                  {Array.from({ length: 10 }).map((_, j) => (
                    <ExploreCard
                      key={j}
                      title={s.title}
                      image={images[(i * 10 + j) % images.length]}
                    />
                  ))}
                </div>
              </div>
            </div>
          ))}
        </section>
      </main>

      <Footer />
    </div>
  )
}
