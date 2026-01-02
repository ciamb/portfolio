package it.me.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "contact_me_batch_config")
@NamedQueries({
    @NamedQuery(
            name = ContactMeBatchConfigEntity.READ_BY_ID,
            query = " select cmbce from ContactMeBatchConfigEntity as cmbce " + " where cmbce.id = :id "),
    @NamedQuery(
            name = ContactMeBatchConfigEntity.UPDATE_BY_ID,
            query = " update ContactMeBatchConfigEntity cmbce set cmbce.isActive = :isActive where cmbce.id = 1")
})
public class ContactMeBatchConfigEntity {
    public static final String READ_BY_ID = "ContactMeBatchConfig.readById";
    public static final String UPDATE_BY_ID = "ContactMeBatchConfig.updateById";

    @Id
    private Integer id;

    @NotNull @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull @Column(name = "target_email", nullable = false)
    private String targetEmail;

    public Integer id() {
        return id;
    }

    public ContactMeBatchConfigEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean isActive() {
        return isActive;
    }

    public ContactMeBatchConfigEntity setIsActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public String targetEmail() {
        return targetEmail;
    }

    public ContactMeBatchConfigEntity setTargetEmail(String targetEmail) {
        this.targetEmail = targetEmail;
        return this;
    }
}
