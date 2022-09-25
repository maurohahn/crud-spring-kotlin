-- 16/09/2022
---------------------------------------------------------------------

-- sequences

create sequence users_seq
  increment 1
  minvalue 1
  -- max value bigint = 9223372036854775807
  maxvalue 9223372036854775807
  start 1
  cache 1;

create sequence groups_seq
  increment 1
  minvalue 1
  -- max value bigint = 9223372036854775807
  maxvalue 9223372036854775807
  start 1
  cache 1;

create sequence permissions_seq
  increment 1
  minvalue 1
  -- max value bigint = 9223372036854775807
  maxvalue 9223372036854775807
  start 1
  cache 1;

---------------------------------------------------------------------
-- tables

create table users (
	id bigint primary key default nextval('users_seq'),
	name varchar,
	password varchar not null,
	email varchar not null,
	is_active boolean not null,
    created_at timestamp not null default current_timestamp,
	updated_at timestamp not null,
	last_login_at timestamp,
	constraint users_email_unique unique (email)
);

create table groups (
	id bigint primary key default nextval('groups_seq'),
	group_name varchar,
	is_active boolean not null,
    created_at timestamp not null default current_timestamp,
	updated_at timestamp not null,
    constraint groups_group_name_unique unique (group_name)
);

create table users_groups (
	user_id bigint not null,
	group_id bigint not null,
    created_at timestamp not null default current_timestamp,
    constraint users_groups_user_id_fkey foreign key (user_id) references users(id) on delete restrict on update cascade,
    constraint users_groups_group_id_fkey foreign key (group_id) references groups(id) on delete restrict on update cascade,
    constraint users_groups_user_id_group_id_unique unique (user_id,group_id)
);

create table permissions (
	id bigint primary key default nextval('permissions_seq'),
	permission_name varchar,
	is_default boolean not null,
	is_active boolean not null,
    created_at timestamp not null default current_timestamp,
	updated_at timestamp not null,
    constraint permissions_permission_name_unique unique (permission_name)
);

create table permissions_groups (
	permission_id bigint not null,
	group_id bigint not null,
    created_at timestamp not null default current_timestamp,
    constraint permissions_groups_permission_id_fkey foreign key (permission_id) references permissions(id) on delete restrict on update cascade,
    constraint permissions_groups_group_id_fkey foreign key (group_id) references groups(id) on delete restrict on update cascade,
    constraint permissions_groups_permission_id_group_id_unique unique (permission_id,group_id)
);
