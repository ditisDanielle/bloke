-- before create tables and indexes, drop all

-- drop tables in reverse order because data relates to each other --
DROP TABLE IF EXISTS public.money_transaction CASCADE;
DROP TABLE IF EXISTS public.order_line CASCADE;
DROP TABLE IF EXISTS public.order CASCADE;
DROP TABLE IF EXISTS public.photo CASCADE;
DROP TABLE IF EXISTS public.wine CASCADE;
DROP TABLE IF EXISTS public.email CASCADE;
DROP TABLE IF EXISTS public.user CASCADE;

-- Create tables --

-- user --
CREATE TABLE public.user (
  id BIGSERIAL,
  type VARCHAR (50) NOT NULL,
  email VARCHAR (50) NOT NULL,
  full_name VARCHAR (50),
  password VARCHAR (100),
  active BOOLEAN NOT NULL,
  street VARCHAR (50),
  street_number VARCHAR (50),
  city VARCHAR (50),
  postal_code VARCHAR (50),
  invoice_street VARCHAR (50),
  invoice_street_number VARCHAR (50),
  invoice_city VARCHAR (50),
  invoice_postal_code VARCHAR (50),
  balance NUMERIC(19,2)
);
ALTER TABLE public.user
  ADD CONSTRAINT pk_user_id PRIMARY KEY (id);
CREATE UNIQUE INDEX ix_user_email ON public.user(email);

-- email --
CREATE TABLE public.email (
  id BIGSERIAL,
  sent BOOLEAN NOT NULL,
  created TIMESTAMP NOT NULL,
  sent_on TIMESTAMP,
  customer_id BIGINT NOT NULL,
  email_from VARCHAR(50) NOT NULL,
  email_to VARCHAR(50) NOT NULL,
  title VARCHAR(128) NOT NULL,
  message VARCHAR(256) NOT NULL
);
ALTER TABLE public.email
  ADD CONSTRAINT pk_email_id PRIMARY KEY (id);
ALTER TABLE public.email
  ADD CONSTRAINT fk_email_customer_id FOREIGN KEY (customer_id) REFERENCES public.email(id);
  
-- wine --
CREATE TABLE public.wine (
  id BIGSERIAL,
  country VARCHAR(32),
  region VARCHAR(32),
  wine_type VARCHAR(32),
  name VARCHAR (50) NOT NULL,
  cost NUMERIC(19,2),
  year INT4
);
ALTER TABLE public.wine
  ADD CONSTRAINT pk_wine_id PRIMARY KEY (id);
CREATE UNIQUE INDEX ix_wine_name ON public.wine(name);

-- photo --
CREATE TABLE public.photo (
    id BIGSERIAL,
    wine_id BIGINT NOT NULL,
    content_type VARCHAR(32) NOT NULL,
    content bytea NOT NULL
);
ALTER TABLE public.photo
  ADD CONSTRAINT pk_photo_id PRIMARY KEY (id);
ALTER TABLE public.photo
  ADD CONSTRAINT fk_photo_wine_id FOREIGN KEY (wine_id) REFERENCES public.wine(id);
CREATE INDEX ix_photo_wine_id ON public.photo(wine_id);

-- order --
CREATE TABLE public.order (
  id BIGSERIAL,
  date TIMESTAMP NOT NULL,
  status VARCHAR (50) NOT NULL,
  customer_id BIGINT NOT NULL
);
ALTER TABLE public.order
  ADD CONSTRAINT pk_order_id PRIMARY KEY (id);
ALTER TABLE public.order
  ADD CONSTRAINT fk_order_customer_id FOREIGN KEY (customer_id) REFERENCES public.user(id);
CREATE INDEX ix_order_customer_id ON public.order(customer_id);

-- order_line --
CREATE TABLE public.order_line (
  id BIGSERIAL,
  order_id BIGINT NOT NULL,
  wine_id BIGINT NOT NULL,
  cost NUMERIC(19,2),
  quantity INTEGER
);
ALTER TABLE public.order_line
  ADD CONSTRAINT pk_order_line_id PRIMARY KEY (id);
ALTER TABLE public.order_line
  ADD CONSTRAINT fk_order_line_order_id FOREIGN KEY (order_id) REFERENCES public.order(id);
ALTER TABLE public.order_line
  ADD CONSTRAINT fk_order_line_wine_id FOREIGN KEY (wine_id) REFERENCES public.wine(id);
CREATE INDEX ix_order_line_order_id ON public.order_line(order_id);
CREATE INDEX ix_order_line_wine_id ON public.order_line(wine_id);

-- money_transaction --
CREATE TABLE public.money_transaction (
    id BIGSERIAL,
    customer_id BIGINT NOT NULL,
    date TIMESTAMP NOT NULL,
    amount NUMERIC(19,2),
    description VARCHAR (2500)
);
ALTER TABLE public.money_transaction
  ADD CONSTRAINT pk_money_transaction_id PRIMARY KEY (id);
ALTER TABLE public.money_transaction
  ADD CONSTRAINT fk_money_transaction_customer_id FOREIGN KEY (customer_id) REFERENCES public.user(id);
CREATE INDEX ix_money_transaction_customer_id ON public.money_transaction(customer_id);