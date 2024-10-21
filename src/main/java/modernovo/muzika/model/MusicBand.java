package modernovo.muzika.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "music_band", schema = "public")
public class MusicBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    @Embedded
    private Coordinates coordinates;


    @Column(name = "creation_date")
    private ZonedDateTime creationDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private MusicGenre genre;

    @Column(name = "number_of_participants")
    private Integer numberOfParticipants;

    @Column(name = "singles_count")
    private Integer singlesCount;

    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "best_album_id")
    private Album bestAlbum;
    @Column(name = "albums_count")
    private Long albumsCount;


    @Column(name = "establishment_date")
    private ZonedDateTime establishmentDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "admin_open")
    private Boolean adminOpen;


}
