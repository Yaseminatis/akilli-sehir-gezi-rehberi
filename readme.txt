=========================================================
AKILLI ŞEHİR GEZİ REHBERİ - PROJE TESLİM DOSYASI
=========================================================

1. GRUP BİLGİSİ
Grup No: 14
Proje Konusu: Akıllı Şehir Gezi Rehberi (Spring Boot & JavaFX)
Github Linki:

2. EKİP ÜYELERİ
- Gizem İlhan 211307039
- Yasemin Atış 231307023

3. PROJE ÖZETİ
Kullanıcıların şehirler içerisindeki mekanları görüntüleyebildiği, kategori bazlı arama yapabildiği, favori mekanlarını (NoSQL tabanlı) saklayıp kendilerine özel gezi planları oluşturabildiği, modern arayüzlü ve katmanlı mimariye sahip bir masaüstü istemci & REST API projesidir.

4. İSTERLERE GÖRE GÖREV DAĞILIMI
Proje rubric (kriter) tablosuna göre adil ve dengeli bir görev dağılımı yapılmıştır:

YASEMİN ATIŞ (Backend & API Mimarisi):
- API & Back-end: Spring Boot ile katmanlı mimarinin (Controller, Service, Repository) kurulması.
- Generic Yapılar: DTO dönüşümleri, BaseEntity ve ApiResponse gibi generic sınıfların kodlanması.
- JDBC & NoSQL: Spring Data JPA (SQL) ve MongoDB (NoSQL) veritabanı entegrasyonlarının yapılması.
- Hata Yönetimi: @ExceptionHandler ile global HTTP hata kodlarının (400, 404, 500) yönetilmesi.
- Ek Özellik: MongoDB'nin Docker container üzerinden (Dockerize Sistem) çalıştırılması.

GİZEM İLHAN (İstemci, UI/UX & Entegrasyon):
- Custom GUI: JavaFX ve CSS motoru kullanılarak modern, dinamik (hover/shadow efektli) kullanıcı arayüzünün geliştirilmesi.
- API Client: Backend'e asenkron/senkron HTTP istekleri atan HttpClient ve Gson entegrasyonlu modülün yazılması.
- Performans & Optimizasyon: Frontend seviyesinde veri süzgeçlerinin yazılması, NoSQL/SQL okuma hızlarının arayüz testleri.
- Analiz & Doküman: Proje Github dokümantasyonunun, README dosyasının ve performans test raporlarının hazırlanması.
- Uygulama Mantığı: Dinamik puan hesaplama, mekan filtreleme ve plang/favori yönetiminin istemciye entegrasyonu.

=========================================================