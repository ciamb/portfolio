package it.me.domain.mapper;

import it.me.domain.dto.ProcessedContactMe;
import it.me.entity.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class ProcessedContactMeListMapper implements Function<List<ContactMe>, List<ProcessedContactMe>> {
    private final Logger logger = Logger.getLogger(ProcessedContactMeListMapper.class.getName());

    @Inject
    ProcessedContactMeMapper processedContactMeMapper;

    @Override
    public List<ProcessedContactMe> apply(List<ContactMe> list) {
        if (list == null || list.isEmpty()) {
            logger.warn("ContactMe list is null or empty");
            return Collections.emptyList();
        }

        return list.stream()
                .map(processedContactMeMapper)
                .toList();
    }
}
