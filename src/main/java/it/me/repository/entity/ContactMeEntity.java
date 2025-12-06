package it.me.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.ZonedDateTime;

@Entity
@Table(name = "contact_me")
@NamedQueries({
        @NamedQuery(
                name = ContactMeEntity.READ_ALL_BY_STATUS_PENDING,
                query = "SELECT cme FROM ContactMeEntity cme WHERE cme.status = 'PENDING' ORDER BY cme.createdAt ASC"
        ),
        @NamedQuery(
                name = ContactMeEntity.COUNT_BY_EMAIL_AND_STATUS_PENDING,
                query = "SELECT COUNT(cme) FROM ContactMeEntity cme WHERE cme.email = :email and cme.status = :status"
        )
})
public class ContactMeEntity {
    public static final String READ_ALL_BY_STATUS_PENDING = "ContactMe.readAllByStatusPending";
    public static final String COUNT_BY_EMAIL_AND_STATUS_PENDING = "ContactMe.countByEmailAndStatusPending";

    /**
     * Represent the status of a ContactMe record of DB. Actual mapped status are
     * {@code PENDING}, {@code PROCESSED}, {@code ERROR}
     */
    public enum Status {
        PENDING, PROCESSED, ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String message;

    @NotNull(message = "Contact back flag is required")
    @Column(name = "contact_back")
    private Boolean contactBack = false;

    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(columnDefinition = "contact_me_status")
    private Status status = Status.PENDING;

    @NotNull
    @Column(nullable = false)
    private Integer attempts = 0;

    @Column(name = "last_attempt_at")
    private ZonedDateTime lastAttemptAt;

    @Column(name = "error_reason", columnDefinition = "TEXT")
    private String errorReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    public Long id() {
        return id;
    }

    public ContactMeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String email() {
        return email;
    }

    public ContactMeEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String name() {
        return name;
    }

    public ContactMeEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String message() {
        return message;
    }

    public ContactMeEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public Boolean contactBack() {
        return contactBack;
    }

    public ContactMeEntity setContactBack(Boolean contactBack) {
        this.contactBack = contactBack;
        return this;
    }

    public Status status() {
        return status;
    }

    public ContactMeEntity setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Integer attempts() {
        return attempts;
    }

    public ContactMeEntity setAttempts(Integer attempts) {
        this.attempts = attempts;
        return this;
    }

    public ZonedDateTime lastAttemptAt() {
        return lastAttemptAt;
    }

    public ContactMeEntity setLastAttemptAt(ZonedDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
        return this;
    }

    public String errorReason() {
        return errorReason;
    }

    public ContactMeEntity setErrorReason(String errorReason) {
        this.errorReason = errorReason;
        return this;
    }

    public ZonedDateTime createdAt() {
        return createdAt;
    }

    public ContactMeEntity setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime updatedAt() {
        return updatedAt;
    }

    public ContactMeEntity setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}