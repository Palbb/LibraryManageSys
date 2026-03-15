-- Таблица учетных записей (админы, библиотекари и т.д.)
CREATE TABLE account (
                         id       BIGSERIAL PRIMARY KEY,
                         username VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         role     VARCHAR(50)  NOT NULL 
);

-- Таблица книг
CREATE TABLE books (
                       id               BIGSERIAL PRIMARY KEY,
                       title            VARCHAR(255) NOT NULL,
                       author           VARCHAR(255) NOT NULL,
                       name             VARCHAR(255) NOT NULL UNIQUE,
                       isbn             VARCHAR(255) NOT NULL,
                       available_copies INT          NOT NULL DEFAULT 0
);

-- Таблица читателей, связанная с аккаунтом
CREATE TABLE readers (
                         id                BIGSERIAL PRIMARY KEY,
                         full_name         VARCHAR(255) NOT NULL,
                         email             VARCHAR(255),
                         account_id        BIGINT       NOT NULL UNIQUE,
                         registration_date DATE DEFAULT CURRENT_DATE,

                         CONSTRAINT fk_reader_account FOREIGN KEY (account_id) REFERENCES account(id)
);

-- Таблица записей о выдаче книг (связующая таблица)
CREATE TABLE borrow_record (
                               id               BIGSERIAL PRIMARY KEY,
                               book_id          BIGINT NOT NULL,
                               reader_id        BIGINT NOT NULL,
                               borrow_time      DATE    DEFAULT CURRENT_DATE,
                               return_dead_line DATE,
                               is_returned      BOOLEAN DEFAULT FALSE,
                               version          BIGINT  DEFAULT 0,

                               CONSTRAINT fk_borrow_book   FOREIGN KEY (book_id)   REFERENCES books(id),
                               CONSTRAINT fk_borrow_reader FOREIGN KEY (reader_id) REFERENCES readers(id)
);