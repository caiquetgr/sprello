CREATE TABLE users
(
    id            SERIAL,
    email         character varying        NOT NULL UNIQUE,
    first_name    character varying(80)    NOT NULL,
    last_name     character varying(300)   NOT NULL,
    password      character varying        NOT NULL,
    creation_date timestamp with time zone NOT NULL,
    active        boolean                  NOT NULL DEFAULT TRUE,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE boards
(
    id            SERIAL,
    name          character varying(50)    NOT NULL,
    creation_date timestamp with time zone NOT NULL,
    modified_date timestamp with time zone NOT NULL,
    created_by    integer                  NOT NULL,
    modified_by   integer                  NOT NULL,
    deleted       boolean                  NOT NULL DEFAULT FALSE,
    CONSTRAINT boards_pkey PRIMARY KEY (id),
    CONSTRAINT boards_created_by_fkey_users FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT boards_modified_by_fkey_users FOREIGN KEY (modified_by) REFERENCES users (id)
);

