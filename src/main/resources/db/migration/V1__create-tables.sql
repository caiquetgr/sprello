CREATE TABLE boards
(
    id   SERIAL,
    name character varying(50) NOT NULL,
    CONSTRAINT boards_pkey PRIMARY KEY (id)
);
