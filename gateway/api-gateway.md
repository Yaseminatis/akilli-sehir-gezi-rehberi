# API Gateway Yapısı

Bu projede API Gateway mantığı uygulanmıştır.

Tüm endpointler aşağıdaki ortak yapı üzerinden yönetilmektedir:

/api/cities
/api/places
/api/users
/api/ratings
/api/nosql/favorites
/api/nosql/travel-plans
/api/nosql/cache

Gateway yaklaşımı sayesinde:

- endpoint yönetimi merkezi hale getirilmiştir
- servis erişimleri standartlaştırılmıştır
- API yapısı ölçeklenebilir hale getirilmiştir
- ileride microservice mimarisine geçiş kolaylaştırılmıştır

Bu yapı monolith mimari üzerinde gateway mantığını simüle etmek amacıyla geliştirilmiştir.