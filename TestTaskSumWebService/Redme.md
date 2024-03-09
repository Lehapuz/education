Program compile by "gradle installSumService" command.
Launching by "java -jar sum_service.jar" this file in the directory build\libs after compilation.
For launching database is used docker-compose file.
Database is Postgresql, and name is sum_service.
For creating tables is used liquibase database migration
Application has three endpoints.
If application is launching locally, endpoints will be:
1. Post Request localhost:8080/api/v1/add with two fields name and value:
   "name": "test1",
   "value": 1
Name should be unique, in the case of successful processing, user gets the following response:
   "code": "0",
   "description": "OK"
and the data is added to the database.
if name is not unique, user gets:
   "code": "-1",
   "description": "This name already exist".
2. Post Request localhost:8080/api/v1/remove with field name
   "name": "test1"
in the case of successful processing, user gets the following response:
   "code": "0",
   "description": "OK"
and the data is removed from the database, if name is wrong, user gets:
   "code": "-2",
   "description": "This name is not found".
3. Post Request localhost:8080/api/v1/sum with two fields of different names:
   "first": "test27",
   "second": "test28"
in the case of successful processing, user gets the following response:
   "code": "0",
   "description": "OK",
   "sum": 124,
where sum is calculating result of values. If one of names is wrong, user gets:
   "code": "-2",
   "description": "This name is not found".