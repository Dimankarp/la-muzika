package modernovo.muzika.model;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "account", schema = "public")
public class User {
    public User() {

    }

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Long getId() {
        return id;
    }

    private String username;

    @Column(name = "hash")
    private String passwordHash;

    @ManyToMany()
    @JoinTable(
            name = "band_owner",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "band_id"))
    private final Set<MusicBand> musicBands = new LinkedHashSet<>();



}