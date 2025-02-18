package dev.ash.techradar.domain.technology.services.impl;

import dev.ash.techradar.common.enums.ChangeType;
import dev.ash.techradar.domain.analytics.repositories.TechnologyChangeRepository;
import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.entities.TechnologyChange;
import dev.ash.techradar.domain.technology.events.TechnologyChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class TechnologyChangeService {

    private final TechnologyChangeRepository changeRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTechnologyChange(TechnologyChangeEvent event) {
        log.debug("Processing technology change event: {}", event);

        if (event.getChanges().isEmpty() && event.getChangeType() != ChangeType.ADDED) {
            return; // No changes to track
        }

        if (event.getChangeType() == ChangeType.ADDED) {
            createAdditionChange(event);
        } else {
            createUpdateChanges(event);
        }
    }

    private void createAdditionChange(TechnologyChangeEvent event) {
        TechnologyChange change = new TechnologyChange();
        change.setTechnology(event.getTechnology());
        change.setChangeType(event.getChangeType());
        change.setChangeDate(event.getChangeDate());
        change.setChangedField("technology");
        change.setNewValue(formatTechnologySummary(event.getTechnology()));

        changeRepository.save(change);
        log.debug("Created technology addition change record: {}", change);
    }

    private void createUpdateChanges(TechnologyChangeEvent event) {
        event.getChanges().forEach((field, propertyChange) -> {
            TechnologyChange change = new TechnologyChange();
            change.setTechnology(event.getTechnology());
            change.setChangeType(event.getChangeType());
            change.setChangeDate(event.getChangeDate());
            change.setChangedField(propertyChange.getField());
            change.setPreviousValue(propertyChange.getPreviousValue());
            change.setNewValue(propertyChange.getNewValue());

            changeRepository.save(change);
            log.debug("Created technology update change record: {}", change);
        });
    }

    private String formatTechnologySummary(Technology technology) {
        return String.format("{name: %s, quadrant: %s, ring: %s}",
                             technology.getName(),
                             technology.getQuadrant(),
                             technology.getRing());
    }
}
