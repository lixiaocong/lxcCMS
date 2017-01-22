CREATE DATABASE lxcCMS;
CREATE TABLE article
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    content LONGTEXT NOT NULL,
    image LONGTEXT,
    summary VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    user_id BIGINT(20) NOT NULL
);
CREATE TABLE comment
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    content LONGTEXT NOT NULL,
    article_id BIGINT(20) NOT NULL,
    user_id BIGINT(20) NOT NULL
);
CREATE TABLE user
(
    id BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    create_time DATETIME NOT NULL,
    last_update_time DATETIME NOT NULL,
    admin BIT(1) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);

ALTER TABLE article ADD FOREIGN KEY (user_id) REFERENCES user (id);
CREATE INDEX FKbc2qerk3l47javnl2yvn51uoi ON article (user_id);
ALTER TABLE comment ADD FOREIGN KEY (article_id) REFERENCES article (id);
ALTER TABLE comment ADD FOREIGN KEY (user_id) REFERENCES user (id);
CREATE INDEX FK5yx0uphgjc6ik6hb82kkw501y ON comment (article_id);
CREATE INDEX FK8kcum44fvpupyw6f5baccx25c ON comment (user_id);
CREATE UNIQUE INDEX UK_sb8bbouer5wak8vyiiy4pf2bx ON user (username);

INSERT INTO lxcCMS.user (create_time, last_update_time, admin, password, username) VALUES ('2017-01-23 00:37:29', '2017-01-23 00:37:31', true, '$2a$10$DSqk6EkAf7C0xut5Yh16A.qtQrBVA6AJD0Bq.pJffAy1Jucpxv8Ju', 'admin');