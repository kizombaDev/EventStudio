[![Build Status](https://travis-ci.org/kizombaDev/EventStudio.svg?branch=master)](https://travis-ci.org/kizombaDev/EventStudio) [![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=org.kizombadev.eventstudio%3Aapplication-pom&metric=coverage)](https://sonarcloud.io/dashboard?id=org.kizombadev.eventstudio%3Aapplication-pom) [![Test Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.kizombadev.eventstudio%3Aapplication-pom&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.kizombadev.eventstudio%3Aapplication-pom) [![Test Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.kizombadev.eventstudio%3Aapplication-pom&metric=security_rating)](https://sonarcloud.io/dashboard?id=org.kizombadev.eventstudio%3Aapplication-pom)

EventStudio
=========

Abstract
-------

In today's digitized world, IT systems become increasingly important. To ensure error-free operation, an analysis of IT systems is of elementary importance. There are several types of analyses: the classical error analysis, the availability analysis, the performance analysis, and the detection of hacker attacks (security analysis). In the first instance, an IT monitoring system must be able to handle a wide variety of event formats and a huge number of events so that the mentioned types of analyses work. The aim of this master's thesis is to design a software architecture of an IT monitoring system that enables the administrator to monitor and analyse different IT systems.  The created software architecture, based on a generic approach, should support the most different system events and use cases. At the end of this thesis, an architecture and technology recommendation for an IT monitoring application is created, which enables the analysis of IT systems. Subsequent further scientific work and projects can build on these findings.

Building
-------

To build sources locally follow these instructions.

    # Clone the repository to a local folder
    git clone https://github.com/kizombaDev/EventStudio.git

    # Execute the build an the unit tests
    ./mvnw clean test
    
    # Execute the integration tests
    ./mvnw verify
    
    # Start the server by executing:
    TODO ElasticSearch
    ./start.cmd
    
    # run web app with hot reload at localhost:8080
    cd event-apps\web-app
    npm run dev

License
-------

Copyright 2018 Marcel Swoboda.

Licensed under the [GNU Lesser General Public License, Version 3.0](https://www.gnu.org/licenses/lgpl.txt)