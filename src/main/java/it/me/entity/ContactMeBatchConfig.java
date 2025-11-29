package it.me.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "contact_me_batch_config")
@NamedQueries({
        @NamedQuery(
                name = ContactMeBatchConfig.READ_BY_ID,
                query = " select cmbc from ContactMeBatchConfig as cmbc " +
                        " where cmbc.id = :id ")
})
public class ContactMeBatchConfig {
    public final static String READ_BY_ID = "ContactMeBatchConfig.readById";
    @Id
    private Integer id;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "target_email", nullable = false)
    private String targetEmail;

    public Integer id() {
        return id;
    }

    public ContactMeBatchConfig setId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean isActive() {
        return isActive;
    }

    public ContactMeBatchConfig setIsActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public String targetEmail() {
        return targetEmail;
    }

    public ContactMeBatchConfig setTargetEmail(String targetEmail) {
        this.targetEmail = targetEmail;
        return this;
    }
}
