package dev.ash.techradar.domain.technology.services.impl;

import dev.ash.techradar.common.enums.ChangeType;
import dev.ash.techradar.domain.analytics.repositories.TechnologyChangeRepository;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.entities.TechnologyChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TechnologyChangeTracker {

    private final TechnologyChangeRepository changeRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void trackCreation(Technology technology) {
        TechnologyChange change = new TechnologyChange();
        change.setTechnology(technology);
        change.setChangeType(ChangeType.ADDED);
        change.setChangeDate(LocalDateTime.now());
        change.setChangedField("technology");
        change.setNewValue(formatTechnologySummary(technology));

        changeRepository.save(change);
        log.debug("Created technology addition change record for: {}", technology.getName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void trackUpdate(Technology original, Technology updated) {
        trackFieldChange(original, updated, "name", original.getName(), updated.getName());
        trackFieldChange(original, updated, "description", original.getDescription(), updated.getDescription());
        trackFieldChange(original, updated, "quadrant", original.getQuadrant(), updated.getQuadrant());
        trackFieldChange(original, updated, "ring", original.getRing(), updated.getRing());

        trackMetadataChanges(original, updated);
    }

    private void trackFieldChange(Technology technology, Technology newTech, String fieldName,
                                  Object oldValue, Object newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            TechnologyChange change = new TechnologyChange();
            change.setTechnology(technology);
            change.setChangeType(ChangeType.UPDATED);
            change.setChangeDate(LocalDateTime.now());
            change.setChangedField(fieldName);
            change.setPreviousValue(formatValue(oldValue));
            change.setNewValue(formatValue(newValue));

            changeRepository.save(change);
            log.debug("Created change record for field: {} in technology: {}", fieldName, technology.getName());
        }
    }

    private void trackMetadataChanges(Technology oldTech, Technology newTech) {
        // Tracking added or modified metadata
        newTech.getMetadata().forEach((key, value) -> {
            String oldValue = oldTech.getMetadata().get(key);
            if (!Objects.equals(oldValue, value)) {
                createMetadataChange(oldTech, key, oldValue, value);
            }
        });

        // Tracking removed metadata
        oldTech.getMetadata().forEach((key, value) -> {
            if (!newTech.getMetadata().containsKey(key)) {
                createMetadataChange(oldTech, key, value, null);
            }
        });
    }

    private void createMetadataChange(Technology technology, String key, String oldValue, String newValue) {
        TechnologyChange change = new TechnologyChange();
        change.setTechnology(technology);
        change.setChangeType(ChangeType.UPDATED);
        change.setChangeDate(LocalDateTime.now());
        change.setChangedField("metadata." + key);
        change.setPreviousValue(oldValue);
        change.setNewValue(newValue);

        changeRepository.save(change);
        log.debug("Created metadata change record for key: {} in technology: {}", key, technology.getName());
    }

    private String formatValue(Object value) {
        if (value == null) {
            return null;
        }
        return value instanceof Enum ? ((Enum<?>) value).name() : value.toString();
    }

    private String formatTechnologySummary(Technology technology) {
        return String.format("{name: %s, quadrant: %s, ring: %s}",
                             technology.getName(),
                             technology.getQuadrant(),
                             technology.getRing());
    }
}
