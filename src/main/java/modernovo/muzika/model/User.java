package modernovo.muzika.model;

import jakarta.persistence.*;
import modernovo.muzika.security.SHA384PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "account", schema = "public")
public class User {
    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = SHA384PasswordEncoder.encodeSHA384(password);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }


    private String username;

    public String getUsername() {
        return username;
    }

    @Column(name = "hash")
    private String passwordHash;

    public String getPasswordHash() {
        return passwordHash;
    }


    @OneToMany(mappedBy = "user")
    private List<RoleMember> roles = new ArrayList<>();

    public List<Role> getRoles() {
        return roles.stream().map(RoleMember::getRole).toList();
    }


    @ManyToMany()
    @JoinTable(
            name = "band_owner",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "band_id"))
    private Set<MusicBand> musicBands = new LinkedHashSet<>();


}