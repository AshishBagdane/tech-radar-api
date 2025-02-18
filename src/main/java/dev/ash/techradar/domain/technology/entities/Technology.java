package dev.ash.techradar.domain.technology.entities;

import dev.ash.techradar.common.entities.BaseEntity;
import dev.ash.techradar.domain.technology.dtos.UpdateTechnologyRequest;
import dev.ash.techradar.domain.technology.enums.Quadrant;
import dev.ash.techradar.domain.technology.enums.Ring;
import dev.ash.techradar.domain.technology.listeners.TechnologyEntityListener;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "technologies")
@EntityListeners({TechnologyEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"metadata"})
public class Technology extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Quadrant quadrant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ring ring;

    @ElementCollection
    @CollectionTable(
        name = "technology_metadata",
        joinColumns = @JoinColumn(name = "technology_id")
    )
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value")
    private Map<String, String> metadata = new HashMap<>();

    @Transient
    private Technology originalState;

    public void setOriginalState(Technology state) {
        this.originalState = state;
    }

    // Business methods
    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public void removeMetadata(String key) {
        this.metadata.remove(key);
    }

    @PreUpdate
    protected void onUpdate() {
        // This ensures updatedAt is modified
        setUpdatedAt(java.time.LocalDateTime.now());
    }

    // Proper equals implementation using business key
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Technology)) {
            return false;
        }
        Technology that = (Technology) o;
        // Using name as business key since it should be unique
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void updateFromRequest(UpdateTechnologyRequest request) {
        if (request.getName() != null) {
            this.setName(request.getName());
        }
        if (request.getDescription() != null) {
            this.setDescription(request.getDescription());
        }
        if (request.getQuadrant() != null) {
            this.setQuadrant(request.getQuadrant());
        }
        if (request.getRing() != null) {
            this.setRing(request.getRing());
        }
        if (request.getMetadata() != null) {
            this.getMetadata().clear();
            this.getMetadata().putAll(request.getMetadata());
        }
    }

    public Technology cloneState() {
        Technology clone = new Technology();

        // Copy basic fields
        clone.setId(this.getId());
        clone.setName(this.getName());
        clone.setDescription(this.getDescription());
        clone.setQuadrant(this.getQuadrant());
        clone.setRing(this.getRing());
        clone.setVersion(this.getVersion());
        clone.setCreatedAt(this.getCreatedAt());
        clone.setUpdatedAt(this.getUpdatedAt());

        // Deep copy metadata
        if (this.getMetadata() != null) {
            clone.setMetadata(new HashMap<>(this.getMetadata()));
        }

        return clone;
    }
}
