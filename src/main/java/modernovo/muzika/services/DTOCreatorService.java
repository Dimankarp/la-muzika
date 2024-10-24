package modernovo.muzika.services;

import modernovo.muzika.model.*;
import org.springframework.stereotype.Service;

@Service
public class DTOCreatorService {

    private final UserService userService;

    DTOCreatorService(UserService userService) {
        this.userService = userService;
    }

    public UserDTO toDTO(User user) {
        var dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setIsAdmin(userService.hasAdminRights(user));
        return dto;
    }

    public MusicBandDTO toDTO(MusicBand musicBand) {

        var dto = new MusicBandDTO();
        dto.setId(musicBand.getId());
        dto.setName(musicBand.getName());
        dto.setDescription(musicBand.getDescription());
        dto.setCoordX(musicBand.getCoordinates().getX());
        dto.setCoordY(musicBand.getCoordinates().getY());
        dto.setCreationDate(musicBand.getCreationDate());
        dto.setGenre(musicBand.getGenre());
        dto.setNumberOfParticipants(musicBand.getNumberOfParticipants());
        dto.setSinglesCount(musicBand.getSinglesCount());
        dto.setAlbumsCount(musicBand.getAlbumsCount());
        dto.setEstablishmentDate(musicBand.getEstablishmentDate());
        dto.setOwnerId(musicBand.getOwner().getId());
        dto.setOwnerName(musicBand.getOwner().getUsername());
        dto.setAdminOpen(musicBand.getAdminOpen());
        if (musicBand.getBestAlbum() != null) {
            dto.setBestAlbum(toDTO(musicBand.getBestAlbum()));
            dto.setBestAlbumId(musicBand.getStudio().getId());
        }

        if (musicBand.getStudio() != null) {
            dto.setStudio(toDTO(musicBand.getStudio()));
            dto.setStudioId(musicBand.getStudio().getId());
        }


        return dto;
    }

    public AlbumDTO toDTO(Album album) {

        var dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setTracks(album.getTracks());
        dto.setOwnerID(album.getOwner().getId());
        return dto;
    }

    public StudioDTO toDTO(Studio studio) {

        var dto = new StudioDTO();
        dto.setId(studio.getId());
        dto.setName(studio.getName());
        dto.setAddress(studio.getAddress());
        dto.setOwnerID(studio.getOwner().getId());
        return dto;

    }

}
