package com.codeassessment.ledgerassement.app.excutor.service.repository;

import com.codeassessment.ledgerassement.app.excutor.util.DateConverter;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.codeassessment.ledgerassement.app.excutor.util.DateRange;

@SuppressWarnings({"unused"})
@Repository
public class PostingsEntryRepository {

  private final CassandraSessionProvider cassandraSessionProvider;
  private final TenantAwareCassandraMapperProvider tenantAwareCassandraMapperProvider;
  private final TenantAwareEntityTemplate tenantAwareEntityTemplate;

  @Autowired
  public PostingsEntryRepository(final CassandraSessionProvider cassandraSessionProvider,
                                final TenantAwareCassandraMapperProvider tenantAwareCassandraMapperProvider,
                                final TenantAwareEntityTemplate tenantAwareEntityTemplate) {
    super();
    this.cassandraSessionProvider = cassandraSessionProvider;
    this.tenantAwareCassandraMapperProvider = tenantAwareCassandraMapperProvider;
    this.tenantAwareEntityTemplate = tenantAwareEntityTemplate;
  }

  public void saveJournalEntry(final PostingsEntryEntity journalEntryEntity) {
    this.tenantAwareEntityTemplate.save(journalEntryEntity);

    final PostingsEntryLookup journalEntryLookup = new PostingsEntryLookup();
    journalEntryLookup.setTransactionIdentifier(journalEntryEntity.getTransactionIdentifier());
    journalEntryLookup.setDateBucket(journalEntryEntity.getDateBucket());
    this.tenantAwareEntityTemplate.save(journalEntryLookup);
  }

  public List<PostingsEntryEntity> fetchJournalEntries(final DateRange range) {
    final Session tenantSession = this.cassandraSessionProvider.getTenantSession();

    final List<String> datesInBetweenRange
        = range.stream()
        .map(DateConverter::toIsoString)
        .collect(Collectors.toList());

    final ResultSet resultSet = tenantSession.execute(QueryBuilder
            .select()
            .all()
            .from("thoth_journal_entries")
            .where(QueryBuilder.in("date_bucket", datesInBetweenRange))
            .getQueryString(), datesInBetweenRange.toArray()
    );

    final Mapper<PostingsEntryEntity> mapper = this.tenantAwareCassandraMapperProvider.getMapper(PostingsEntryEntity.class);
    final Result<PostingsEntryEntity> journalEntryEntities = mapper.map(resultSet);
    return journalEntryEntities.all();
  }

  public Optional<PostingsEntryEntity> findJournalEntry(final String transactionIdentifier) {
    final Optional<PostingsEntryLookup> optionalJournalEntryLookup = this.tenantAwareEntityTemplate.findById(PostingsEntryLookup.class, transactionIdentifier);
    if (optionalJournalEntryLookup.isPresent()) {
      final PostingsEntryLookup journalEntryLookup = optionalJournalEntryLookup.get();
      final List<PostingsEntryEntity> journalEntryEntities = this.tenantAwareEntityTemplate.fetchByKeys(PostingsEntryEntity.class, journalEntryLookup.getDateBucket(),
          journalEntryLookup.getTransactionIdentifier());
      return Optional.of(journalEntryEntities.get(0));
    } else {
      return Optional.empty();
    }
  }
}
