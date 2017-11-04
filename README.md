# lixiaocong's personal CMS

This is a personal Content Management System.

The system consists of 3 parts:
+ Blog
    - Register an account
    - Connect to QQ ans login with QQ
    - Post, update and delete blog
    - Comment on blog
+ Admin
    - Manage user, blog, comments
    - Upload download task and download file to your server
+ WeChat
    - Users can get latest blog on their WeChat

The project is based on Spring including:
- Spring Framework
- Spring Security
- Spring Social
- Spring Data
- Spring Boot
- Spring MVC

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To run this project on your machine, you need to install NodeJS Tomcat8 and Aria2.

I'm using H2 as my database, you can change to MySQL and other JDBC database.
Execute db.sql to init your database

The downloader is aria2c, you can also change to other downloader such as Transmission,checkout my [code](https://github.com/lixiaocong/transmission4j)

## Deployment

Clone the project to your machine using git
```
cd <any folder on your machine>
git clone git@github.com:lixiaocong/lxcCMS.git
cd lxcCMS
```

Edit the /admin/src/environments/environments.prod.ts to config your server address.

Then compile the Angular admin app
```bash
cd admin
npm install
npm install -g @angular/cli
ng build --bh /admin/ --prod --aot
```
Create application.properties and qqconnectconfig.properties in /src/mian/resources/. An example can be found in wiki page on my GitHub.

Compile the Java project
```
./gradlew war
mv ./build/libs/lxcCMS.war ${tomcat_dir}/ROOT.war
```

Your can also use the [hook](https://github.com/lixiaocong/hook)
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