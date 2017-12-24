#!/usr/bin/env bash

# update code
sudo git pull

# build the angular project
cd admin
sudo npm install
sudo npm run build

# build the docker image
cd ..
sudo ./gradlew buildDocker

# run the project
sudo docker-compose up
