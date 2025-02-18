package dev.ash.techradar.domain.technology.entities;

import dev.ash.techradar.common.enums.ChangeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "technology_changes")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"technology"})
public class TechnologyChange {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_id")
    private Technology technology;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeType changeType;

    @Column(nullable = false)
    private LocalDateTime changeDate;

    private String previousValue;

    private String newValue;

    @Column(length = 50)
    private String changedField;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TechnologyChange that)) {
            return false;
        }
        // Using natural composite key
        return technology != null && changeDate != null &&
            technology.equals(that.technology) &&
            changeDate.equals(that.changeDate);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
