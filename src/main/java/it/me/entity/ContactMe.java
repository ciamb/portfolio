package it.me.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

@Entity
@Table(name = "contact_me")
@NamedQueries({
        @NamedQuery(
                name = ContactMe.FIND_BY_STATUS,
                query = "SELECT cm FROM ContactMe cm WHERE cm.status = :status ORDER BY cm.createdAt DESC"
        ),
        @NamedQuery(
                name = ContactMe.FIND_PENDING,
                query = "SELECT cm FROM ContactMe cm WHERE cm.status = 'PENDING' ORDER BY cm.createdAt ASC"
        ),
        @NamedQuery(
                name = ContactMe.COUNT_BY_STATUS,
                query = "SELECT COUNT(cm) FROM ContactMe cm WHERE cm.status = :status"
        )
})
public class ContactMe {

    // Named query constants
    public static final String FIND_BY_STATUS = "ContactMe.findByStatus";
    public static final String FIND_PENDING = "ContactMe.findPending";
    public static final String COUNT_BY_STATUS = "ContactMe.countByStatus";

    // Status enum
    public enum Status {
        PENDING, PROCESSED, ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String message;

    @NotNull(message = "Contact back flag is required")
    @Column(name = "contact_back")
    private Boolean contactBack = false;

    @Enumerated(EnumType.STRING)
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

    // PrePersist callback to set creation timestamp
    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    // PreUpdate callback to update timestamp
    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }

    // Constructors
    public ContactMe() {

    }

    public ContactMe(String email, String name, String message, Boolean contactBack) {
        this.email = email;
        this.name = name;
        this.message = message;
        this.contactBack = contactBack;
    }

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

    // Helper methods for business logic
    public ContactMe incrementAttempts() {
        this.attempts++;
        this.lastAttemptAt = ZonedDateTime.now();
        return this;
    }

    public ContactMe markAsProcessed() {
        this.status = Status.PROCESSED;
        return this;
    }

    public ContactMe markAsError(String errorReason) {
        this.status = Status.ERROR;
        this.errorReason = errorReason;
        return this;
    }
}