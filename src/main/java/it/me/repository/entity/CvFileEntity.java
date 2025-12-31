package it.me.repository.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
        name = "cv_file",
        indexes = {@Index(name = "uq_single_active_cv", columnList = "is_active")},
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uq_cv_file_sha256",
                    columnNames = {"sha256"})
        })
@NamedQuery(
        name = CvFileEntity.READ_BY_SHA256,
        query = " select cve from CvFileEntity cve " + " where cve.sha256 = :sha256 ")
@NamedQuery(
        name = CvFileEntity.UPDATE_IS_ACTIVE_TO_FALSE_IF_ANY,
        query = " update CvFileEntity cve set cve.isActive = false,"
                + " cve.updatedAt = :updatedAt"
                + " where cve.isActive = true ")
@NamedQuery(
        name = CvFileEntity.READ_BY_IS_ACTIVE,
        query = " select cve from CvFileEntity cve" + " where cve.isActive = true ")
public class CvFileEntity {
    public static final String READ_BY_SHA256 = "CvFile.readBySha256";
    public static final String UPDATE_IS_ACTIVE_TO_FALSE_IF_ANY = "CvFile.updateIsActiveToFalseIfAny";
    public static final String READ_BY_IS_ACTIVE = "CvFile.readByIsActive";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType = "application/pdf";

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "filesize_bytes", insertable = false, updatable = false)
    private Long filesizeBytes;

    @Column(nullable = false, length = 64)
    private String sha256;

    @Column(nullable = false, name = "is_active")
    private boolean isActive = false;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private ZonedDateTime updatedAt;

    public Long id() {
        return id;
    }

    public CvFileEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String filename() {
        return filename;
    }

    public CvFileEntity setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String contentType() {
        return contentType;
    }

    public CvFileEntity setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] fileData() {
        return fileData;
    }

    public CvFileEntity setFileData(byte[] fileData) {
        this.fileData = fileData;
        return this;
    }

    // only gettable
    public Long filesizeBytes() {
        return filesizeBytes;
    }

    public CvFileEntity setFilesizeBytes(Long filesizeBytes) {
        this.filesizeBytes = filesizeBytes;
        return this;
    }

    public String sha256() {
        return sha256;
    }

    public CvFileEntity setSha256(String sha256) {
        this.sha256 = sha256;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public CvFileEntity setIsActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public ZonedDateTime createdAt() {
        return createdAt;
    }

    public CvFileEntity setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime updatedAt() {
        return updatedAt;
    }

    public CvFileEntity setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
