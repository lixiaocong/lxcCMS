# lixiaocong's personal CMS (***For version 1, see the master branch***)

This is a personal Content Management System.

The system consists of 3 parts:
+ Blog
    - Register an account
    - Login with Facebook and QQ
    - Post, update and delete blogs
    - Comment on blogs
+ Admin
    - Manage users, blogs, comments
    - Upload torrent file and download the file to your server and get an unique URL after finished
+ WeChat
    - Users can get latest blogs on their WeChat

The purpose of this project is to learn Spring including:
- Spring framework
- Spring Security
- Spring Social
- Spring Data
- Spring Boot
- Spring MVC

Basic knowledge like HTML, CSS, JavaScript can be learnt and other front end techniques such as Bootstrap, FreeMarker, jQuery can also be learnt
With the help of this project, you can also learn how to use Nginx/Apache web server, MySQL and maybe Docker if you want.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To run this project on your machine, you need to install MySQL(5.7+), Transmission, Nginx/Apache. Installing theme using Docker is recommended. Install Docker on your machine. If you don’t know how to do it , check out **[this website]( https://www.docker.com/)**

### Installing
After that, open your terminal on Linux/Mac or command window on Windows
```
docker run --name nginx -d -p <your local port>:80 -v <your local folder>:/usr/share/nginx/html nginx:stable
docker run --name transmission -d -p <your local port>:9091 -v <your local folder>:/var/lib/transmission-daemon dperson/transmission
docker run --name mysql -d -p <your local port>:3306 -e MYSQL_ROOT_PASSWORD=root mysql:5.7 docker run --name mysql -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```
The username and password of Transmission are both **admin**

The username and password of MySQL are both **root**
Execute db.sql to init your database

## Deployment

Clone the project to your machine using git
```
cd <any folder on your machine>
git clone git@github.com:lixiaocong/lxcCMS.git
```

Change the src/main/resources/application.properties and src/main/resources/qqconnectconfig.properties. Templates can be downloaded [here]( http://www.lixiaocong.com:9401/application.properties) and [here]( http://www.lixiaocong.com:9401/qqconnectconfig.properties)

Cd to the downloaded folder, run the command
```
./gradlew bootRun
```
or you can run
```
./gradlew war
```
than move the war file to Tomcat webapps folder


## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to me.

## Author

**li xiaocong**

## License

BSD 3-Clause License

Copyright (c) 2016, lixiaocong(lxccs@iCloud.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
