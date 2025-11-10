--liquibase formatted sql

--changeset auth-service:insert-default-roles
INSERT INTO roles (name) VALUES
                             ('ADMIN'),
                             ('STUDENT'),
                             ('MENTOR');
