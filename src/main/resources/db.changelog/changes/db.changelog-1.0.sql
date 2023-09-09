--liquibase formatted sql

--changeset koroleva:1_create_table_chat_state
CREATE TABLE chat_state (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    two_step_back_state TEXT,
    step_back_state TEXT,
    current_state TEXT
);

--changeset koroleva:2_create_table_users
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT REFERENCES chat_state(chat_id) ON DELETE RESTRICT,
    username VARCHAR(100),
    fullname VARCHAR(100),
    phone VARCHAR(100),
    email VARCHAR(100)
);




