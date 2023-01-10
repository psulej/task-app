-- TASKS

create sequence tasks_seq start with 1 increment by 1;
-- drop sequence tasks_seq;
-- drop table tasks;

CREATE TABLE tasks(
                      id bigint primary key,
                      title varchar(25) not null,
                      content varchar(1500) not null,
                      date_time timestamp not null,
                      user_id bigint not null
);

-- select * from tasks;
-- SELECT count(*) FROM tasks WHERE 1 = 1;

-- USERS

create sequence users_seq start with 1 increment by 1;

CREATE TABLE users(
                      id bigint primary key,
                      login varchar(15) not null,
                      password varchar(100) not null,
                      email varchar(50) not null
);

-- drop sequence users_seq;
-- drop table users;
-- select * from users;