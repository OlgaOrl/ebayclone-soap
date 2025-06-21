#!/bin/bash

# Build the project
mvn clean package

# Run with Docker
docker-compose up --build