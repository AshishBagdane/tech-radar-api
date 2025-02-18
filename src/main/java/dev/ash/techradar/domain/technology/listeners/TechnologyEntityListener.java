package dev.ash.techradar.domain.technology.listeners;

import dev.ash.techradar.domain.technology.entities.Technology;
import dev.ash.techradar.domain.technology.services.impl.TechnologyChangeTracker;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TechnologyEntityListener {

    private static final String ORIGINAL_STATE_KEY = "originalState";

    private static TechnologyChangeTracker changeTracker;

    @Autowired
    public void setChangeTracker(TechnologyChangeTracker changeTracker) {
        TechnologyEntityListener.changeTracker = changeTracker;
    }

    @PostLoad
    public void postLoad(Technology technology) {
        // Store original state after loading
        technology.setOriginalState(technology.cloneState());
    }

    @PreUpdate
    public void preUpdate(Technology technology) {
        if (changeTracker != null && technology.getId() != null) {
            Technology originalState = technology.getOriginalState();
            if (originalState != null) {
                changeTracker.trackUpdate(originalState, technology);
            }
        }
    }
}
