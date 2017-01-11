## Using MySQL in Spring Boot via Spring Data JPA and Hibernate

This is a kick-starter project for demonstrating how one can develop applications easly using the latest technologies, namely, Spring boot, angularJs, web resource optimizer for java (wro4j), Spring security, Spring data JPA, Hibernate and testing tools like Jasmine, karma, JUnit, mockito, etc.

This project will be growing to include even more frameworks and technologies on the go.


### Build and run

#### Configurations

Open the `application.properties` file and set your own configurations.

#### Prerequisites

- Java 8
- Maven > 3.0

#### From terminal

Go on the project's root folder, then type:

    $ mvn spring-boot:run

#### From Eclipse (Spring Tool Suite)

Import as *Existing Maven Project* and run it as *Spring Boot App*.


### Usage

- Run the application and go on http://localhost:8081/. I have set that as my default 

### Login Credentials,
 - The application configured to use a combination of inMemoryAuthentication and userDetailsService implementation. The following credentialities can be used to login if no database is created.
 - login with User role: Username: testuser and password: testuser
 - Login with Admin role: Username: testadmin and password: testadmin
 
