CREATE DATABASE balajar_spring_api;

USE balajar_spring_api;

CREATE TABLE user
(
    username        VARCHAR(100) NOT NULL,
    password        VARCHAR(100) NOT NULL,
    name            VARCHAR(100) NOT NULL,
    token           VARCHAR(100),
    token_expire_at BIGINT,
    PRIMARY KEY (username),
    UNIQUE (token)
) ENGINE InnoDB;

SELECT * FROM user;

DESC user;

CREATE TABLE contacts
(
    id         VARCHAR(100) NOT NULL,
    username   VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100),
    phone      VARCHAR(100),
    email      VARCHAR(100),
    PRIMARY KEY (id),
    FOREIGN KEY fk_user_contact (username) REFERENCES user(username)
) ENGINE InnoDB;

SELECT * FROM contacts;

DESC contacts;

CREATE TABLE addresses
(
    id          VARCHAR(100) NOT NULL,
    contact_id  VARCHAR(100) NOT NULL,
    street      VARCHAR(100),
    city        VARCHAR(100),
    province    VARCHAR(100),
    country     VARCHAR(100) NOT NULL,
    postal_code VARCHAR(100),
    PRIMARY KEY (id),
    FOREIGN KEY fk_contacts_addresses (contact_id) REFERENCES contacts (id)
) ENGINE InnoDB;

SELECT * FROM addresses;

DESC addresses;