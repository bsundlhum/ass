ALTER TABLE assessledge_ledgers ADD COLUMN show_accounts_in_chart BOOLEAN NOT NULL DEFAULT 1;

ALTER TABLE assessledge_ledgers ALTER COLUMN show_accounts_in_chart DROP DEFAULT;