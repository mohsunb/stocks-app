# stocks-app
## A stocks back-end app with login and purchase functionality, written in Java with Spring Boot framework.

### [Coding challenge for internship at ExpressBank](https://drive.google.com/file/d/1G8OTfZFHv08KViywMyx7zm4k6QRb3oLT/view?usp=share_link)

## Dependencies:
* Spring Web
* Spring Data JPA
* Spring Security
* Validation
* Lombok
* MariaDB Driver
* Java Mail Sender
* ```io.jsonwebtoken:``` & ```jjwt-api:0.11.5``` + ```jjwt-impl:0.11.5``` + ```jjwt-jackson:0.11.5```
* H2 Database
* Liquibase

## SQL Database used: MariaDB
Database name: ```stocks_app_db```;

Tables:
* users(id, name, surname, username, password, balance, role, enabled);
* confirmation_tokens(id, username, token, creation_date);
* stocks_available(id, name, price, available_count);
* stocks_purchased(id, owner_id, item_id, count);

## Endpoints: (Postman can be used to interact with them)
* ```/api/v1/auth/register```: (GET & POST) Sign up with a new user account. ```GET``` request includes a simple front-end and email verification. No authorization necessary.
* ```/api/v1/auth/auth```: (POST) Login to a verified account. Unverified accounts cannot be logged into. Returns Json Web Token as a String. No authorization necessary.
* ```/stocks/market/list-all```: (GET) Displays information about currently available stocks to purchase. No authorization necessary.
* ```/api/v1/user/current```: (GET) Displays information of the currently logged in user. Authorization with Bearer Token is necessary.
* ```/stocks/owned/list```: (GET) Displays information about stocks purchased by the currently logged in user. Authorization with Bearer Token is necessary.
* ```/api/v1/user/current/deposit```: (POST) Deposits money to the balance of the currently logged in user. Amount must be a positive value. Authorization with Bearer Token is necessary.
* ```/stocks/market/purchase```: (POST) Purchase stocks. Request body: "name" and "count". Sends an email to the user upon successful transactions. Authorization with Bearer Token is necessary.
* ```/stocks/owned/sell```: (POST) Sell currently owned stocks, only one stock variant at a time. Sends an email to the user upon successful transactions. Authorization with Bearer Token is necessary.
