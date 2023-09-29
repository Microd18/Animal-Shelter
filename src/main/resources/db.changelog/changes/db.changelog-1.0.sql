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

--changeset pruglo-ve:20230923-4 failOnError:true
--comment: Create cats table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'cats';

CREATE TABLE IF NOT EXISTS cats
(
    id            BIGSERIAL PRIMARY KEY,
    nickname      VARCHAR(255),
    age           INTEGER,
    user_id       BIGINT
        CONSTRAINT fk_cats_users REFERENCES users (id)
);

--changeset pruglo-ve:20230923-5 failOnError:true
--comment: Create dogs table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'dogs';

CREATE TABLE IF NOT EXISTS dogs
(
    id            BIGSERIAL PRIMARY KEY,
    nickname      VARCHAR(255),
    age           INTEGER,
    user_id       BIGINT
        CONSTRAINT fk_dogs_users REFERENCES users (id)
);

--changeset pruglo-ve:20230923-6 failOnError:true
--comment: Create cat_photos table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'cat_photos';
CREATE TABLE IF NOT EXISTS cat_photos
(
    id              BIGSERIAL PRIMARY KEY,
    file_size       BIGINT,
    media_type      VARCHAR(255),
    data            OID
);

--changeset pruglo-ve:20230923-7 failOnError:true
--comment: Create dog_photos table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'dog_photos';
CREATE TABLE IF NOT EXISTS dog_photos
(
    id              BIGSERIAL PRIMARY KEY,
    file_size       BIGINT,
    media_type      VARCHAR(255),
    data            OID
);

--changeset pruglo-ve:20230923-8 failOnError:true
--comment: Create cat_reports table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'cat_reports';

CREATE TABLE IF NOT EXISTS cat_reports
(
    id                  BIGSERIAL PRIMARY KEY,
    photo_id            BIGINT
        CONSTRAINT fk_reports_cat_photos REFERENCES cat_photos (id),
    ration              OID,
    well_being          OID,
    behavior            OID,
    updated             TIMESTAMP,
    created             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id             BIGINT
        CONSTRAINT fk_reports_users REFERENCES users (id),
    cat_id              BIGINT
        CONSTRAINT fk_reports_cats REFERENCES cats (id)
);

--changeset pruglo-ve:20230923-9 failOnError:true
--comment: Create dog_reports table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'dog_reports';

CREATE TABLE IF NOT EXISTS dog_reports
(
    id                  BIGSERIAL PRIMARY KEY,
    photo_id            BIGINT
        CONSTRAINT fk_reports_dog_photos REFERENCES dog_photos (id),
    ration              OID,
    well_being          OID,
    behavior            OID,
    updated             TIMESTAMP,
    created             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id             BIGINT
        CONSTRAINT fk_reports_users REFERENCES users (id),
    dog_id              BIGINT
        CONSTRAINT fk_reports_dogs REFERENCES dogs (id)
);

--changeset pruglo-ve:20230923-10 failOnError:true
--comment: Enter data into a table cats.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from information_schema.tables where table_name = 'cats';

INSERT INTO cats (nickname, age) VALUES
                                     ('Васька', 3),
                                     ('Мурка', 2),
                                     ('Барсик', 5),
                                     ('Матроскин', 1),
                                     ('Том', 4),
                                     ('Леопольд', 2),
                                     ('Базилио', 3),
                                     ('Гарфилд', 6),
                                     ('Геркулес', 2),
                                     ('Шерлок', 4);

--changeset pruglo-ve:20230923-11 failOnError:true
--comment: Enter data into a table dogs.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:1 select count(*) from information_schema.tables where table_name = 'dogs';

INSERT INTO dogs (nickname, age) VALUES
                                     ('Барбос', 2),
                                     ('Рекс', 4),
                                     ('Бобик', 3),
                                     ('Ляля', 5),
                                     ('Шарик', 2),
                                     ('Полкан', 3),
                                     ('Бим', 4),
                                     ('Жучка', 6),
                                     ('Лайка', 3),
                                     ('Рэмбо', 4);

--changeset pruglo-ve:20230924-12 failOnError:true
--comment: Create user_reports_states table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'user_reports_states';

CREATE TABLE IF NOT EXISTS user_reports_states
(
    id              BIGSERIAL PRIMARY KEY,
    state_data      JSONB,
    chat_id         BIGINT UNIQUE
        CONSTRAINT fk_user_reports_states_chat REFERENCES chat (id) ON DELETE CASCADE ON UPDATE CASCADE
);

--changeset microd18:20230929-13 failOnError:true
--comment: Create volunteer_info_cat table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'volunteer_info_cat';

CREATE TABLE IF NOT EXISTS volunteer_info_cat
(
    id                  BIGSERIAL PRIMARY KEY,
    amount_of_days      INTEGER,
    rating              DOUBLE PRECISION,
    user_id             BIGINT
        CONSTRAINT fk_cats_users REFERENCES users (id)
);

--changeset microd18:20230929-14 failOnError:true
--comment: Create volunteer_info_dog table.
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'volunteer_info_dog';

CREATE TABLE IF NOT EXISTS volunteer_info_dog
(
    id                  BIGSERIAL PRIMARY KEY,
    amount_of_days      INTEGER,
    rating              DOUBLE PRECISION,
    user_id             BIGINT
        CONSTRAINT fk_cats_users REFERENCES users (id)
);

--changeset microd18:20230930-15 failOnError:true
--comment: Add extraDays column.
--preconditions onFail:MARK_RAN onError:HALT

ALTER TABLE volunteer_info_cat ADD extra_days INTEGER;

--changeset microd18:20230930-16 failOnError:true
--comment: Add extraDays column.
--preconditions onFail:MARK_RAN onError:HALT

ALTER TABLE volunteer_info_dog ADD extra_days INTEGER;


