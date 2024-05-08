package com.codeassessment.ledgerassement.app.excutor.service.repository;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "assessledge_journal_entry_lookup")
public class PostingsEntryLookup {

  @PartitionKey
  @Column(name = "transaction_identifier")
  private String transactionIdentifier;
  @Column(name = "date_bucket")
  private String dateBucket;

  public PostingsEntryLookup() {
    super();
  }

  public String getTransactionIdentifier() {
    return this.transactionIdentifier;
  }

  public void setTransactionIdentifier(final String transactionIdentifier) {
    this.transactionIdentifier = transactionIdentifier;
  }

  public String getDateBucket() {
    return this.dateBucket;
  }

  public void setDateBucket(final String dateBucket) {
    this.dateBucket = dateBucket;
  }
}
