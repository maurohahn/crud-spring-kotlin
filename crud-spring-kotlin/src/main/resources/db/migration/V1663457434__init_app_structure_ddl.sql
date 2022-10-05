-- 17/09/2022
---------------------------------------------------------------------

-- sequences

create sequence products_seq
  increment 1
  minvalue 1
  -- max value bigint = 9223372036854775807
  maxvalue 9223372036854775807
  start 1
  cache 1;

create sequence customers_seq
  increment 1
  minvalue 1
  -- max value bigint = 9223372036854775807
  maxvalue 9223372036854775807
  start 1
  cache 1;

create sequence orders_seq
  increment 1
  minvalue 1
  -- max value bigint = 9223372036854775807
  maxvalue 9223372036854775807
  start 1
  cache 1;

create sequence order_items_seq
  increment 1
  minvalue 1
  -- max value bigint = 9223372036854775807
  maxvalue 9223372036854775807
  start 1
  cache 1;

---------------------------------------------------------------------
-- tables

create table products (
	id bigint primary key default nextval('products_seq'),
	description varchar not null,
	price real not null,
	brand varchar not null,
	quantity int not null,
	category varchar,
	is_active boolean not null,
    created_at timestamp not null default current_timestamp,
	updated_at timestamp not null,
	constraint products_description_unique unique (description)
);

create table customers (
	id bigint primary key default nextval('customers_seq'),
	name varchar,
	cpf varchar not null,
	email varchar not null,
	address varchar,
	city varchar,
	cep varchar,
	uf varchar,
	is_active boolean not null,
    created_at timestamp not null default current_timestamp,
	updated_at timestamp not null,
	constraint customers_email_unique unique (email),
	constraint customers_cpf_unique unique (cpf)
);

create table orders (
    id bigint primary key default nextval('orders_seq'),
    customer_id bigint not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null,
    constraint orders_customer_id_fkey foreign key (customer_id) references customers(id) on delete restrict on update cascade
);

create table order_items (
    id bigint primary key default nextval('order_items_seq'),
	order_id bigint,
	product_id bigint not null,
	quantity int not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null,
    constraint order_items_order_id_fkey foreign key (order_id) references orders(id) on delete restrict on update cascade,
    constraint order_items_product_id_fkey foreign key (product_id) references products(id) on delete restrict on update cascade
);

