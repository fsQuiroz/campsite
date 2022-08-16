# Campsite

## Introduction
Microservice intended to handle reservations

## Database

This project use MySQL for storing reservations

## Documentation

This project has a Postman Collection in file `Campsite.postman_collection.json`

## Getting started

### Dependencies

This application has been proven to work with:
- Java version 17.0.4 or above
- Maven version 3.8.6 or above
- Docker version 20.10.8 or above

### Build

To generate Docker image of project you can use script `build`. This script is self-sufficient and does not require dependencies
```
bash build
```

### Run from IDE

To run the project within your IDE, you'll need all dependencies running. For this you can use the script `only-dependencies` to start redis and mysql on Docker:
```
bash only-dependencies
```
You'll need this ports to be available:
- `8080` for backend
- `3306` for MySQL
- `9736` for Redis

### Run from Docker

To run the project as a self-sufficient unit you can use the script `run` to start the docker image, expose the application on `http://localhost:8080` and start up all dependencies:
```
bash run
```
You'll need this ports to be available:
- `8080` for backend
- `3306` for MySQL
- `9736` for Redis

### Call Endpoints

The endpoints can be called using the following methods:
- Import to Postman the collection under `Campsite.postman_collection.json`
- Or some other http tool such as cUrl

### Get Available Days

This endpoint will return the available days to arrive and departure given the optional range `from`-`to`

```
curl --location --request GET 'http://localhost:8080/reservations/availability?from=2022-08-01&to=2022-09-30'
```

#### Query Params:
- from [date, optional]: Starting date to check availability. Default value to today. Format ISO-8601 `2022-01-31`
- to [date, optional]: Ending date to check availability. Default value to today plus 31 days. Format ISO-8601 `2022-01-31`

### Create Reservation

This endpoint will create a reservation if all information is valid

```
curl --location --request POST 'http://localhost:8080/reservations' \
--header 'Content-Type: application/json' \
--data-raw '{
    "arrival": "2022-08-19",
    "departure": "2022-08-20",
    "name": "Miss Rolando Langosh",
    "email": "Leilani_OConnell8@yahoo.com"
}'
```

#### Body Params:
- arrival [date, mandatory]: Date of arrival. Format ISO-8601 `2022-01-31`
- departure [date, mandatory]: Date of departure. Format ISO-8601 `2022-01-31`
- name [string, mandatory]: Reservation holder's name
- email [string, mandatory]: Reservation holder's email

### Modify Reservation

This endpoint will update a previously created reservation's arrival and departure

```
curl --location --request PUT 'http://localhost:8080/reservations/10' \
--header 'Content-Type: application/json' \
--data-raw '{
    "arrival": "2022-08-20",
    "departure": "2022-08-22"
}'
```

#### Url Param:
- reservationId [id, mandatory]: Reservation id to me modified

#### Body Params:
- arrival [date, mandatory]: New date of arrival. Format ISO-8601 `2022-01-31`
- departure [date, mandatory]: New date of departure. Format ISO-8601 `2022-01-31`

### Cancel Reservation

This endpoint will cancel a previously created reservation

```
curl --location --request DELETE 'http://localhost:8080/reservations/3'
```

#### Url Param:
- reservationId [id, mandatory]: Reservation id to me cancelled

## Load Testing

The project has a jMeter project configured to perform load testing on Get Available Days and Create Reservation endpoints. To execute:

1. Have Docker Image already built, if not, run `bash build`
2. Have Docker Image and dependencies running, if not, run `bash run`
3. Open jMeter
4. Load into jMeter project `src/main/resources/jMeterHttpLoadTest.jmx`
5. Start test in jMeter
6. You can see al requests on Requests Log of each endpoint Thread Group, or
7. You can see a summary of all executions in All Requests Sumary

This application has been proven to work with:
- Java version 17.0.4
- jMeter version 5.5