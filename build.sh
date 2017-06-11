#!/bin/bash
echo "start build..."
cd admin
npm install
npm run build
cd ..
./gradlew war
