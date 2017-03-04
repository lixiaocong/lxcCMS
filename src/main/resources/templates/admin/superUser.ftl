<!--
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
-->

<!DOCTYPE html>
<html lang="zh-CN" ng-app="Admin" ng-controller="AdminCtrl">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link rel="Shortcut Icon" href="/image/favicon.ico"/>
    <link rel="stylesheet" href="/css/wangEditor.min.css">
    <link rel="stylesheet" href="/css/admin.css">

    <title>管理系统</title>
</head>

<body>
<nav class="navbar-inverse navbar-fixed-top" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target="#navbar">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <span class="navbar-brand">Hola
        ${username}
        </span>
    </div>

    <div class="collapse navbar-collapse" id="navbar">
        <ul class="nav navbar-nav">
            <li><a href="#/dashboard">DashBoard</a></li>
            <li><a href="#/info">账号</a></li>
            <li><a href="#/user">用户</a></li>
            <li><a href="#/article">文章</a></li>
            <li><a href="#/comment">评论</a></li>
            <li><a href="#/downloader">BT</a></li>
            <li><a href="#/file">文件</a></li>
        </ul>
        <ul class="nav navbar-nav right-nav">
            <li><a href="/blog">返回博客</a></li>
            <li><a href="/logout">退出</a></li>
        </ul>
    </div>
</nav>

<script type="text/ng-template" id="alert.html">
    <div ng-transclude></div>
</script>

<div class="content-wrapper">
    <div uib-alert
         ng-repeat="alert in alerts"
         ng-class="'alert-' + (alert.type || 'warning')"
         close="closeAlert($index)"
         dismiss-on-timeout="3000"
         ng-bind="alert.msg">
    </div>
    <div ng-view=""></div>
</div>
<script src="//cdn.bootcss.com/angular.js/1.5.8/angular.min.js"></script>
<script src="//cdn.bootcss.com/angular.js/1.5.8/angular-route.min.js"></script>
<script src="//cdn.bootcss.com/angular.js/1.5.8/angular-animate.min.js"></script>
<script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/angular-ui-bootstrap/2.0.0/ui-bootstrap-tpls.min.js"></script>
<script src="/wangEditor.min.js"></script>

<script src="/js/superUser.js"></script>
<script src="/js/dashboard.js"></script>
<script src="/js/info.js"></script>
<script src="/js/user.js"></script>
<script src="/js/article.js"></script>
<script src="/js/comment.js"></script>
<script src="/js/downloader.js"></script>
<script src="/js/file.js"></script>
</body>
</html>
