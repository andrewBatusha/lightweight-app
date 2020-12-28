--
-- Schema settings
--
SET
statement_timeout = 0;
SET
lock_timeout = 0;
SET
client_encoding = 'UTF8';
SET
standard_conforming_strings = ON;
SET
check_function_bodies = FALSE;
SET
client_min_messages = WARNING;

--
-- TYPES
--
CREATE TYPE entity_status AS ENUM ('Active', 'Banned', 'Deleted');
CREATE TYPE user_role AS ENUM ('Admin', 'Editor', 'User');

--
-- TABLES
--
CREATE TABLE accounts
(
    id              BIGSERIAL     PRIMARY KEY,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP              DEFAULT NULL,
    status          entity_status NOT NULL DEFAULT 'Active',
    user_role       user_role     NOT NULL DEFAULT 'User',
    nick_name       VARCHAR(35)   NOT NULL,
    email           VARCHAR(19)   NOT NULL,
    email_confirmed BOOLEAN       NOT NULL DEFAULT FALSE,
    password        VARCHAR(255)  NOT NULL
);

CREATE TABLE beverages
(
    id              BIGSERIAL     PRIMARY KEY,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP              DEFAULT NULL,
    name            VARCHAR(35)   NOT NULL,
    ingredients     VARCHAR(250),
    image_url       VARCHAR(124),
    description     VARCHAR(250),
    recipe          VARCHAR(250),
    account_id      BIGSERIAL     NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE reviews
(
    id              BIGSERIAL     PRIMARY KEY,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP              DEFAULT NULL,
    description     VARCHAR(250),
    pros            VARCHAR(150),
    cons            VARCHAR(150),
    rating          SMALLINT,
    beverage_id     BIGSERIAL     NOT NULL,
    FOREIGN KEY (beverage_id) REFERENCES beverages (id)
);

--
-- Primary Keys
--

--
-- UK
--
ALTER TABLE ONLY accounts ADD CONSTRAINT uk_accounts__nick_name UNIQUE (nick_name);
ALTER TABLE ONLY accounts ADD CONSTRAINT uk_accounts__email UNIQUE (email);



