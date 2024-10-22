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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "hash")
    private String passwordHash;

    public String getPasswordHash() {
        return passwordHash;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<RoleMember> roles = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<MusicBand> musicBands = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Album> albums = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Studio> studios = new ArrayList<>();

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = SHA384PasswordEncoder.encodeSHA384(password);
    }

    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public List<Role> getRoles() {
        return roles.stream().map(RoleMember::getRole).toList();
    }

    public List<MusicBand> getBands() {
        return musicBands;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Studio> getStudios() {
        return studios;
    }
}