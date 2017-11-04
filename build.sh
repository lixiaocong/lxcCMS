#!/usr/bin/env bash

cd admin
npm install
npm run build
cd ..
./gradlew buildDocker