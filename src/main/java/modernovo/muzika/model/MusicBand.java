package modernovo.muzika.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "music_band")
public class MusicBand implements Ownable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    @Embedded
    private Coordinates coordinates;


    @Column(name = "creation_date", insertable = false, updatable = false)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Integer getSinglesCount() {
        return singlesCount;
    }

    public void setSinglesCount(Integer singlesCount) {
        this.singlesCount = singlesCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Album getBestAlbum() {
        return bestAlbum;
    }

    public void setBestAlbum(Album bestAlbum) {
        this.bestAlbum = bestAlbum;
    }

    public Long getAlbumsCount() {
        return albumsCount;
    }

    public void setAlbumsCount(Long albumsCount) {
        this.albumsCount = albumsCount;
    }

    public ZonedDateTime getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(ZonedDateTime establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Boolean getAdminOpen() {
        return adminOpen;
    }

    public void setAdminOpen(Boolean adminOpen) {
        this.adminOpen = adminOpen;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
}

