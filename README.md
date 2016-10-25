# lixiaocong的内容管理系统

## 博客部分
+ 用户的注册登录（支持qq和facebook）
+ 文章增删改查

## 管理部分
+ 对Transmission任务的添加,删除,下载

## 微信公众号
+ 可以设置公众号token，公众号可以获取最新文章

## 技术
+ 逻辑层：spring
+ 数据层：hibernate+jpa+mysql
+ 表示层：jsp + angular

## 服务器需要安装的程序：
+ mysql5.7
+ transmission-daemon

## 使用方法
+ 设置application.properties中所有xxx部分的内容
+ 设置qqconnectconfig.properties中所有xxx部分内容
+ 设置logback-spring.xml确定日志格式和内容