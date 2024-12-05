package modernovo.muzika.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "admin_requests")
public class AdminRequest {

    public AdminRequest() {
    }

    public AdminRequest(User user) {
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date", insertable = false, updatable = false)
    private ZonedDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AdminRequestStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public AdminRequestStatus getStatus() {
        return status;
    }

    public void setStatus(AdminRequestStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
