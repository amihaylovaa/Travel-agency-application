**Lux travel agency** is a Restful application powered by Spring boot.
Its main purpose is to present all holidays and excursions that the travel agency provides.

The applications is consisting of :

* Domain layer - contains all basic entities without which the application could not exist

* Persistence layer - contains all database operation

* Service layer - contains all the business logic with large number of validations

There are controllers that handles POST, GET, PUT and DELETE HTTP methods.

Examples for handling rest API :

## Creating user 

End point - /users/register

Request body :

````
{
  "username":"SomeUsername",
  "email":"SomeEmail@gmail.bg",
  "password":"SomePassword"
}
````
Response :
````
{
    "id": 5,
    "username": "SomeUsername",
    "email": "SomeEmail@gmail.bg",
    "password": "$2a$10$BXiSC32pmXTxnkhLwWGiU./pP8Z7Rqmf3rPeJ1TYwVe/3tVzl07uW"
}
````
## Finding transport by existing id

End point - `/transports/id`
````
Response
{
    "@type": "Bus",
    "id": 2,
    "transportClass": "FIRST"
}
````
## Removing booking that does not exist 

End point - `/bookings/id`

````
{
    "status": 500,
    "error": "Internal Server Error",
    "message": "This booking does not exist",
    "path": "/bookings/3"
}
````





**Application's data is stored in relational database management system MySQL.
