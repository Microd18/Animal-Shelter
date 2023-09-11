--liquibase formatted sql

--changeset pruglo-ve:20230911-1 failOnError:true
--comment: Create chat_state table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'chat_state';

CREATE TABLE IF NOT EXISTS chat_state (
    id                      BIGSERIAL      PRIMARY KEY,
    chat_id                 BIGINT,
    current_state           VARCHAR(255),
    step_back_state         VARCHAR(255),
    two_step_back_state     VARCHAR(255),
    is_bot_started          BOOLEAN
);

--changeset pruglo-ve:20230911-2 failOnError:true
--comment: Create users table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'users';

CREATE TABLE IF NOT EXISTS users (
    id                      BIGSERIAL      PRIMARY KEY,
    email                   VARCHAR(255),
    phone                   VARCHAR(255),
    username                VARCHAR(255),
    chat_state_id           BIGINT
);