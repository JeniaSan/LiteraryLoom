# Online-Book-Store
This application is a bookstore that allows you to browse books, add to cart, issue and track the status of the order
## Table of Contents
- [Intro](#introduction)
- [Tech stack](#technologies-used)
- [Controllers](#api-functionalities)
- [Required downloads](#a-list-of-what-needs-to-be-installed-to-run-this-application)
- [Start app](#how-to-start-this-app)
- [Video presentation](#video-presentation)
## Introduction
This application is a spring boot application that was created so that the user can conveniently find the books he needs according to the necessary parameters (author, price, title, category).
This project includes many different technologies for fast and reliable operation of the application.
Next, we will familiarize ourselves with each part of this application in more detail.
## Technologies used
This application includes the following list of technologies:
- Spring Data JPA: Ensuring streamlined and effective data retrieval and persistence.
- Spring Security: Enhancing application security and safeguarding user data.
- MapStruct: Simplifying and optimizing the process of mapping between DTOs and entities.
- Docker: Facilitating containerization for simplified deployment and scalability.
- OpenAPI: Enabling comprehensive documentation of our RESTful APIs, offering explicit and in-depth insights into available resources, request structures, and response formats.
- Liquibase: Employed for the management and versioning of database schemas, simplifying schema modifications and ensuring uniform data structure across diverse environments.
- Swagger: Seamlessly navigate and engage with our API using the user-friendly Swagger documentation.
    For a more comprehensive overview of the functions and capabilities of these controllers, please refer to the "Features" section for an in-depth understanding.
## API Functionalities
Within our Online Book Store project, we have meticulously adhered to REST (Representational State Transfer) principles when architecting our controllers. These controllers serve as pivotal components responsible for managing HTTP requests and responses.
- AuthenticationController: This controller manages requests for user registration and login, which can be executed through either email and password or a JWT token.
- BookController: This controller is dedicated to receiving and managing requests related to adding, updating, retrieving, and searching for books.
- CategoryController: Here, you can find the handling of requests for adding, updating, and retrieving categories, as well as fetching all books within a specific category.
- ShoppingCartController: Within this controller, you can request actions such as adding, deleting, and updating books within the shopping cart, as well as retrieving the user's shopping cart.
- OrderController: This controller is responsible for handling requests related to creating and retrieving orders, as well as updating the status of orders.
## Controllers functionalities
- ### AuthenticationController (/auth):
1) POST /register - Register user and save to DB;
2) POST /login - Login user and return JWT token;
- ### BookController (/books):
1) GET - Get a list of all available books;
2) GET /{id} - Get book by id;
3) POST - Create a new book and insert into database;
4) DELETE /{id} - Delete book by id;
5) PUT /{id} - Update a book to the request values;
6) GET /search - Search books by params. All params : author, isbn, price, title;
- ### CategoryController (/categories):
1) POST - Create a new category;
2) GET - Get a list of all available categories;
3) GET /{id} - Get a category by id;
4) PUT /{id} - Update a category;
5) DELETE /{id} - Delete a category by id;
6) GET /{id}/books - Get a list of all available books by category id;
- ### ShoppingCartController (/cart):
1) GET - Get a shopping cart with all cart items;
2) POST - Add a new cart item into shopping cart;
3) PUT - /cart-items/{cartItemId} - Update a cart item in shopping cart;
4) DELETE - /cart-items/{cartItemId} - Delete cart item in shopping cart
- ### OrderController (/orders):
1) GET - Get orders history;
2) POST - Create an order and clear shopping cart;
3) GET - /{orderId}/items - Get a list of all items from order;
4) GET - /{orderId}/items/{itemId} - Get an order item from order;
5) PATCH - /{id} - Change order status;
## A list of what needs to be installed to run this application:
- Java 17 (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- PostgreSQL (https://www.postgresql.org/download/)
- Docker (https://www.docker.com/)
- Maven (https://maven.apache.org/download.cgi)
## How to start this app:
Download git repository by git command:
 ```bash
 git clone https://github.com/JeniaSan/Online-Book-Store.git
 ```
Build a project using **Maven**:
 ```bash
 mvn clean install
 ```
Then, rise a **Docker** container of your app:
 ```bash
 docker build -t {imageʼs name or hash code}
 docker-compose build
 docker-compose up
 ```
Also, you can run this project without docker, but before that, you need to configure the connection to your local database in the application properties. Run this command after that:
```bash
 mvn spring-boot:run
```
## Video presentation
This video shows the operation of the application in detail. Here we will see how the search for books and categories works, the ability of the admin to change them, and how the shopping cart and orders work.
**Link** : https://www.loom.com/share/0d0a39e23f1b44d790b2efebb256edc5
if you have any questions about using this application, you can contact us: [email](mailto:yevheniipolishuchenko@gmail.com)
