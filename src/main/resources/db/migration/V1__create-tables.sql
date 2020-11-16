CREATE TABLE boards
(
    id   SERIAL,
    name character varying(50) NOT NULL,
    CONSTRAINT boards_pkey PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            SERIAL,
    email         character varying        NOT NULL UNIQUE,
    first_name    character varying(80)    NOT NULL,
    last_name     character varying(300)   NOT NULL,
    password      character varying        NOT NULL,
    creation_date timestamp with time zone NOT NULL,
    active        boolean                  NOT NULL DEFAULT TRUE
);
