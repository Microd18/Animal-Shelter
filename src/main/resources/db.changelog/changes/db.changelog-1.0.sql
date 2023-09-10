--liquibase formatted sql

--changeset koroleva:1_create_enum_for_chat_states
--precondition onFail:MARK_RAN
--rollback DROP TYPE bot_command
CREATE TYPE bot_command AS ENUM (
'CAT',
'DOG',
'HELP',
'BACK',
'PASS',
'STOP',
'START',
'SAFETY',
'CONTACT',
'SCHEDULE',
'DESCRIPTION',
'SHELTER_INFO',
'DATING_RULES',
'DOCUMENTS',
'TRANSPORTATION_RECOMMENDATION',
'KITTY_HOME_SETUP_RECOMMENDATION',
'PUPPY_HOME_SETUP_RECOMMENDATION',
'HOME_SETUP_RECOMMENDATION',
'SPECIAL_NEED_RECOMMENDATION',
'DOG_TRAINER_ADVICE',
'DOG_TRAINER_RECOMMENDATION',
'REFUSAL_REASON'
);

--changeset koroleva:2_create_table_chat_state
--precondition onFail:MARK_RAN
--rollback DROP TABLE chat_state
CREATE TABLE chat_state (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    two_step_back_state bot_command,
    step_back_state bot_command,
    current_state bot_command
);

--changeset koroleva:3_create_table_users
--precondition onFail:MARK_RAN
--rollback DROP TABLE chat_state
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT REFERENCES chat_state(chat_id) ON DELETE RESTRICT,
    username VARCHAR(100),
    fullname VARCHAR(100),
    phone VARCHAR(100),
    email VARCHAR(100)
);




