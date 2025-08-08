package com.example.nvd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "cvesdata")
public class CveData {

    @Id
    @Column(name = "cve_id")
    @JsonProperty("cve_id")
    private String cveId;

    @Column(name = "source_identifier")
    @JsonProperty("source_identifier")
    private String sourceIdentifier;

    @Column(name = "published")
    private Timestamp published;

    @Column(name = "last_modified")
    @JsonProperty("last_modified")
    private Timestamp lastModified;

    @Column(name = "vuln_status")
    @JsonProperty("vuln_status")
    private String vulnStatus;

    // Store as JSONB array of objects
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "descriptions", columnDefinition = "jsonb")
    private List<Map<String, Object>> descriptions;

    @Column(name = "base_score")
    @JsonProperty("base_score")
    private Double baseScore;

    @Column(name = "severity")
    private String severity;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "weaknesses", columnDefinition = "jsonb")
    private List<Map<String, Object>> weaknesses;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configurations", columnDefinition = "jsonb")
    private List<Map<String, Object>> configurations;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "reference_urls", columnDefinition = "jsonb")
    @JsonProperty("reference_urls")
    private List<String> referenceUrls;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_data", columnDefinition = "jsonb")
    @JsonProperty("raw_data")
    private Map<String, Object> rawData;

    private String description;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    // ============== GETTERS & SETTERS ==============

    public String getCveId() {
        return cveId;
    }

    public void setCveId(String cveId) {
        this.cveId = cveId;
    }

    public String getSourceIdentifier() {
        return sourceIdentifier;
    }

    public void setSourceIdentifier(String sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
    }

    public Timestamp getPublished() {
        return published;
    }

    public void setPublished(Timestamp published) {
        this.published = published;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    public String getVulnStatus() {
        return vulnStatus;
    }

    public void setVulnStatus(String vulnStatus) {
        this.vulnStatus = vulnStatus;
    }

    public List<Map<String, Object>> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Map<String, Object>> descriptions) {
        this.descriptions = descriptions;
    }

    public Double getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(Double baseScore) {
        this.baseScore = baseScore;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<Map<String, Object>> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<Map<String, Object>> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<Map<String, Object>> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Map<String, Object>> configurations) {
        this.configurations = configurations;
    }

    public List<String> getReferenceUrls() {
        return referenceUrls;
    }

    public void setReferenceUrls(List<String> referenceUrls) {
        this.referenceUrls = referenceUrls;
    }

    public Map<String, Object> getRawData() {
        return rawData;
    }

    public void setRawData(Map<String, Object> rawData) {
        this.rawData = rawData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
