# README

## Test Endpoints
### Greeting
- Healthcheck endpoint
```java
 curl "http://localhost:8080/greeting?name=User"  
```
### Database test
- Test adding and retrieving entries to JPA springboot H2 database

**Add entries**
```java
curl -X POST http://localhost:8080/addTestDbEntry \
    -H "Content-Type: application/json" \
    -d '{"inputValue": "my test value"}'
```
**Retrieve all entries**
```java
curl "http://localhost:8080/getTestDbEntry"
```