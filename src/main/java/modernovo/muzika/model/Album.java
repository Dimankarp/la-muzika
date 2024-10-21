package modernovo.muzika.model;

import jakarta.persistence.*;

@Entity
@Table(name = "album", schema = "public")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer tracks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_id"
    )
    private User owner;

    public Album() {
    }

    public Album(String name, Integer tracks, User owner) {
        this.name = name;
        this.tracks = tracks;
        this.owner = owner;
    }

}