# REST API Aggregator — ISS Observation Tracker

A Java-based REST API aggregator that collects and analyzes data from multiple external services to determine the best conditions for observing the International Space Station (ISS).

The project demonstrates integration with third-party REST APIs, JSON processing, configurable API clients and data aggregation architecture.

## Overview

The application allows users to specify their location and receive information about upcoming ISS passes:

- when the ISS will appear above the observer;
- duration and visibility parameters of the pass;
- weather conditions during the observation window;
- twilight information to estimate sky visibility.

The final observation report combines data from several independent APIs into a single analytical result.

## Features

- REST API integration with multiple external services;
- configurable API clients through JSON configuration;
- automatic request building;
- JSON parsing and DTO mapping;
- aggregation of heterogeneous API responses;
- ISS visibility analysis;
- export of results to JSON and CSV formats.

## Integrated APIs

### ISS Tracking

Provides satellite pass predictions:

- next ISS passes;
- start and end time;
- maximum elevation angle;
- observation direction;
- pass duration;
- estimated brightness.

Source:

- N2YO API

### Weather Data

Provides current weather forecast:

- temperature;
- cloud coverage;
- weather description;
- wind speed.

Source:

- OpenWeather API

### Astronomical Data

Provides twilight information:

- civil twilight;
- astronomical twilight;
- sunrise and sunset data.

Source:

- Sunrise-Sunset API

## Architecture

The project uses a modular architecture:

```
                +----------------+
                |  config.json   |
                +-------+--------+
                        |
                        v
              +-------------------+
              | ApiClientFactory  |
              +---------+---------+
                        |
        +---------------+---------------+
        |               |               |
        v               v               v

   N2YO Client   Weather Client   Twilight Client

        \              |              /
         \             |             /
          v            v            v

          +-----------------------+
          |   API Aggregator      |
          +-----------+-----------+
                      |
                      v

          +-----------------------+
          | Observation Analyzer  |
          +-----------+-----------+
                      |
                      v

          JSON / CSV Report
```

## Technologies

- Java 17+
- Gradle
- OkHttp — HTTP client
- Jackson — JSON serialization/deserialization
- JSON Flattener — CSV export
- REST API integration

## Project Structure

```
src/main/java/com/alex

├── data
│   ├── DTO
│   ├── common
│   ├── info
│   └── response
│
├── parser
│
├── service
│   ├── api
│   └── httpProvider
│
├── logic
│
└── writer
```

## Configuration

Before running the application:

1. Copy example configuration:

```bash
cp src/main/resources/config.example.json src/main/resources/config.json

API sources are configured through:

```
src/main/resources/config.json
```

Example:

```json
{
  "name": "n2yo",
  "baseUrl": "https://api.n2yo.com/rest/v1/satellite/visualpasses",
  "extra_path": [
    25544,
    60.007194,
    30.381778
  ]
}
```

Configuration contains:

- API endpoint;
- request parameters;
- location coordinates;
- additional path parameters.

## Running

Clone repository:

```bash
git clone https://github.com/<username>/RESTApiAgregator.git
```

Build project:

```bash
./gradlew build
```

Run:

```bash
./gradlew run
```

## Example Output

Example observation report:

```json
{
  "pass": {
    "startLocal": "22:15:30",
    "duration": 420,
    "maxEl": 65.4,
    "direction": "NW→SE"
  },
  "weather": {
    "clouds": 20,
    "temp": 14.5,
    "windSpeed": 3.2
  },
  "twilight": {
    "civilEndLocal": "21:40:00"
  },
  "verdict": "Good conditions for ISS observation"
}
```

## Possible Improvements

Future development ideas:

- web interface with interactive map;
- user accounts and saved locations;
- notifications before ISS passes;
- caching API responses;
- database storage;
- Docker deployment;
- scheduled background updates.

## Purpose

The project was created as a practical example of working with REST APIs and demonstrates how data from independent external services can be combined into a single analytical application.