<!DOCTYPE html>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="keywords" content="lixiaocong, 李晓聪">
    <meta property="qc:admins" content="1244377267641011737676375"/>
    <link href="/image/favicon.ico" rel="Shortcut Icon"/>
    <link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link href="/css/blog.css" rel="stylesheet">

    <title>浏览</title>
</head>

<body>
<#include "header.ftl">
<div class="container sections-wrapper">
    <div class="row">
        <div class="primary col-sm-8 col-xs-12">
        <#list articles as article>
        <div class="latest section">
            <div class="section-inner">
                <h1 class="title"><a href="/blog/detail?id=${article.id}">${article.title}</a></h1>
                <h5>作者:${article.user.username}</h5>
                <div class="content">
                    <div class="item row">
                        <#if article.image??>
                            <div class="col-sm-4 col-xs-12">
                                <img class="img-responsive project-image" src="${article.image}"/>
                            </div>
                        <div class="desc col-sm-8 col-xs-12">
                        <#else >
                        <div class="desc col-xs-12">
                        </#if>
                        <p>${article.summary}</p>
                        <p><a class="more-link" href="/blog/detail?id=${article.id}"><i
                                class="fa fa-external-link"></i>阅读全文</a></p>
                    </div>
                    </div>
                    </div>
                    <h6 class="year">${article.createTime?string("yyyy年MM月dd HH:mm")}</h6>
                </div>
            </div>
        </#list>

            <ul class="pagination">
            <#if pageCurr != pageMin>
                <li><a href="/blog?page=${pageCurr-1}">&laquo;</a></li>
            <#else >
                <li class="disabled"><span>&laquo;</span></li>
            </#if>

            <#list pageMin..pageMax as page>
                <#if page == pageCurr>
                    <li class="active"><span>${page}</span></li>
                <#else >
                    <li><a class="page-btn" href="/blog?page=${page}">${page}</a></li>
                </#if>
            </#list>

            <#if pageCurr != pageMax>
                <li><a href="/blog?page=${pageCurr+1}">&raquo;</a></li>
            <#else >
                <li class="disabled"><span>&raquo;</span></li>
            </#if>
            </ul>
        </div>

            <div class="secondary col-sm-4 col-xs-12">
            <#include "side.ftl">
            </div>
        </div>
    </div>
    <script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>