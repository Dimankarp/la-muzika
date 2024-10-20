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

}