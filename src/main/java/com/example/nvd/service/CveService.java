package com.example.nvd.service;

import com.example.nvd.model.CveData;
import com.example.nvd.repository.CveRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CveService {

    @Autowired
    private CveRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Page<CveData> getCves(Integer limit, Integer offset, Integer year, Double score, Integer days, String sortBy, String order) {

        // Map API field names (snake_case) to entity property names (camelCase)
        Map<String, String> fieldMap = Map.of(
                "cve_id", "cveId",
                "published", "published",
                "last_modified", "lastModified",
                "base_score", "baseScore"
        );

        // Translate frontend snake_case to entity camelCase for sorting
        sortBy = fieldMap.getOrDefault(sortBy, sortBy);

        Pageable pageable = PageRequest.of(
                offset / limit,
                limit,
                Sort.by(Sort.Direction.fromString(order), sortBy)
        );

        Page<CveData> page = repository.findAll(CveSpecification.filterBy(year, score, days), pageable);

        // Fix configurations JSON string if needed
        for (CveData cve : page.getContent()) {
            Object configs = cve.getConfigurations();

            if (configs != null && configs instanceof String) {
                try {
                    List<Map<String, Object>> parsedConfigs = objectMapper.readValue(
                            (String) configs,
                            new TypeReference<List<Map<String, Object>>>() {}
                    );
                    cve.setConfigurations(parsedConfigs);
                } catch (Exception e) {
                    System.err.println("Failed to parse configurations JSON for CVE " + cve.getCveId() + ": " + e.getMessage());
                }
            }
        }

        return page;
    }


    public Optional<CveData> getCveById(String id) {
        return repository.findById(id);
    }
}
