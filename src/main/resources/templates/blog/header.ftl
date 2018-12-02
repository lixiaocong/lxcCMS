<nav class="navbar-default navbar-fixed-top">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target="#navbar">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>

        <span class="navbar-brand">Hi
        ${username!"guest"}
        </span>
    </div>

    <div class="collapse navbar-collapse" id="navbar">
        <ul class="nav navbar-nav">
            <li><a href="/blog">articles</a></li>
            <li><a href="/blog/edit">publish</a></li>
        </ul>
        <ul class="nav navbar-nav" id="logout">
        <#if username??>
            <li><a href="/admin">admin</a></li>
            <li><a href="/logout">logout</a></li>
        <#else>
            <li><a href="/signin">login</a></li>
        </#if>
        </ul>
    </div>
</nav>

<header class="header">
    <div class="container">
        <img class="profile-image img-responsive pull-left" src="/image/logo.png"/>
        <div class="profile-content pull-left">
            <h1 class="name">lixiaocong's blog</h1>
            <ul class="social list-inline">
                <li><a href="tencent://message/?uin=960418051&Site=&Menu=yes"><i class="fa fa-qq"></i></a></li>
                <li><a href="https://github.com/lixiaocong"><i class="fa fa-github-alt"></i></a></li>
            </ul>
        </div>
    </div>
</header>
</div>
