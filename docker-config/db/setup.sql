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

CREATE TABLE UserConnection (
  userId VARCHAR(255) NOT NULL,
	providerId VARCHAR(255) NOT NULL,
	providerUserId VARCHAR(255),
	rank INT NOT NULL,
	displayName VARCHAR(255),
	profileUrl VARCHAR(512),
	imageUrl VARCHAR(512),
	accessToken VARCHAR(512) NOT NULL,
	secret VARCHAR(512),
	refreshToken VARCHAR(512),
	expireTime BIGINT,
	PRIMARY KEY (userId, providerId, providerUserId)
);
CREATE UNIQUE INDEX UK_user_connection_rank ON UserConnection(userId, providerId, rank);

CREATE TABLE config
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY ,
  create_time DATETIME NOT NULL,
  last_update_time DATETIME NOT NULL,
  config_key VARCHAR(255) NOT NULL,
  config_value VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX UK_config_index ON config (config_key);

INSERT INTO user (create_time, last_update_time, admin, password, username) VALUES ('2017-01-01 00:00:00', '2017-01-01 00:00:00', true, '$2a$10$mQStoPcXwGUIRzJXbr87aukqNhtl5np325iof39Rdt7zIMI8oUYaG', 'admin');
