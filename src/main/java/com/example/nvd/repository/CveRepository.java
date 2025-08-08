package com.example.nvd.repository;

import com.example.nvd.model.CveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CveRepository extends JpaRepository<CveData, String>, JpaSpecificationExecutor<CveData> {
}
