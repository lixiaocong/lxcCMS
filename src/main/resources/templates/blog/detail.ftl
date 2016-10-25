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
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="/image/favicon.ico" rel="Shortcut Icon"/>
    <link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link href="/css/blog.css" rel="stylesheet">

    <title>${article.title}</title>
</head>

<body>
<#include "header.ftl">
<div class="container sections-wrapper">
    <div class="row">
        <div class="primary col-sm-8 col-xs-12">
            <div class="section">
                <div class="section-inner">
                    <h1 class="title">${article.title}</h1>
                    <h5>作者:${article.user.username}</h5>
                    <div class="content"><p>${article.content}</p></div>
                    <h6 class="year">最后编辑于${article.lastUpdateTime?string("yyyy年MM月dd HH:mm")}</h6>
                </div>
            </div>

            <div><#list comments as comment>
                <div class="panel-footer">
                    <p><b>${comment.user.username}</b>: ${comment.content}</p>
                    <h6 class="year">${comment.lastUpdateTime?string("yyyy年MM月dd HH:mm")}</h6>
                </div>
            </#list>
            </div>
            <br>

            <div class="panel panel-info">
                <div class="panel-heading">
                    评论
                </div>
                <form action="/blog/comment/${article.id}" id="commentForm" method="post">
                    <div class="panel-body">
                        <textarea name="content" id="content" rows="5" style="width: 100%;"></textarea>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" class="btn btn-default" value="提交"/>
                    </div>
                </form>
            </div>
        </div>

        <div class="secondary col-sm-4 col-xs-12">
        <#include "side.ftl">
        </div>
    </div>
</div>
<script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery-validate/1.15.1/jquery.validate.min.js"></script>
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/localization/messages_zh.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $().ready(function () {
        $("#commentForm").validate({
            rules: {
                content: {
                    required: true
                }
            }
        });
    });
</script>
</body>
</html>