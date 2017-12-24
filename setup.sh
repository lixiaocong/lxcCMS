#!/usr/bin/env bash

# update
sudo apt update
sudo apt upgrade

# install jdk and curl
sudo apt install -y openjdk-8-jdk git curl

# install nodejs 8
sudo curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs

# install docker and docker-compose
sudo curl -fsSL get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# install docker compose
sudo curl -L https://github.com/docker/compose/releases/download/1.18.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
