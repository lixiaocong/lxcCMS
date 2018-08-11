<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/sign.css">
    <link rel="Shortcut Icon" href="/image/favicon.ico"/>
    <title>lixiaocong's blog</title>
</head>

<body>
<div id="container" class="row">
    <div class="col-sm-offset-4 col-sm-4 col-xs-offset-2 col-xs-8">
        <form class="form-horizontal" role="form" id="signupForm" action="/singup" method="post">
            <div class="form-group">
                <h1>用户注册</h1>
            </div>
        <#if message??>
            <span class="error">${message}</span>
        </#if>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="text" class="form-control"
                           placeholder="请输入用户名" name="username" id="username" value="${username!""}"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="password" class="form-control"
                           placeholder="请输入密码" name="password" id="password"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="password" class="form-control"
                           placeholder="请再次输入密码" name="repsword" id="repsword">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <div class="pull-left">
                        <input type="submit" class="btn btn-primary" value="注册"/>
                    </div>
                    <div class="pull-right">
                        <a href="/signin" class="btn btn-default">返回登录</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="//cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery-validate/1.15.1/jquery.validate.min.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
<script type="text/javascript">
    $().ready(function () {
        $("#signupForm").validate({
            rules: {
                username: {
                    required: true,
                    minlength: 3
                },
                password: {
                    required: true,
                    minlength: 6
                },
                repsword: {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                }
            }
        });
    });
</script>
</body>
</html>