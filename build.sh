#!/usr/bin/env bash
tomcat_dir='/home/lixiaocong'
npm='npm'

echo 'changing folder'
current_path=$(cd `dirname $0`; pwd)

echo 'building admin'
cd ${current_path}/admin
${npm} install
${npm} install -g @angular/cli
ng build --bh /admin/ --e prod
cd ..

echo 'building war'
./gradlew war

echo 'move war to tomcat webapp'
mv ./build/libs/lxcCMS.war ${tomcat_dir}/ROOT.war

echo 'success'
