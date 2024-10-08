package modernovo.muzika.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

@Entity
@Table(name = "music_band", schema = "public")
public class MusicBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates;

    @NotNull
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

    @OneToOne
    @JoinColumn(name = "best_album_id")
    private Album bestAlbum;
    @Column(name = "albums_count")
    private Long albumsCount;

    @NotNull
    @Column(name = "establishment_date")
    private ZonedDateTime establishmentDate;

    @OneToOne
    @JoinColumn(name = "studio_id")
    private Studio studio;

}
