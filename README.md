<h1 align="center" style="font-weight: bold;">Easy-ecommerce 💻</h1>

<p align="center">
 <a href="#tech">Technologies</a> • 
 <a href="#started">Getting Started</a> • 
  <a href="#routes">API Endpoints</a> •
 <a href="#colab">Collaborators</a> •
 <a href="#contribute">Contribute</a>
</p>

<p align="center">
    <b>Backend API for eCommerce management with users, products, categories, orders and payments.</b>
</p>

<h2 id="technologies">💻 Technologies</h2>

- Java 17
- PostgreSQL 8
- Spring Framework  3.3.2
- Spring Web 
- Spring Data JPA
- Docker 27.0.3
- Swagger 2.5.0
- Apache Maven 3.3.2
- Spring Validation I/O
- GIT 2.34.1

<h2 id="started">🚀 Getting started</h2>

This section guides you through running the project locally.

<h3>Pre-requisites</h3>

Before you begin, ensure you have the following software installed:

* Java Development Kit (JDK) -  https://www.oracle.com/java/technologies/downloads/
* Maven - https://maven.apache.org/download.cgi
* Docker - https://www.docker.com/
* PostgreSQL - https://www.postgresql.org/download/

**Optional:**
* IDE (Integrated Development Environment) - (e.g., IntelliJ IDEA, Eclipse)

<h3>Running the Project</h3>

1.  **Clone the Repository:**
```
git clone git@github.com:RayanSf18/Easy-ecommerce.git
```
2. **Navigate to the Project Directory:**
```
cd easy-ecommerce
```
3. **Run Postgres Docker:**
```
cd easy-ecommerce/local
run: docker-compose up -d
```
4. **Start the Application:**
```
cd easy-ecommerce
mvn spring-boot:run
```
<h2 id="routes">📍 API Endpoints</h2>

| route               | description                                          
|----------------------|-----------------------------------------------------
| <kbd>POST /users</kbd>     | create a new user [request details](#post-user-create)
| <kbd>GET /users/{userId}</kbd>     | search for a specific user [response details](#get-user-detail)
| <kbd>GET /users</kbd>     | search all users [response details](#get-users-details)
| <kbd>PUT /users/{userId}</kbd>     | update data for a specific user [request details](#put-user)
| <kbd>DELETE /users/{userId}</kbd>     | delete a specific use [request details](#delete-user)
| <kbd>POST /products</kbd>     | create a new product [request details](#post-product-create)
| <kbd>GET /products/{productId}</kbd>     | search for a specific product [response details](#get-product-detail)
| <kbd>GET /products</kbd>     | search all users [response details](#get-users-details)


<h3 id="post-user-create">POST /users</h3>

**REQUEST**
```json
{
  "name":"Rayan silva",
  "email": "rayan.dev@gmail.com",
  "phone": "(99) 9 9999-9999",
  "password": "dev.java@"
}
```
**RESPONSE**
```json
{
  "user_id": 1
}
```

<h3 id="get-user-detail">GET /users/{userId}</h3>

**RESPONSE**
```json
{
  "user_id": 1,
  "name": "Rayan silva",
  "email": "rayan.dev@gmail.com",
  "phone": "(99) 9 9999-9999",
  "password": "dev.java@"
}
```

<h3 id="get-users-details">GET /users</h3>

**RESPONSE**
```json
{
  "user_id": 1,
  "name": "Rayan silva",
  "email": "rayan.dev@gmail.com",
  "phone": "(99) 9 9999-9999",
  "password": "dev.java@"
}
...
 ...
```
<h3 id="put-user">PUT /users/{userId}</h3>

**REQUEST**
```json
{
  "name": "Rayan silva Ferreira",
  "phone": "(55) 5 5555-4444",
  "password":  "desenvolvedor.java.backend"
}
```
**RESPONSE**
```json
{
  "user_id": 1
}
```
<h3 id="delete-user">DELETE /users/{userId}</h3>

**REQUEST**
```json
http://localhost:8080/users/userId
```

<h3 id="post-product-create">POST /products</h3>

**REQUEST**
```json
{
  "name": "Kingston A2000 SSD 10000",
  "brand": "Kingston",
  "model": "A2000",
  "description": "SSD Kingston A2000 NVMe PCIe, 250GB, até 2000MB/s de leitura.",
  "price": 250.00,
  "imgUrl": "http://example.com/img/kingston_a2000.jpg",
  "categories": [
    "Computers",
    "Storage",
    "Components"
  ]
}

```
**RESPONSE**
```json
{
  "product_id": 1
}
```
<h3 id="get-product-detail">GET /products/{productId}</h3>

**RESPONSE**
```json
{
  "productId": 1,
  "name": "Kingston A2000 SSD 10000",
  "brand": "Kingston",
  "model": "A2000",
  "description": "SSD Kingston A2000 NVMe PCIe, 250GB, até 2000MB/s de leitura.",
  "price": 250,
  "imgUrl": "http://example.com/img/kingston_a2000.jpg",
  "categories": [
    {
      "id": 1,
      "name": "Computers"
    },
    {
      "id": 2,
      "name": "Components"
    },
    {
      "id": 3,
      "name": "Storage"
    }
  ]
}
```
