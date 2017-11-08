#!/usr/bin/env bash

# install required lib
apt install -y curl nodejs

# install docker and docker-compose
curl -fsSL get.docker.com -o get-docker.sh
sh get-docker.sh
sudo apt install -y docker-compose
