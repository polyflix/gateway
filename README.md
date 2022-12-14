# Polyflix API Gateway

The API gateway sits between our client and our microservices.

The API gateway acts as a reverse proxy to accept all API calls,
it can aggregate the various services required to fulfill them,
and return the appropriate result.

For now, the Polyflix API Gateway acts as a path through with a versioning gesture.

## Installation
You will need
* Java 11 or later
* Maven 3.2+

Clone the project: 
```shell
git clone git@gitlab.polytech.umontpellier.fr:polyflix-do/gateway.git
```

To run the application go to the root of the project and run:
```shell
./mvnw spring-boot:run
```
or use your favorite IDE to run the project.


## Configuration

You will find the `application.yml` configuration file in the `src/main/resources` path.

This file will allow you to configure all the **uri** for the different microservices.

`application.yml`
```yml
polyflix:
  services:
    legacy: "http://localhost:5000/"
    video: "http://localhost:5002/"
    ...
```
Launch your service locally and update this configuration file with the right `uri`
then launch the API Gateway.

The API Gateway host is also configured in the `application.yml` file:

```yml
server:
  port: 4000
```

To access your service through the Gateway you have to add the `/api/` prefix
to the path.

example:

| Service              |         Gateway         | 
|----------------------|:-----------------------:|
| /v2.0.0/video/health | api/v2.0.0/video/health |


## Authorization

The Gateway authorization management is very simple for now.

The authentification is still managed by the backend legacy
that is now accessed by the gateway.

Once you are authenticated you have to pass a JWT in the requests header.

```json
{
  "Authorization": "Bearer eyJhbGc...",
  ...
}
```

The gateway will try to get information about the user related
with the JWT and add them to the header before forwarding the request.

```json
{
  "Authorization": "Bearer eyJhbGc...",
  // Id of the user
  "X-User-Id": "...",
  // roles of the user
  "X-User-Roles": "..."
}
```
