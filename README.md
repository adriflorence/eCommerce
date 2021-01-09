# eCommerce Application

The template is written in Java using Spring Boot, Hibernate ORM, and the H2 database.

* `JpaRepository` interfaces for each model that allow Hibernate to connect them with the database so the data can be accessed in the code. 
* `data.sql`: Spring will run this file every time the application starts to populate the database

## Examples
To create a new user  send a POST request to:
http://localhost:8080/api/user/create with an example body like 

```
{
    "username": "test"
}
```


and this would return
```
{
    "id" 1,
    "username": "test"
}
```


## Authentication and Authorization
* Authentication: a combination of usernames and passwords (with hashing)
* Authorization: JSON Web Tokens (JWT)


Once all this is setup, Spring's default /login endpoint can be used to login like so:

```
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

If those are valid credentials, this endpoint should return a 200 OK with an Authorization header ("Bearer < data >").
* If "Bearer" is not present, endpoints should return 401 Unauthorized.
* If it's present and valid, the endpoints should function as normal.

## Testing
Unit tests are implemented, demonstrating at least 80% code coverage.
