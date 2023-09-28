# Reactive REST Service with Java + Spring Boot

This repository is to showcase Reactive REST Service using Java and Spring Boot.

## Github Actions Build status
[![Build Status](https://github.com/harishkannarao/java-reactive-rest-service/workflows/CI-main/badge.svg)](https://github.com/harishkannarao/java-reactive-rest-service/actions?query=workflow%3ACI-main)

## Required Softwares, Tools and Version
* Java JDK Version: 17
* Gradle Version: 7
* Git Client: Any latest version
* Docker: Any latest version
* Integrated Development Environment: Any version of IntelliJ Idea or Eclipse

## Running the build

    ./gradlew clean build --info
    
## Running the application using build tool

    ./gradlew runLocal

## Run the sample application using java
    
    ./gradlew clean assemble

    java -jar build/libs/java-reactive-rest-service.jar 

## Update dependencies to latest version

    ./gradlew useLatestVersions
