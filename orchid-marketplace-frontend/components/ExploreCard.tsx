import Image from 'next/image'

export default function ExploreCard({ title, image }: { title: string; image: string }) {
  return (
    <div className="block">
      <div className="aspect-square rounded-2xl overflow-hidden bg-primary-peach/10 border border-primary-peach/20">
        <div className="w-full h-full relative">
          <Image src={image} alt={title} fill className="object-cover" />
        </div>
      </div>
    </div>
  )
}
