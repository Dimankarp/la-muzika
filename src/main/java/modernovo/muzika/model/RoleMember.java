package modernovo.muzika.model;

import jakarta.persistence.*;

@Entity
@Table(name = "role_member", schema = "public")
public class RoleMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;


    public RoleMember() {
    }

    public RoleMember(Role role, User user) {
        this.role = role;
        this.user = user;
    }

    public Role getRole() {
        return role;
    }
}
