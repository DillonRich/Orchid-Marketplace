import { NextResponse } from 'next/server'
import fs from 'fs'
import path from 'path'

const DATA_FILE = path.join(process.cwd(), 'data', 'favorites.json')

function ensureDataFile() {
  const dir = path.dirname(DATA_FILE)
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true })
  if (!fs.existsSync(DATA_FILE)) fs.writeFileSync(DATA_FILE, JSON.stringify({}), 'utf-8')
}

function readAll() {
  ensureDataFile()
  const raw = fs.readFileSync(DATA_FILE, 'utf-8')
  try { return JSON.parse(raw) } catch { return {} }
}

function writeAll(obj: any) {
  ensureDataFile()
  fs.writeFileSync(DATA_FILE, JSON.stringify(obj, null, 2), 'utf-8')
}

// POST: { items: string[], localId?: string }
export async function POST(req: Request) {
  try {
    // prefer authenticated user identification via header (integrations should set this)
    const userId = req.headers.get('x-user-id')
    if (!userId) return NextResponse.json({ error: 'missing userId' }, { status: 400 })
    const body = await req.json()
    const items: string[] = Array.isArray(body?.items) ? body.items : []
    const localId: string | undefined = body?.localId

    const all = readAll()
    const existing = new Set([...(all[userId] || [])])
    items.forEach((i) => existing.add(i))
    all[userId] = Array.from(existing)

    // Optionally remove the local bucket (if provided and different)
    if (localId && localId !== userId) {
      delete all[localId]
    }

    writeAll(all)
    return NextResponse.json({ ok: true, items: all[userId] })
  } catch (e) {
    return NextResponse.json({ error: 'server_error' }, { status: 500 })
  }
}
