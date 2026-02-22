# Restaurant Review API

A RESTful backend application for managing restaurants and user reviews.
Users can search for restaurants, upload photos, and create reviews for restaurants.

The application is built with **Spring Boot** and uses **Keycloak (OAuth2 / JWT)** for authentication and authorization.
Restaurant search functionality is powered by **Elasticsearch**, and the entire environment can be started using **Docker Compose**.

---

## Features

* Create, update, delete, and retrieve restaurants
* Search restaurants by:

  * name or keyword
  * minimum rating
  * geographic location (latitude / longitude + radius)
* Create, update, delete, and list reviews for restaurants
* Upload and retrieve restaurant photos
* Pagination and sorting for reviews
* Secure endpoints using **JWT authentication with Keycloak**
* Full-text search using **Elasticsearch**
* Containerized infrastructure using **Docker Compose**

---

## Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Web**
* **Spring Security**
* **OAuth2 Resource Server**
* **Keycloak**
* **Elasticsearch**
* **Kibana**
* **MapStruct**
* **Lombok**
* **Docker & Docker Compose**

---

## Architecture Overview

The project follows a layered architecture:

Controller → Service → Repository → Data Source

* **Controllers** handle HTTP requests and responses.
* **Services** contain business logic.
* **Mappers (MapStruct)** convert between DTOs and domain objects.
* **DTOs** are used to expose API responses and requests.

---

## Authentication

Authentication is handled using **Keycloak**.

* Users authenticate with Keycloak.
* The backend validates JWT tokens using Spring Security's **OAuth2 Resource Server**.
* Authenticated users can create, update, and delete reviews.

Example secured endpoint:

```
POST /api/restaurants/{restaurantId}/reviews
```

Requires a valid **JWT access token** issued by Keycloak.

---

## Restaurant Search

Restaurants are indexed in **Elasticsearch** to support fast search queries such as:

* keyword search
* rating filters
* location-based filtering

Example request:

```
GET /api/restaurants?q=pizza&minRating=4
```

---

## API Endpoints

### Restaurants

```
POST   /api/restaurants
GET    /api/restaurants
GET    /api/restaurants/{restaurantId}
PUT    /api/restaurants/{restaurantId}
DELETE /api/restaurants/{restaurantId}
```

### Reviews

```
POST   /api/restaurants/{restaurantId}/reviews
GET    /api/restaurants/{restaurantId}/reviews
GET    /api/restaurants/{restaurantId}/reviews/{reviewId}
PUT    /api/restaurants/{restaurantId}/reviews/{reviewId}
DELETE /api/restaurants/{restaurantId}/reviews/{reviewId}
```

### Photos

```
POST   /api/photos
GET    /api/photos/{id}
```

---

## Running the Project

The application uses **Docker Compose** to run supporting services:

* Keycloak
* Elasticsearch
* Kibana

Start the infrastructure:

```
docker-compose up -d
```

Then run the Spring Boot application.

---

## Example Flow

1. User authenticates via Keycloak and receives a JWT token.
2. The token is included in API requests.
3. User can create a review for a restaurant.
4. Reviews are stored and linked to the restaurant.
5. Restaurants can be searched using Elasticsearch.
