# README
See [Project Status](Docs/CurrentStatus.md) and [Learnings](Docs/Learnings.md)
# Development
## Local Development
For local development, run the project in your IDE as normal. Ensure you have ffmpeg installed and have set the `export FFMPEG_PATH=/usr/local/bin/ffmpeg` env var.
## Managing containerised application
### Build and run
**Build Jar file**
```shell
mvn clean install # for fat jar with dependencies
```
**Build docker image**
```shell
docker build -t roshzam:latest .
# Check image is built
docker images
```
**Run container process**

You [must bind mount the directory](https://docs.docker.com/engine/storage/bind-mounts/#:~:text=If%20you%20use%20%2D%2Dvolume,not%20exist%20on%20the%20host.) containing the sample audio files for ingestion using the absolute path using the `--volume` or `-v` flag during run time. 
```shell
# Add the -d option to detatch the terminal - means no logs though
docker run -p 8080:8080 -v /Users/roshans/src/roshzam/samples:/samples roshzam:latest
```
Stop container with `docker stop roshzam`
### Remote JVM Debug
To debug containers remotely run:
```shell
docker run -p 8080:8080 -p 5005:5005 \
 -v /Users/roshans/src/roshzam/samples:/samples \
  -e JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005" \
   roshzam:latest
```
Then start the remote debugger on port 5005.

To run inspect within the container using command line run:
```shell
docker ps
# Grab the container ID
docker exec <container-ID> -it sh
/ \# <run-as-if-on-command-line>
```

# Endpoints
## Audio Ingestion Endpoints
### Query App
**Query DB for audio match**
- Choose whether to use statistical or naive matching. Statistical matching used by default
```shell
curl -X POST http://localhost:8080/roshzam/query-song \
     -F "statistical=true" \
     -F "file=@snippets/FLG_snippet_1.mp3"
```
**Query DB for filename hashes**
```shell
curl "http://localhost:8080/roshzam/query-db?filename='02_-_Future_Looks_Good.mp3'"
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