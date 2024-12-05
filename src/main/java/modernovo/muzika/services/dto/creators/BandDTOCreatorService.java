package modernovo.muzika.services.dto.creators;

import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.model.MusicBand;
import org.springframework.stereotype.Service;

@Service
public class BandDTOCreatorService {

    private final AlbumDTOCreatorService albumDTOCreatorService;
    private final StudioDTOCreatorService studioDTOCreatorService;

    BandDTOCreatorService(AlbumDTOCreatorService albumDTOCreatorService, StudioDTOCreatorService studioDTOCreatorService) {
        this.albumDTOCreatorService = albumDTOCreatorService;
        this.studioDTOCreatorService = studioDTOCreatorService;
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
            dto.setBestAlbum(albumDTOCreatorService.toDTO(musicBand.getBestAlbum()));
            dto.setBestAlbumId(musicBand.getBestAlbum().getId());
        }

        if (musicBand.getStudio() != null) {
            dto.setStudio(studioDTOCreatorService.toDTO(musicBand.getStudio()));
            dto.setStudioId(musicBand.getStudio().getId());
        }


        return dto;
    }
}
