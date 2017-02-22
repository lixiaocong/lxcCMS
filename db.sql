CREATE TABLE user
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    admin BIT(1) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX UK_sb8bbouer5wak8vyiiy4pf2bx ON user (username);

CREATE TABLE article
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    content LONGTEXT NOT NULL,
    image LONGTEXT,
    summary VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    CONSTRAINT FKbc2qerk3l47javnl2yvn51uoi FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE INDEX FKbc2qerk3l47javnl2yvn51uoi ON article (user_id);

CREATE TABLE comment
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    content LONGTEXT NOT NULL,
    article_id BIGINT(20) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    CONSTRAINT FK5yx0uphgjc6ik6hb82kkw501y FOREIGN KEY (article_id) REFERENCES article (id),
    CONSTRAINT FK8kcum44fvpupyw6f5baccx25c FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE INDEX FK5yx0uphgjc6ik6hb82kkw501y ON comment (article_id);
CREATE INDEX FK8kcum44fvpupyw6f5baccx25c ON comment (user_id);

-- 创建spring social 数据库 --
create table UserConnection (userId varchar(255) not null,
	providerId varchar(255) not null,
	providerUserId varchar(255),
	rank int not null,
	displayName varchar(255),
	profileUrl varchar(512),
	imageUrl varchar(512),
	accessToken varchar(512) not null,
	secret varchar(512),
	refreshToken varchar(512),
	expireTime bigint,
	primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);