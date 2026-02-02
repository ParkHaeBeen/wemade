package com.wemade.infrastructure.persistence;

import com.wemade.domain.Analysis;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AnalysisRepository {
    private final ConcurrentHashMap<String, Analysis> store = new ConcurrentHashMap<>();

    public void save(Analysis analysis) {
        store.put(analysis.getId(), analysis);
    }

    public Optional<Analysis> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
