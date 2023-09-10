--liquibase formatted sql

--changeset koroleva:1_create_table_chat_state
--precondition onFail:MARK_RAN
--rollback DROP TABLE chat_state
CREATE TABLE chat_state (
    id BIGINT UNIQUE,
    chat_id BIGINT UNIQUE,
    two_step_back_state VARCHAR(200),
    step_back_state VARCHAR(200),
    current_state VARCHAR(200)
);

--changeset koroleva:2_create_table_users
--precondition onFail:MARK_RAN
--rollback DROP TABLE users
CREATE TABLE users (
    id BIGINT UNIQUE,
    chat_id BIGINT REFERENCES chat_state(chat_id),
    username VARCHAR(100),
    fullname VARCHAR(100),
    phone VARCHAR(100),
    email VARCHAR(100)
);




