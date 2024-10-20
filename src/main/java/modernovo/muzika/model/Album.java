package modernovo.muzika.model;

import jakarta.persistence.*;
@Entity
@Table(name = "album", schema = "public")
public class Album {
    public Album(){}

    public Album(String name, Integer tracks) {
        this.name = name;
        this.tracks = tracks;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer tracks;

}