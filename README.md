# README

## Audio Ingestion Endpoints
### Query App
**Query DB for match**
- Choose whether to use statistical or naive matching. Statistical matching used by default
```shell
curl -X POST http://localhost:8080/roshzam/query-song \
     -F "statistical=true" \
     -F "file=@snippets/FLG_snippet_1.mp3"
```
### Ingest MP3 file
**Ingest file from filepath**
```shell
curl -X POST http://localhost:8080/roshzam/upload-song \
     -F file=@"/path/to/file.mp3"
```
Example:
```shell
curl -X POST http://localhost:8080/roshzam/upload-song \
     -F file=@"11 - Human.mp3"
```
**Check entries in DB**
- Check that hashes are being made and saved successfully
```shell
curl "http://localhost:8080/roshzam/test-db"
```

## Test Endpoints
### Greeting
- Healthcheck endpoint
```shell
 curl "http://localhost:8080/greeting?name=User"  
```
### Database test
- Test adding and retrieving entries to JPA springboot H2 database

**Add entries**
```shell
curl -X POST http://localhost:8080/addTestDbEntry \
    -H "Content-Type: application/json" \
    -d '{"inputValue": "my test value"}'
```
**Retrieve all entries**
```shell
curl "http://localhost:8080/getTestDbEntry"
```