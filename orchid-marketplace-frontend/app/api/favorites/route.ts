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

export async function GET(req: Request) {
  const url = new URL(req.url)
  const userId = url.searchParams.get('userId') || req.headers.get('x-user-id')
  if (!userId) return NextResponse.json({ error: 'missing userId' }, { status: 400 })
  const all = readAll()
  const items = all[userId] || []
  return NextResponse.json({ items })
}

export async function POST(req: Request) {
  const userId = req.headers.get('x-user-id')
  if (!userId) return NextResponse.json({ error: 'missing userId' }, { status: 400 })
  const body = await req.json()
  const id = body?.id
  if (!id) return NextResponse.json({ error: 'missing id' }, { status: 400 })
  const all = readAll()
  all[userId] = Array.from(new Set([...(all[userId] || []), id]))
  writeAll(all)
  return NextResponse.json({ ok: true })
}

export async function DELETE(req: Request) {
  const userId = req.headers.get('x-user-id')
  if (!userId) return NextResponse.json({ error: 'missing userId' }, { status: 400 })
  const body = await req.json()
  const id = body?.id
  if (!id) return NextResponse.json({ error: 'missing id' }, { status: 400 })
  const all = readAll()
  all[userId] = (all[userId] || []).filter((x: string) => x !== id)
  writeAll(all)
  return NextResponse.json({ ok: true })
}
