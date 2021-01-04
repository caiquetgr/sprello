CREATE TABLE task_status
(
    id          integer                NOT NULL,
    name        character varying(100) NOT NULL,
    description character varying(200) NOT NULL,
    CONSTRAINT task_status_pkey PRIMARY KEY (id)
);

INSERT INTO task_status
VALUES (1, 'To do', 'Task is awaiting to be started'),
       (2, 'Progress', 'Task is in progress'),
       (3, 'Revision', 'Task in done but still needs to be revised'),
       (4, 'Done', 'Task has already been finished'),
       (5, 'Cancelled', 'Task has been cancelled');

CREATE TABLE tasks
(
    id            SERIAL,
    board         integer                  NOT NULL,
    name          character varying(100)   NOT NULL,
    description   character varying(500)   NOT NULL,
    task_status   integer                  NOT NULL,
    creation_date timestamp with time zone NOT NULL,
    modified_date timestamp with time zone NOT NULL,
    created_by    integer                  NOT NULL,
    modified_by   integer                  NOT NULL,
    deleted       boolean                  NOT NULL DEFAULT FALSE,
    CONSTRAINT tasks_pkey PRIMARY KEY (id),
    CONSTRAINT tasks_board_fkey_boards FOREIGN KEY (board) REFERENCES boards (id),
    CONSTRAINT tasks_created_by_fkey_users FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT tasks_modified_by_fkey_users FOREIGN KEY (modified_by) REFERENCES users (id),
    CONSTRAINT tasks_task_status_fkey_task_status FOREIGN KEY (task_status) REFERENCES task_status (id)
);
