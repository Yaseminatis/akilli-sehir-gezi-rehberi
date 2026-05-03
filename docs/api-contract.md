# API Sözleşmesi Taslağı

Bu doküman, Akıllı Şehir Gezi Rehberi API projesinde kullanılacak temel endpoint yapılarını açıklar.

---

## City API

### Şehirleri Listeleme

GET /api/cities

Açıklama:
Sistemde kayıtlı tüm şehirleri listeler.

---

### Şehir Detayı Getirme

GET /api/cities/{id}

Açıklama:
Belirli bir şehrin detay bilgisini getirir.

---

### Şehir Ekleme

POST /api/cities

Örnek istek:

```json
{
  "name": "Kocaeli",
  "country": "Türkiye",
  "description": "Marmara Bölgesi'nde yer alan şehir",
  "imageUrl": "kocaeli.jpg"
}
```

---

## Place API

### Gezilecek Yerleri Listeleme

GET /api/places

Açıklama:
Sistemde kayıtlı gezilecek yerleri listeler.

---

### Şehre Göre Gezilecek Yerleri Listeleme

GET /api/cities/{cityId}/places

Açıklama:
Belirli bir şehre ait gezilecek yerleri listeler.

---

### Gezilecek Yer Ekleme

POST /api/places

Örnek istek:

```json
{
  "name": "Seka Park",
  "description": "Kocaeli'de yer alan sahil parkı",
  "category": "PARK",
  "address": "İzmit/Kocaeli",
  "imageUrl": "sekapark.jpg",
  "cityId": 1
}
```

---

## Comment API

### Yorumları Listeleme

GET /api/places/{placeId}/comments

Açıklama:
Belirli bir gezilecek yere ait yorumları listeler.

---

### Yorum Ekleme

POST /api/places/{placeId}/comments

Örnek istek:

```json
{
  "userId": 1,
  "content": "Çok güzel bir yerdi."
}
```

---

## Rating API

### Puan Ekleme

POST /api/places/{placeId}/ratings

Örnek istek:

```json
{
  "userId": 1,
  "score": 5
}
```

---

### Ortalama Puan Getirme

GET /api/places/{placeId}/ratings/average

Açıklama:
Belirli bir gezilecek yerin ortalama puanını getirir.

---

## Hata Kodları

| Durum | HTTP Kodu |
|------|-----------|
| Veri bulunamadı | 404 |
| Geçersiz istek | 400 |
| Sunucu hatası | 500 |