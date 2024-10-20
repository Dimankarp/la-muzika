package modernovo.muzika.model;

import java.time.ZonedDateTime;

public class MusicBandDTO {

    private String name;
    private Float coordX;
    private Float coordY;
    private ZonedDateTime creationDate;
    private MusicGenre genre;
    private Integer numberOfParticipants;
    private Integer singlesCount;
    private String description;
    private String besalbumName;
    private Integer bestAlbumTracks;
    private Long albumsCount;
    private ZonedDateTime establishmentDate;
    private String studioName;

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

    public String getBesalbumName() {
        return besalbumName;
    }

    public void setBesalbumName(String besalbumName) {
        this.besalbumName = besalbumName;
    }

    public Integer getBestAlbumTracks() {
        return bestAlbumTracks;
    }

    public void setBestAlbumTracks(Integer bestAlbumTracks) {
        this.bestAlbumTracks = bestAlbumTracks;
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

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public String getStudioAddress() {
        return studioAddress;
    }

    public void setStudioAddress(String studioAddress) {
        this.studioAddress = studioAddress;
    }

    private String studioAddress;

}
