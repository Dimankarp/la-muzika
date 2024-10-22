package modernovo.muzika.model;

public class AlbumDTO {

    private Long id;

    private String name;

    private Integer tracks;

    private Long ownerID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTracks() {
        return tracks;
    }

    public void setTracks(Integer tracks) {
        this.tracks = tracks;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long owner_id) {
        this.ownerID = owner_id;
    }
}
