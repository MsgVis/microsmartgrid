[![Build Status](https://travis-ci.com/jblossey/microsmartgrid.svg?token=CKXxArV3KpMci1D3TZaC&branch=master)](https://travis-ci.com/jblossey/microsmartgrid)
[![codecov](https://codecov.io/gh/jblossey/microsmartgrid/branch/master/graph/badge.svg?token=Xgpc4DxbYR)](https://codecov.io/gh/jblossey/microsmartgrid)
![WTFPL License](https://img.shields.io/badge/License-WTFPL-green "This work is licensed under the WTFPL")
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fjblossey%2Fmicrosmartgrid.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fjblossey%2Fmicrosmartgrid?ref=badge_shield)

Version support:

![Java Version](https://img.shields.io/badge/Java%20Version-11-blue)
![Spring Cloud Version](https://img.shields.io/badge/Spring%20Cloud%20Version-Hoxton.SR1-blue)
![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot%20Version-2.2.2.RELEASE-blue)
![Docker Compose Version](https://img.shields.io/badge/Docker%20Compose%20Version-2.4-blue)
![Tomcat Version](https://img.shields.io/badge/Tomcat%20Version-8-blue)


# Micro Smart Grid Visualisation Tool

__See how Energy flows and calculate Insights.__

This *alpha-stage* application enables admins of microsmartgrid architectures to easily deploy a distributed supervision tool.

<div id="TOC">

### TABLE OF CONTENTS

1. [Building, Installing, Using and Uninstalling Msg-Vis](#1)
    1. [Build, Install, Use and Uninstall Msg-Vis on a UNIX-like system](#2-1)
    2. [Build Options](#2-1)
    3. [Dependencies](#2-2)
    4. [Building Msg-Vis Documentation](#2-3)
    5. [Building Msg-Vis Packages](#2-4)
    6. [Debugging Msg-Vis](#2-5)
2. [Testing Distributed Systems](#3)
    1. [Unit tests](#3-1)
    2. [Intergration tests](#3-2)
3. [Platform Notes](#4)
    1. [Linux](#4-1)
    2. [Microsoft Windows](#4-2)
    3. [Mac](#4-3)

<div id="1">

## 1 Building, Installing, Using and Uninstalling Msg-Vis

You need a distribution of Java 11 (e.g. OpenJDK11), [Maven](https://maven.apache.org/), Docker (for Windows and Mac you can get your desktop client [here](https://www.docker.com/products/docker-desktop)), [Docker Compose](https://docs.docker.com/compose/install/), PostgreSQL and TimescaleDB.

### PostgreSQL

We use PostgeSQL as database system.

Our current implementation has been tested with [PostgreSQL 11](https://www.postgresql.org/docs/11/index.html).

### TimescaleDB

We use the PostgreSQL extension [TimescaleDB](https://www.timescale.com/), a scalable time-series database which uses standard SQL.

With Timescale you turn tables into whats called [Hypertables](https://docs.timescale.com/latest/introduction/architecture) with which you interact just as you would with a regular PostgreSQL table. [Create Hypertable](https://docs.timescale.com/latest/api#create_hypertable).

We also make use of Timescale's [Time Bucket](https://docs.timescale.com/latest/api#time_bucket). Time Buckets are aggregates over arbitrary time intervals.

Our current implementation has been tested with TimescaleDB [v1.5](https://docs.timescale.com/v1.5/main) and [v1.6](https://docs.timescale.com/v1.6/main).

### Setup database

To setup the database have a look at the bash script in the timescaledb folder.

### Data structure

There are two tables: devices and readings.

Intuitively devices stores all known devices. The ids should match the ids in the topology JSON file so the view service can query the database correctly.

Readings stores all readings. The device id matches the id in devices.

[Top](#TOC)

## Micro services

### timescaleDbReader

timescaleDbReader exposes several database queries as REST GET endpoints. These are:

- /latest
- /readings/[avg/min/max/std]
- /readings
- /deviceList
- /deviceBy[Id/Name]

For further information please refer to the documentation.

### timescaleDbWriter

timescaleDbWriter writes to the database. It exposes following POST endpoints:

- /reading
- /device

For further information please refer to the documentation.

# TODO/ TOWRITE:
- Which protocols are supported?
- Which calculations can be done?
- Which external frameworks can be plugged-in (JIAC)?
- Which functional services exist (database read/write, aggregation)?
- What endpoints do they expose and how?
- Datatype matching? Topology Management?
- What infrastructure (services) is used?
- What frontend is used?
- How to start it (debuggable, full-start)?
- How to contribute?
- Maven Configuration/ Demo-Module how-to?
- A neat GIF of UI
- What DB is used? How do i set it up?
- Describe CI/CDE Pipe


