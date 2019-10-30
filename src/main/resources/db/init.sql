CREATE SCHEMA IF NOT EXISTS bank;

CREATE TABLE account (
  account_number bigint auto_increment NOT NULL,
  balance decimal (19,2) NOT NULL,
  CONSTRAINT pk_account PRIMARY KEY (account_number),
);

CREATE TABLE operation (
  id bigint auto_increment NOT NULL,
  account_from bigint,
  account_to bigint,
  type varchar(20) NOT NULL,
  amount decimal (19,2) NOT NULL,
  CONSTRAINT pk_operation PRIMARY KEY (id),
  CONSTRAINT fk_account_from FOREIGN KEY (account_from) REFERENCES account (account_number),
  CONSTRAINT fk_account_to FOREIGN KEY (account_to) REFERENCES account (account_number)
);

CREATE INDEX account_from_idx ON operation(account_from);
CREATE INDEX account_to_idx ON operation(account_to);