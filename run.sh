#!/usr/bin/env bash

# update code
sudo git pull

# build the angular project
cd admin
sudo npm install
sudo npm run build
cd ..

# build the docker image
cd cms
sudo ./gradlew buildDocker
cd ..

# run the project
sudo docker-compose up -d
sudo docker-compose logs -t >> cms.log
