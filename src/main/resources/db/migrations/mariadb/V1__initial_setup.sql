CREATE TABLE assessledge_ledgers (
  id               BIGINT        NOT NULL AUTO_INCREMENT,
  a_type           VARCHAR(32)   NOT NULL,
  identifier       VARCHAR(32)    NOT NULL,
  a_name           VARCHAR(256)  NOT NULL,
  description      VARCHAR(2048) NULL,
  parent_ledger_id BIGINT        NULL,
  created_on       TIMESTAMP(3)  NOT NULL,
  created_by       VARCHAR(32)    NOT NULL,
  last_modified_on TIMESTAMP(3)  NULL,
  last_modified_by VARCHAR(32)    NULL,
  CONSTRAINT assessledge_ledgers_pk PRIMARY KEY (id),
  CONSTRAINT assessledge_ledgers_identifier_uq UNIQUE (identifier),
  CONSTRAINT assessledge_ledgers_parent_fk FOREIGN KEY (parent_ledger_id) REFERENCES assessledge_ledgers (id)
);

CREATE TABLE assessledge_accounts (
  id                    BIGINT         NOT NULL AUTO_INCREMENT,
  a_type                VARCHAR(32)    NOT NULL,
  identifier            VARCHAR(32)    NOT NULL,
  a_name                VARCHAR(256)   NOT NULL,
  holders               VARCHAR(256)   NULL,
  signature_authorities VARCHAR(256)   NULL,
  balance               NUMERIC(15, 5) NOT NULL,
  reference_account_id  BIGINT         NULL,
  ledger_id             BIGINT         NOT NULL,
  a_state               VARCHAR(32)    NOT NULL,
  created_on            TIMESTAMP(3)   NOT NULL,
  created_by            VARCHAR(32)    NOT NULL,
  last_modified_on      TIMESTAMP(3)   NULL,
  last_modified_by      VARCHAR(32)    NULL,
  CONSTRAINT assessledge_accounts_pk PRIMARY KEY (id),
  CONSTRAINT assessledge_accounts_identifier_uq UNIQUE (identifier),
  CONSTRAINT assessledge_reference_accounts_fk FOREIGN KEY (reference_account_id) REFERENCES assessledge_accounts (id),
  CONSTRAINT assessledge_accounts_ledgers_fk FOREIGN KEY (ledger_id) REFERENCES assessledge_ledgers (id)
);

CREATE TABLE assessledge_account_entries (
  id               BIGINT         NOT NULL AUTO_INCREMENT,
  account_id       BIGINT         NULL,
  a_type           VARCHAR(32)    NOT NULL,
  transaction_date TIMESTAMP(3)   NOT NULL,
  message          VARCHAR(2048)  NULL,
  amount           NUMERIC(15, 5) NOT NULL,
  balance          NUMERIC(15, 5) NOT NULL,
  CONSTRAINT assessledge_account_entries_pk PRIMARY KEY (id),
  CONSTRAINT assessledge_account_entries_accounts_fk FOREIGN KEY (account_id) REFERENCES assessledge_accounts (id)
);

CREATE TABLE assessledge_commands (
  id         BIGINT       NOT NULL AUTO_INCREMENT,
  account_id BIGINT       NOT NULL,
  a_type     VARCHAR(32)  NOT NULL,
  a_comment  VARCHAR(32)  NULL,
  created_by VARCHAR(32)   NOT NULL,
  created_on TIMESTAMP(3) NULL,
  CONSTRAINT assessledge_commands_pk PRIMARY KEY (id),
  CONSTRAINT assessledge_commands_accounts_fk FOREIGN KEY (account_id) REFERENCES assessledge_accounts (id)
    ON UPDATE RESTRICT
);