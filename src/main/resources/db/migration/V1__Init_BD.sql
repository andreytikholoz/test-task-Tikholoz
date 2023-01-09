CREATE TABLE user_entity_t01 (
        uuid uuid NOT NULL,
        avatar_base64 clob,
        email varchar2(63 CHAR) UNIQUE,
        password_hash varchar2(255 CHAR),
        PRIMARY KEY (uuid)
    );

CREATE INDEX user_entity_email_index ON user_entity_t01 (email);

CREATE INDEX user_entity_email_and_password_index ON user_entity_t01 (email, password_hash);
