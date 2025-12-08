import sqlite3
import requests
import time

DB_PATH = "geo_cache.db"


class GeoService:

    def __init__(self):
        self._create_table()

    def _create_table(self):
        conn = sqlite3.connect(DB_PATH)
        cur = conn.cursor()
        cur.execute("""
            CREATE TABLE IF NOT EXISTS geo_cache (
                location TEXT PRIMARY KEY,
                latitude REAL,
                longitude REAL,
                updated_at INTEGER
            )
        """)
        conn.commit()
        conn.close()

    def get_coordinates(self, location):
        cleaned = self._clean(location)
        cached = self._get_cached(cleaned)

        if cached:
            return cached

        coords = self._query_api(cleaned)

        if coords:
            self._save(cleaned, coords)
            return coords

        return None

    def _clean(self, text):
        if not text:
            return ""
        text = text.strip()
        text = " ".join(text.split())
        return text

    def _get_cached(self, location):
        conn = sqlite3.connect(DB_PATH)
        cur = conn.cursor()
        cur.execute("SELECT latitude, longitude FROM geo_cache WHERE location = ?", (location,))
        row = cur.fetchone()
        conn.close()

        if row:
            return row[0], row[1]
        return None

    def _save(self, location, coords):
        conn = sqlite3.connect(DB_PATH)
        cur = conn.cursor()

        cur.execute(
            "REPLACE INTO geo_cache (location, latitude, longitude, updated_at) VALUES (?, ?, ?, ?)",
            (location, coords[0], coords[1], int(time.time()))
        )

        conn.commit()
        conn.close()

    def _query_api(self, location):
        try:
            url = "https://nominatim.openstreetmap.org/search"
            params = {
                "q": location + ", Sri Lanka",
                "format": "json"
            }
            headers = {"User-Agent": "GeoLookupScript"}

            res = requests.get(url, params=params, headers=headers, timeout=10)
            data = res.json()

            if not data:
                return None

            lat = float(data[0]["lat"])
            lon = float(data[0]["lon"])
            return (lat, lon)
        except Exception:
            return None
