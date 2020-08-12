# contactapi-ws
Contact Application Web Service

This is a basic CRUD application for handling contacts.

Tech Stacks
------------------
1. Java 8
2. Springboot 2.x
3. JPA/Hibernate
4. H2 DB
5. Maven 3.x

DB Script
-------------------
Script and Entity diagram is available 'src/main/resources/db/' folder. 

Running the script is optional but if you want initial data, you can run the insert statements of the script after the application is running for the first time.
1. Open Browser
2. Enter http://localhost:8080/api/v1/h2-console
3. Enter 'jdbc:h2:mem:contact_db' (without quote) to JDBC URL field
4. Username is 'sa' (without quote)
5. Leave password blank
6. Click Connect button
7. Copy and paste the insert statements into the textarea
8. Click Run button

Running the application
-------------------
* Before running the applicaton make sure you have setup your environment correctly.

1. Open terminal and navigate to 'contactapi-ws' directory
2. Type command: mvn spring-boot:run
