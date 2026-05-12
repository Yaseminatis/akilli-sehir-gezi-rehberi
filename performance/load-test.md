# Performans Testleri

Projede temel performans testleri gerçekleştirilmiştir.

Test edilen endpointler:

- GET /api/places
- GET /api/cities
- GET /api/nosql/cache/recent-searches
- GET /api/nosql/favorites/user/{userId}

Test Senaryoları:

- Çoklu GET isteği
- Eş zamanlı endpoint erişimi
- Cache endpoint erişim testi
- MongoDB response testi

Gözlemler:

- Endpoint response süreleri düşük seviyede kalmıştır.
- MongoDB cache yapıları hızlı response üretmiştir.
- API response yapısı stabil çalışmıştır.
- Exception handler performans kaybı oluşturmamıştır.

Sonuç:

Sistem küçük ve orta ölçekli yük altında stabil çalışmaktadır.