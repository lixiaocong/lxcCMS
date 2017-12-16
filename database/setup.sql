CREATE DATABASE IF NOT EXISTS lxcCMS;
USE lxcCMS;

CREATE TABLE user
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    admin BIT(1) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX UK_user_index ON user (username);

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
    CONSTRAINT FK_article_user FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE INDEX FK_article_user ON article (user_id);

CREATE TABLE comment
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    content LONGTEXT NOT NULL,
    article_id BIGINT(20) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    CONSTRAINT FK_comment_article FOREIGN KEY (article_id) REFERENCES article (id),
    CONSTRAINT FK_comment_user FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE INDEX FK_comment_article ON comment (article_id);
CREATE INDEX FK_comment_user ON comment (user_id);

-- 创建spring social 数据库 --
create table UserConnection (
  userId varchar(255) not null,
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
	primary key (userId, providerId, providerUserId)
);
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);

INSERT INTO user (create_time, last_update_time, admin, password, username) VALUES ('2017-01-01 00:00:00', '2017-01-01 00:00:00', true, '$2a$10$mQStoPcXwGUIRzJXbr87aukqNhtl5np325iof39Rdt7zIMI8oUYaG', 'admin');
