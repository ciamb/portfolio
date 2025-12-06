package it.me.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

@Entity
@Table(name = "contact_me_batch_log")
public class ContactMeBatchLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "run_at", nullable = false)
    private ZonedDateTime runAt;

    @NotNull
    @Column(name = "processed", nullable = false)
    private Integer processed;

    @NotNull
    @Column(name = "with_error", nullable = false)
    private Integer withError;

    @Size(max = 255)
    @Column(name = "sent_to")
    private String sentTo;

    public Long id() {
        return id;
    }

    public ContactMeBatchLogEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime runAt() {
        return runAt;
    }

    public ContactMeBatchLogEntity setRunAt(ZonedDateTime runAt) {
        this.runAt = runAt;
        return this;
    }

    public Integer processed() {
        return processed;
    }

    public ContactMeBatchLogEntity setProcessed(Integer processed) {
        this.processed = processed;
        return this;
    }

    public Integer withError() {
        return withError;
    }

    public ContactMeBatchLogEntity setWithError(Integer withError) {
        this.withError = withError;
        return this;
    }

    public String sentTo() {
        return sentTo;
    }

    public ContactMeBatchLogEntity setSentTo(String sentTo) {
        this.sentTo = sentTo;
        return this;
    }
}
