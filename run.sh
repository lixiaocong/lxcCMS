#!/usr/bin/env bash

# build the angular project
cd admin
npm install
npm run devBuild

# build the docker image
cd ..
./gradlew buildDocker

# run the project
docker-compose up