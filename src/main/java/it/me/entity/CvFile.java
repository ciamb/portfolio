package it.me.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.SQLType;
import java.time.ZonedDateTime;

@Entity
@Table(
        name = "cv_file",
        indexes = {
                @Index(name = "uq_single_active_cv", columnList = "is_active")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_cv_file_sha256", columnNames = {"sha256"})
        }
)
@NamedQuery(
        name = CvFile.READ_BY_SHA256,
        query = " select cv from CvFile cv " +
                " where cv.sha256 = :sha256 "
)
@NamedQuery(
        name = CvFile.UPDATE_IS_ACTIVE_TO_FALSE_IF_ANY,
        query = " update CvFile cv set cv.isActive = false," +
                " cv.updatedAt = :updatedAt" +
                " where cv.isActive = true "
)
@NamedQuery(
        name = CvFile.READ_BY_IS_ACTIVE,
        query = " select cv from CvFile cv" +
                " where cv.is_active = true"
)
public class CvFile {
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
    public byte[] fileData;

    @Column(name = "filesize_bytes", insertable = false, updatable = false)
    public Long filesizeBytes;

    @Column(nullable = false, length = 64)
    public String sha256;

    @Column(nullable = false, name = "is_active")
    public boolean isActive = false;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    public ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    public ZonedDateTime updatedAt;

    public Long id() {
        return id;
    }

    public CvFile setId(Long id) {
        this.id = id;
        return this;
    }

    public String filename() {
        return filename;
    }

    public CvFile setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public String contentType() {
        return contentType;
    }

    public CvFile setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] fileData() {
        return fileData;
    }

    public CvFile setFileData(byte[] fileData) {
        this.fileData = fileData;
        return this;
    }

    // only gettable
    public Long filesizeBytes() {
        return filesizeBytes;
    }

    public CvFile setFilesizeBytes(Long filesizeBytes) {
        this.filesizeBytes = filesizeBytes;
        return this;
    }

    public String sha256() {
        return sha256;
    }

    public CvFile setSha256(String sha256) {
        this.sha256 = sha256;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public CvFile setActive(boolean active) {
        isActive = active;
        return this;
    }

    public ZonedDateTime createdAt() {
        return createdAt;
    }

    public CvFile setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime updatedAt() {
        return updatedAt;
    }

    public CvFile setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
