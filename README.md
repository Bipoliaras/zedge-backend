# zedge-backend
Technical task for Zedge

Main features
Created Artist API for searching artists, saving favorite artists and getting favorite albums

Artist API

Method  | Path | Explanation
------------- | ------------- | ------------- |
GET  | /albums?userId={id} | Get favorite albums for artist with userId  |
POST  | /artists/search/{name} | Search artists by name |
POST  | /artists?userId={id} | Save favorite artist for user with userId |

## Configuration

| Property | Description | Required | Example (or default if not required) |
| ----------- | ----------- | -------- | ------- |
zedge.backend.artist.cache.expiry.time.hours | Artist search cache expiry time in hours | no | 1
zedge.backend.maximum.requests | Maximum requests which can be sent by the application | no | 100
zedge.backend.album.updater.initial.delay | Initial delay of album searcher in milliseconds | no | 60000
zedge.backend.album.updater.fixed.delay | Fixed delay of album searcher in milliseconds | no | 3600000

# Required dependencies to run the project

* Maven (download from https://maven.apache.org/download.cgi)

* JDK 11 (download from https://adoptopenjdk.net/?variant=openjdk11)

## Steps to run

* Navigate to the root of the project

* Run "mvn clean install"

* Run "java -jar target/zedge-backend-0.0.1-SNAPSHOT.jar"

* API will become available on http://localhost:8080/

* API Swagger docs are available at http://localhost:8080/swagger-ui.html

* H2 database panel is available at http://localhost:8080/h2-console/

   * jdbc url - jdbc:h2:mem:zedge
   * username - zedge
   * password - backend
   
## Initial data

Users with ids 1 and 2 are created when the application is started

## Example requests

* Search artists `curl --location --request POST "localhost:8080/artists/search/big"`

* Get albums `curl --location --request GET "localhost:8080/artists/albums?userId=1"`
