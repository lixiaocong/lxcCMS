#!/usr/bin/env bash

# install required lib
sudo apt install -y curl nodejs 

# install docker and docker-compose
curl -fsSL get.docker.com -o get-docker.sh
sh get-docker.sh
sudo apt install -y docker-compose

# build the angular project
cd admin
npm install
npm run devBuild

# build the docker image
cd ..
./gradlew buildDocker

# run the project in deamon
docker-compose up -d