# ![](https://fonts.gstatic.com/s/i/materialiconsoutlined/bug_report/v12/24px.svg) kkazm/bugtracker

A bug tracker with a web-based user interface, similar to Jira and Bugzilla. Built with Spring Boot. This repository is
the REST API component.

The corresponding web frontend source code is [available here](https://github.com/kkazm/bugtracker-ui).

**Demo available online at <https://kkazm.ovh/bugtracker>**

Swagger UI available at <http://kkazm.ovh/bugtracker/swagger-ui/index.html>

## Building and running from source

Clone the repository and run the following command (JDK 17+ required):

    ./gradlew bootRun

By default, the application listens on all interfaces and on port 8080.

To run all tests, execute the following command:

    ajsodifajio

## Database

By default, the application uses the H2 in-memory database. All data is lost after application shutdown.

When the application is running, the database can be accessed externally using the following connection properties:

    url: jdbc:h2:mem:mydb
    driver-class-name: org.h2.Driver
    username: sa
    password:

### Database schema

## Extra

Example http request file in