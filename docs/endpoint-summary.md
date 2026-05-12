# Endpoint Özeti

## City Endpoints

- GET /api/cities
- GET /api/cities/{id}
- POST /api/cities
- PUT /api/cities/{id}
- DELETE /api/cities/{id}

## Place Endpoints

- GET /api/places
- GET /api/places/{id}
- GET /api/places/search
- GET /api/places/category
- GET /api/places/city/{cityId}
- POST /api/places
- PUT /api/places/{id}
- DELETE /api/places/{id}

## User Endpoints

- POST /api/users
- GET /api/users/{id}
- GET /api/users/email

## Rating Endpoints

- POST /api/ratings
- GET /api/ratings/place/{placeId}

## NoSQL Favorite Endpoints

- POST /api/nosql/favorites
- GET /api/nosql/favorites/user/{userId}

## NoSQL Travel Plan Endpoints

- POST /api/nosql/travel-plans
- GET /api/nosql/travel-plans/user/{userId}

## NoSQL Cache Endpoints

- POST /api/nosql/cache/search
- GET /api/nosql/cache/recent-searches