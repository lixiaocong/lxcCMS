#!/usr/bin/env bash

# build the angular project
cd admin
cnpm install
npm run devBuild

# build the docker image
cd ..
./gradlew buildDocker

# run the project
docker-compose up