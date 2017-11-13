#!/usr/bin/env bash

# install jdk
apt install -y openjdk-8-jdk

# install nvm
wget -qO- https://raw.githubusercontent.com/creationix/nvm/v0.33.6/install.sh | bash

# install docker and docker-compose
curl -fsSL get.docker.com -o get-docker.sh
sh get-docker.sh
sudo apt install -y docker-compose
