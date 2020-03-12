[![Build Status](https://travis-ci.org/MsgVis/microsmartgrid.svg?branch=master)](https://travis-ci.org/MsgVis/microsmartgrid)
[![codecov](https://codecov.io/gh/MsgVis/microsmartgrid/branch/master/graph/badge.svg)](https://codecov.io/gh/MsgVis/microsmartgrid)
![WTFPL License](https://img.shields.io/badge/License-WTFPL-green "This work is licensed under the WTFPL")
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FMsgVis%2Fmicrosmartgrid.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FMsgVis%2Fmicrosmartgrid?ref=badge_shield)

Version support:

![Java Version](https://img.shields.io/badge/Java%20Version-11-blue)
![Spring Cloud Version](https://img.shields.io/badge/Spring%20Cloud%20Version-Hoxton.SR1-blue)
![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot%20Version-2.2.2.RELEASE-blue)
![Docker Compose Version](https://img.shields.io/badge/Docker%20Compose%20Version-2.4-blue)
![Tomcat Version](https://img.shields.io/badge/Tomcat%20Version-8-blue)


# Micro Smart Grid Visualisation Tool

__See how Energy flows and calculate Insights.__

This *alpha-stage* application enables admins of microsmartgrid architectures to easily deploy a distributed supervision tool. Currently supported protocol: MQTT

<div id="TOC">

### TABLE OF CONTENTS

1. [Building, Installing, Using and Uninstalling Msg-Vis](#1)
    1. [Building and Installing](#1-1)
    2. [Using](#1-2)
    3. [Services and Endpoints](#1-3)
    4. [Uninstalling](#1-4)
    5. [Deploying](#1-5)
    6. [Database Structure](#1-6)

<div id="1">

## 1 Building, Installing, Using and Uninstalling Msg-Vis

You need a distribution of Java 11 (e.g. OpenJDK11), [Maven](https://maven.apache.org/), Docker (for Windows and Mac you can get your desktop client [here](https://www.docker.com/products/docker-desktop)) and [Docker Compose](https://docs.docker.com/compose/install/). If you plan to debug the system or setup your own database outside of the docker environment, you need [TimescaleDB v1.6](https://docs.timescale.com/v1.6/main) which must be configured according to this [shell script](https://github.com/MsgVis/microsmartgrid/blob/master/timescaledb/init_timescaledb.sh). Then `clone` this repository.

[Top](#TOC)
<div id="1-1">

### Building and Installing
Once you installed all necessary tools and cloned this repository, navigate to the base folder and execute `mvn clean build install -DskipTests`.

[Top](#TOC)
<div id="1-2">

### Using
There are different ways to use Msg-Vis. You can run all services together with docker or you can execute each service individually, either with docker or via maven.  

To **run all services with docker**, simply build the project and execute `docker-compose up --build` in the base directory. All the work will be done for you.  

To **run individual services with maven**, navigate to the service's directory and execute `mvn spring-boot:run`.  

To **run individual services with docker**, first navigate into the service's directory, build the image with `docker build -t <serviceTag> .` and then run it with `docker run -p <ext-port>:<int-port> <serviceTag>`.

[Top](#TOC)
<div id="1-3">

### Services and Endpoints

All services which other services depend on are exposing spring actuator endpoints (e.g. actuator/health) by default (see [docs](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints)).  
Listed dependencies are transitive.

#### Config Server

Provides all necessary configuration for each service. Has to **always** start as first service and gets called by each service on startup.  

**Port**: 8888

**Relies on**: None

**Endpoints:** None

#### Eureka Server

Service discovery. Provides a graphical overview page on `/`.

**Port**: 8761

**Relies on**: Config Server

**Endpoints:** None

#### Timescale DB Reader

Reads and aggregates data from the database.

**Port**: 4720

**Relies on**: Eureka Server, Timescale DB

**Endpoints:**  
| Method | Path | Variables | Description |
------------- | ------------------------- | ------- | ------------- |
GET| /latest | DTF* Cutoff| Queries the database for the last reading within \<cutoff\> days from each device.
GET| /readings |int id, DTF since, DTF until| Queries the database for all readings from one device within an interval.
GET| /readings/avg | int id, DTF since, DTF until, int step| Queries the database for all readings from one device within an interval and the average is returned.
GET| /readings/min | see above| Queries the database for all readings from one device within an interval and the minimum is returned.
GET| /readings/max | see above | Queries the database for all readings from one device within an interval and the maximum is returned.
GET| /readings/std | see above | Queries the database for all readings from one device within an interval. Readings will be aggregated over 'step' and the standard deviation is returned.
GET| /deviceList |-| Queries a list of all registered devices.
GET| /deviceById |int id| Query devices table by id.
GET| /deviceByName | String name | Queries devices table by name.  

*DTF refers to datetime-format

#### Timescale DB Writer

Writes data to the database.

**Port**: 4721

**Relies on**: Eureka Server, Timescale DB

**Endpoints:**  
| Method | Path | Variables | Description |
------------- | ------------------------- | ------- | ------------- |
PUT| /device | Deviceinformation** deviceInfo| Save or update DeviceInformation to a Device.
POST| /reading |String name, String json| Deserialize the json to a java object and assign it to a DeviceInformation object. Save the created object to the database.  

** see [database model](https://github.com/MsgVis/microsmartgrid/blob/master/database/src/main/java/com/microsmartgrid/database/model/DeviceInformation.java)

#### View

Graphical Frontend. Written mainly with [D3](https://d3js.org). The topology can be passed as a json (see [here](https://github.com/MsgVis/microsmartgrid/tree/master/view/src/main/resources/static/json)) and has to match the database.

**Port**: 8080

**Relies on**: Timescale DB Reader

**Endpoints:** None

#### MQTT Client

Is able to subscribe to MQTT brokers. Config must be done in-code.

**Port**: 1883

**Relies on**: Timescale DB Writer

**Endpoints:** None

[Top](#TOC)
<div id="1-4">

### Uninstalling

To uninstall Msg-Vis and all its dependencies you should delete the whole base directory, then clean your .m2 and docker images/containers. Finally, you should uninstall all services named in the [beginning](#1).

[Top](#TOC)
<div id="1-5">

### Deployment

To deploy Msg-Vis via docker to **docker swarm** or **kubernetes** just follow the usual steps for deploying a spring cloud application with Eureka. Remember to change the host of all services from localhost to dynamic, auto-detectable docker-compose network-addresses (see [docs](https://docs.docker.com/compose/networking/)) and to set the ports to 0 to allow dynamic port allocation.  

To deploy **single Msg-Vis services** to **single tomcat instances**, change `<packaging>jar</packaging>` to `<packaging>war</packagin>` in every pom.xml file which applies. Additionally, navigate into the `<ServiceName>Application.java` usually located at `<servicename>/src/main/java/com/microsmartgrid/<servicename>` and extend the main class with `extends SpringBootServletInitializer`. Then clean-build the project and drop the resulting .war-file into your tomcat's webapps folder.

To deploy **all Msg-Vis services** to **one single tomcat instance**, refer to this [dedicated branch](https://github.com/MsgVis/microsmartgrid/tree/_single_tomcat_deployment) and execute the `install.sh` as `root`. It will clean-build the project and auto-configure your tomcat instance. Be sure to have an appropriate instance of timescaleDb already running. The build depends on it.  
Note however, this does only work on an Ubuntu distribution.

[Top](#TOC)
<div id="1-6">

### Database structure

There are two tables: devices and readings.

Intuitively devices stores all known devices. The ids should match the ids in the topology JSON file so the view service can query the database correctly.

Readings stores all readings. The device id matches the id in devices.

[Top](#TOC)
