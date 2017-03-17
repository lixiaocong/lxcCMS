# lixiaocong的内容管理系统

## 博客部分
+ 用户的注册登录
+ 文章增删改查

## 管理部分
+ 对Transmission任务的添加,删除,下载

## 微信公众号
+ 可以设置公众号token，公众号可以获取最新文章

## 技术
+ 逻辑层：spring
+ 数据层：hibernate + jpa + mysql
+ 表示层：jsp + angular

## 服务器需要安装的程序：（建议使用docker）
+ mysql5.7，创建blog表
+ transmission-daemon，设置用户名密码
+ aria2c,设置token

## 计划
+ (增加aria2c支持)
+ (计划升级为angular2 + websocket)
+ (计划增加google的ui风格)
+ (准备删除Facebook）
+ (添加邮件支持）
