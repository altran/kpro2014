Rest Test App
=============

Prerequisite:
------------

* java 1.7 
* maven3

Install:
--------
in the project directory write: mvn clean install

Run:
----
in the project directory write: mvn spring-boot:run

How to test it:
---------------
* Run the Main.java class from the test directory
* Or install a Chrome extension like Postman and execute the following requests:

1. POST http://localhost:8080/api/sensor/create
```{"id":1,"name":"temperature","date":1412201489904,"value":"10"}```

2. GET http://localhost:8080/api/sensor/1 
3. GET http://localhost:8080/api/sensors 
4. PUT http://localhost:8080/api/sensor/delete/1 