### _**Java Instrumentation Engineer Project**_
***
#### Travis-CI Build Status:
[![Build Status](https://app.travis-ci.com/dougj0220/metrics-service-parent.svg?branch=master)](https://app.travis-ci.com/dougj0220/metrics-service-parent)
***
#### _Project Info_:
Simple metric gathering library with a Servlet Filter implementation that tracks request lifecycle processing times and response payload sizes in bytes. The project provides a metrics API to get min, max, average request times and response sizes in bytes. You can also call an API with a specific requestId to view the metrics of that particular request. The project uses in-memory data store and metrics-service REST API to gather data are report using spring-boot 2.6.4 and JBoss undertow web server.
***
#### _Tech stack_:
- java 11
- Maven
- spring-boot v2.6.4
- undertow
- Apache commons
***
#### _How to run project_:
- have java 11 installed or use [jenv](https://www.jenv.be/) 
- have Maven installed for build tool
- clone https://github.com/dougj0220/metrics-service-parent.git
- from project root run: `mvn clean package`
- start undertow web server with app running on http://localhost:8082 `java -jar metrics-service/target/metrics-service-0.0.1-SNAPSHOT.jar`
- use Postman, curl, httpie and make a GET request to: `http://localhost:8082/api/v1/app/version`
- explore other endpoints