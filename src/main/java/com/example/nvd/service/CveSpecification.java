package com.example.nvd.service;

import com.example.nvd.model.CveData;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CveSpecification {

    public static Specification<CveData> filterBy(Integer year, Double score, Integer days) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (year != null) {
                predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("published")), year));
            }

            if (score != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("base_score"), score));
            }

            if (days != null) {
                Timestamp pastDate = Timestamp.from(Instant.now().minusSeconds(days * 86400L));
                predicates.add(cb.greaterThanOrEqualTo(root.get("last_modified"), pastDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
