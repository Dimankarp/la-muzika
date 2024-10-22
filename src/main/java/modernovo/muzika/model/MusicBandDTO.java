package modernovo.muzika.model;

import java.time.ZonedDateTime;

public class MusicBandDTO {

    private Long id;
    private String name;
    private Float coordX;
    private Float coordY;
    private ZonedDateTime creationDate;
    private MusicGenre genre;
    private Integer numberOfParticipants;
    private Integer singlesCount;
    private String description;
    private Long albumsCount;
    private ZonedDateTime establishmentDate;
    private Long ownerId;
    private String ownerName;
    private Boolean adminOpen;
    private AlbumDTO bestAlbum;
    private Long bestAlbumId;
    private StudioDTO studio;
    private Long studioId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCoordX() {
        return coordX;
    }

    public void setCoordX(Float coordX) {
        this.coordX = coordX;
    }

    public Float getCoordY() {
        return coordY;
    }

    public void setCoordY(Float coordY) {
        this.coordY = coordY;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getAdminOpen() {
        return adminOpen;
    }

    public void setAdminOpen(Boolean adminOpen) {
        this.adminOpen = adminOpen;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public AlbumDTO getBestAlbum() {
        return bestAlbum;
    }

    public void setBestAlbum(AlbumDTO bestAlbum) {
        this.bestAlbum = bestAlbum;
    }

    public StudioDTO getStudio() {
        return studio;
    }

    public void setStudio(StudioDTO studio) {
        this.studio = studio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBestAlbumId() {
        return bestAlbumId;
    }

    public void setBestAlbumId(Long bestAlbumId) {
        this.bestAlbumId = bestAlbumId;
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }
}

