package modernovo.muzika.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "audit")
public class AuditEntry {

    public AuditEntry(){}

    public AuditEntry(User user, MusicBand target, ActionType action) {
        this.user = user;
        this.target = target;
        this.action = action;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date", insertable = false, updatable = false)
    private ZonedDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private MusicBand target;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType action;

}
