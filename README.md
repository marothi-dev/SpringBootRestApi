Apart from IDE, you can also run this app using following approaches.

    java -jar path-to-jar
    on Project root , mvn spring-boot:run
    
Open POSTMAN tool, select request type [GET for this usecase], 
specify the uri http://localhost:8080/SpringBootRestApi/api/users/ and Send., should retrieve all users.

Use APi Specified in RestApiController
to perform other actons

Alternatively you can run  mvn spring-boot:run
and execute SpringBootRestTestClient

