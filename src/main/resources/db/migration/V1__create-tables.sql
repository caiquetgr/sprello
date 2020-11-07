CREATE SEQUENCE boards_id_seq;

CREATE TABLE boards
(
    id   integer                                            NOT NULL DEFAULT nextval('boards_id_seq'),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT boards_pkey PRIMARY KEY (id)
)
