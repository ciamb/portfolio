package it.me.domain.dto;

import lombok.Builder;

@Builder
public record ContactMeBatchConfig(Integer id, Boolean isActive, String targetEmail) {
    public ContactMeBatchConfig activate() {
        return ContactMeBatchConfig.builder()
                .id(this.id)
                .isActive(true)
                .targetEmail(this.targetEmail)
                .build();
    }
}
