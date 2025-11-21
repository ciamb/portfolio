package it.me.entity;

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
                name = ContactMe.READ_PENDING,
                query = "SELECT cm FROM ContactMe cm WHERE cm.status = 'PENDING' ORDER BY cm.createdAt ASC"
        ),
        @NamedQuery(
                name = ContactMe.COUNT_BY_EMAIL_AND_STATUS,
                query = "SELECT COUNT(cm) FROM ContactMe cm WHERE cm.email = :email and cm.status = :status"
        )
})
public class ContactMe {
    public static final String READ_PENDING = "ContactMe.readPending";
    public static final String COUNT_BY_EMAIL_AND_STATUS = "ContactMe.countByEmailAndStatus";

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

    public ContactMe setId(Long id) {
        this.id = id;
        return this;
    }

    public String email() {
        return email;
    }

    public ContactMe setEmail(String email) {
        this.email = email;
        return this;
    }

    public String name() {
        return name;
    }

    public ContactMe setName(String name) {
        this.name = name;
        return this;
    }

    public String message() {
        return message;
    }

    public ContactMe setMessage(String message) {
        this.message = message;
        return this;
    }

    public Boolean contactBack() {
        return contactBack;
    }

    public ContactMe setContactBack(Boolean contactBack) {
        this.contactBack = contactBack;
        return this;
    }

    public Status status() {
        return status;
    }

    public ContactMe setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Integer attempts() {
        return attempts;
    }

    public ContactMe setAttempts(Integer attempts) {
        this.attempts = attempts;
        return this;
    }

    public ZonedDateTime lastAttemptAt() {
        return lastAttemptAt;
    }

    public ContactMe setLastAttemptAt(ZonedDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
        return this;
    }

    public String errorReason() {
        return errorReason;
    }

    public ContactMe setErrorReason(String errorReason) {
        this.errorReason = errorReason;
        return this;
    }

    public ZonedDateTime createdAt() {
        return createdAt;
    }

    public ContactMe setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime updatedAt() {
        return updatedAt;
    }

    public ContactMe setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}