#!/usr/bin/env bash

cd admin
npm install
npm run devBuild
cd ..
./gradlew buildDocker