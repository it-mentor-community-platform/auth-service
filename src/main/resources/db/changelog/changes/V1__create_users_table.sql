--liquibase formatted sql

--changeset auth-service:create-users-table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       telegram_user_id BIGINT NOT NULL UNIQUE
);