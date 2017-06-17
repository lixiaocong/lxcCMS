#!/usr/bin/env bash
tomcat_dir='/Users/lixiaocong/Documents/tomcat/webapps'
npm='cnpm'

echo 'start build angular admin'
cd admin
${npm} install
ng build --bh /admin/ --e prod
cd ..

echo 'start build war'
./gradlew war

echo 'move war to tomcat webapp'
mv ./build/libs/lxcCMS.war ${tomcat_dir}/ROOT.war

echo 'success'
