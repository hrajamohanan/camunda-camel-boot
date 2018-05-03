# camunda-camel-boot
camunda-camel-boot
Forked from larbigj/camunda-camel-boot


This project can start Camunda process as a Spring Boot application. 

The project has included all the  dependencies which is required to call a Camel route in the project.

Changes has been made to connect to the MySQL DB now as this will make it more production ready if you want to use this project for 
real project.

Make changes to application.properties and the following properties will need modification

<code>
  
  spring.datasource.url=jdbc:mysql://localhost:3306/camunda
  
  spring.datasource.username=camunda
  
  spring.datasource.password=camunda
  
  camunda.bpm.database.type=mysql


</code>
