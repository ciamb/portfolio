package it.me.repository.contact.me.mapper;

import it.me.domain.dto.ContactMe;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class ContactMeEntityList2ContactMeListMapper
        implements Function<List<ContactMeEntity>, List<ContactMe>> {

    @Inject
    ContactMeEntity2ContactMeMapper contactMeEntity2ContactMeMapper;

    @Override
    public List<ContactMe> apply(List<ContactMeEntity> contactMeEntities) {
        if (contactMeEntities == null) {
            return null;
        }

        if (contactMeEntities.isEmpty()) {
            return List.of();
        }

        return contactMeEntities.stream()
                .map(contactMeEntity2ContactMeMapper)
                .toList();
    }
}
