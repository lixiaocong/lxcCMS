#!/usr/bin/env bash

# install jdk
sudo apt install -y openjdk-8-jdk

# install nodejs 8
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs

# install docker and docker-compose
curl -fsSL get.docker.com -o get-docker.sh
sh get-docker.sh
sudo apt install -y docker-compose

