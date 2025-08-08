package com.example.nvd.controller;

import com.example.nvd.model.CveData;
import com.example.nvd.service.CveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cves")
public class CveController {

    @Autowired
    private CveService service;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getCves(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Double score,
            @RequestParam(required = false) Integer days,
            @RequestParam(defaultValue = "published") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        System.out.println("Received request parameters:");
        System.out.println("limit = " + limit);
        System.out.println("offset = " + offset);
        System.out.println("year = " + year);
        System.out.println("score = " + score);
        System.out.println("days = " + days);
        System.out.println("sortBy = " + sortBy);
        System.out.println("order = " + order);

        Page<CveData> page = service.getCves(limit, offset, year, score, days, sortBy, order);

        Map<String, Object> response = new HashMap<>();
        response.put("total", page.getTotalElements());
        response.put("cves", page.getContent());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCveById(@PathVariable String id) {
        return service.getCveById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404)
                        .body(Map.of("error", "CVE not found")));
    }


}
