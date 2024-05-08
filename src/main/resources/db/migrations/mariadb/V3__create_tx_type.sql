CREATE TABLE assessledge_tx_types (
  id               BIGINT        NOT NULL AUTO_INCREMENT,
  identifier       VARCHAR(32)   NOT NULL,
  a_name           VARCHAR(256)  NOT NULL,
  description      VARCHAR(2048) NULL,
  CONSTRAINT assessledge_tx_types_pk PRIMARY KEY (id),
  CONSTRAINT assessledge_tx_types_identifier_uq UNIQUE (identifier)
);

INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ACCC', 'Account Closing');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ACCO', 'Account Opening');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ACCT', 'Account Transfer');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ACDT', 'ACH Credit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ADBT', 'ACH Debit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ADJT', 'Adjustments');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('APAC', 'ACH Pre-Authorised');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ARET', 'ACH Return');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('AREV', 'ACH Reversal');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ARPD', 'ARP Debit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ASET', 'ACH Settlement');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ATXN', 'ACH Transaction');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('BACT', 'Branch Account Transfer');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('BBDD', 'SEPA B2B Direct Debit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('BCDP', 'Branch Deposit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('BCHQ', 'Branch Cheque');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('BCWD', 'Branch Withdrawal');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CAJT', 'Credit Adjustments');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CASH', 'Cash Letter');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CCCH', 'Certified Customer Cheque');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CCHQ', 'Cheque');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CDIS', 'Controlled Disbursement');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CDPT', 'Cash Deposit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CHRG', 'Charges');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CQRV', 'Cheque Reversal');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CRCQ', 'Crossed Cheque');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('CWDL', 'Cash Withdrawal');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('DAJT', 'Debit Adjustments');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('DDWN', 'Drawdown');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('DMCT', 'Domestic Credit Transfer');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('DSBR', 'Controlled Disbursement');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ERTA', 'Exchange Rate Adjustment');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('FEES', 'Fees');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ICCT', 'Intra Company Transfer');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('INTR', 'Interests');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('MIXD', 'Mixed Deposit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('MSCD', 'Miscellaneous Deposit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('NTAV', 'Not Available');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('OPCQ', 'Open Cheque');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ORCQ', 'Order Cheque');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('OTHR', 'Other');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('PADD', 'Pre-Authorised Direct Debit');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('PMDD', 'Direct Debit Payment');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('POSC', 'Credit Card Payment');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('POSD', 'Point-of-Sale Payment Debit Card');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('POSP', 'Point-of-Sale Payment');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('PPAY', 'Principal Payment');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('PSTE', 'Posting Error');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('RCDD', 'Reversal Due To Payment Cancellation Request');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('RIMB', 'Reimbursement');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('RPCR', 'Reversal Due To Payment Cancellation Request');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('SMRT', 'Smart-Card Payment');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('TAXE', 'Taxes');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('YTDA', 'YTD Adjustment');
INSERT INTO assessledge_tx_types (identifier, a_name) VALUES ('ZABA', 'Zero Balancing');
