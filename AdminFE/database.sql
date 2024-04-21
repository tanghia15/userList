CREATE DATABASE Shop_app;
USE Shop_app;

CREATE TABLE users(
    id SERIAL PRIMARY KEY ,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '',
    created_at timestamp with time zone,
    updated_at timestamp with time zone,
    is_active BYTEA,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0
);
CREATE TABLE tokens(
    id SERIAL PRIMARY KEY ,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expriration_date timestamp with time zone,
    revoked bytea NOT NULL,
    expired bytea NOT NULL,
    user_id INT,
    FOREIGN KEY(user_id) REFERENCES users(id)
);
--ho tro dang nhap fb va gg
CREATE TABLE social_accounts(
    id SERIAL PRIMARY KEY ,
    provider VARCHAR(20) NOT NULL ,
    provider_id VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL ,
    name VARCHAR(100) NOT NULL ,
    user_id INT,
    FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE roles(
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL

);
select * from roles
select * from users 
select * from tokens