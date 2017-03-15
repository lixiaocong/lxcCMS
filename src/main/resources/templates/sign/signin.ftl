<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/sign.css">
    <link rel="Shortcut Icon" href="/image/favicon.ico"/>
    <title>lixiaocong's blog</title>
</head>

<body>
<div class="row" id="container">
    <div class="col-sm-offset-4 col-sm-4 col-xs-offset-2 col-xs-8">
        <form class="form-horizontal" role="form" id="signinForm" action="/signin" method="post">
            <div class="form-group">
                <h1>登录</h1>
            </div>
        <#if message??>
            <span class="error">${message}</span>
        </#if>
        <#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
            <span class="error">登录失败,请重试</span>
        </#if>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="text" class="form-control"
                           placeholder="请输入用户名" name="username" id="username"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="password" class="form-control"
                           placeholder="请输入密码" name="password" id="password"></div>
            </div>
            <div class="form-group">
                <div class="col-xs-6 col-sm-8">
                    <input type="text" class="form-control" placeholder="请输入验证码" name="imagecode" id="imagecode">
                </div>
                <div class="col-xs-6 col-sm-4">
                    <img src="/file/imagecode" onclick="this.src=this.src+'?'+Math.random()">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <label for="remember-me"><input type="checkbox" name="remember-me" id="remember-me"> 记住我</label>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <input type="submit" class="btn btn-primary" value="登录"/>
                    <button class="btn btn-info cancel" form="facebook">
                        <span class="fa fa-facebook"></span>
                    </button>
                    <button class="btn btn-info cancel" form="qq">
                        <span class="fa fa-qq"></span>
                    </button>
                    <a href="/signup" class="btn btn-default pull-right">去注册</a>
                </div>
            </div>
        </form>
        <div hidden>
            <form id="facebook" action="/signin/facebook" method="post"></form>
            <form id="qq" action="/signin/qq" method="post"></form>
        </div>
    </div>
</div>
<script src="//cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery-validate/1.15.1/jquery.validate.min.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
<script type="text/javascript">
    $().ready(function () {
        $("#signinForm").validate({
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                },
                imagecode: {
                    required: true
                }
            }
        });
    });
</script>
</body>
</html>