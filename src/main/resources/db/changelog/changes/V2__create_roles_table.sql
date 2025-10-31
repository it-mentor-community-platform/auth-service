--liquibase formatted sql

--changeset auth-service:create-roles-table
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);