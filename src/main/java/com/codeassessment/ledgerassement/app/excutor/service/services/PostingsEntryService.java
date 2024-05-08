package com.codeassessment.ledgerassement.app.excutor.service.services;

import com.codeassessment.ledgerassement.app.excutor.service.mapper.PostingsEntryMapper;
import com.codeassessment.ledgerassement.app.excutor.service.repository.PostingsEntryEntity;
import com.codeassessment.ledgerassement.app.excutor.service.repository.PostingsEntryRepository;
import com.codeassessment.ledgerassement.app.excutor.util.DateRange;
import com.codeassessment.ledgerassement.domain.model.PostingsEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostingsEntryService {

  private final PostingsEntryRepository journalEntryRepository;

  @Autowired
  public PostingsEntryService(final PostingsEntryRepository journalEntryRepository) {
    super();
    this.journalEntryRepository = journalEntryRepository;
  }

  public List<PostingsEntry> fetchJournalEntries(final DateRange range) {
    final List<PostingsEntryEntity> journalEntryEntities =
        this.journalEntryRepository.fetchJournalEntries(range);

    if (journalEntryEntities != null) {
      return journalEntryEntities
          .stream()
          .map(PostingsEntryMapper::map)
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  public Optional<PostingsEntry> findJournalEntry(final String transactionIdentifier) {
    final Optional<PostingsEntryEntity> optionalJournalEntryEntity = this.journalEntryRepository.findJournalEntry(transactionIdentifier);

    return optionalJournalEntryEntity.map(PostingsEntryMapper::map);
  }
}
