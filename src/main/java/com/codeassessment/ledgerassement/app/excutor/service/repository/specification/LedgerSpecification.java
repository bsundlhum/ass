package com.codeassessment.ledgerassement.app.excutor.service.repository.specification;

import com.codeassessment.ledgerassement.app.excutor.service.repository.LedgerEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

public class LedgerSpecification {

  private LedgerSpecification() {
    super();
  }

  public static Specification<LedgerEntity> createSpecification(
      final boolean includeSubLedger, final String term, final String type) {
    return (root, query, cb) -> {

      final ArrayList<Predicate> predicates = new ArrayList<>();

      if (!includeSubLedger) {
        predicates.add(
            cb.isNull(root.get("parentLedger"))
        );
      }

      if (term != null) {
        final String likeExpression = "%" + term + "%";

        predicates.add(
            cb.or(
                cb.like(root.get("identifier"), likeExpression),
                cb.like(root.get("name"), likeExpression)
            )
        );
      }

      if (type != null) {
        predicates.add(cb.equal(root.get("type"), type));
      }

      return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    };
  }
}
