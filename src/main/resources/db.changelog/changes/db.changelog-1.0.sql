--liquibase formatted sql

--changeset pruglo-ve:20230916-1 failOnError:true
--comment: Create chat table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'chat';

CREATE TABLE IF NOT EXISTS chat
(
    id          BIGSERIAL               PRIMARY KEY,
    chat_id     BIGINT      UNIQUE      NOT NULL,
    bot_started BOOLEAN                 NOT NULL
);

--changeset pruglo-ve:20230911-2 failOnError:true
--comment: Create chat_state table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'chat_state';

create TABLE IF NOT EXISTS chat_states
(
    id              BIGSERIAL PRIMARY KEY,
    state_data      JSONB,
    chat_id         BIGINT UNIQUE
        CONSTRAINT fk_chat_states_chat REFERENCES chat (id) ON DELETE CASCADE ON UPDATE CASCADE
);

--changeset pruglo-ve:20230911-3 failOnError:true
--comment: Create users table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'users';

create TABLE IF NOT EXISTS users
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255),
    phone         VARCHAR(255),
    username      VARCHAR(255),
    chat_id       BIGINT UNIQUE
        CONSTRAINT fk_users_chat REFERENCES chat (id) ON DELETE CASCADE ON UPDATE CASCADE
);