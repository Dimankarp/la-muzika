package modernovo.muzika.services.entity.creators;

import modernovo.muzika.model.dto.MusicBandDTO;
import modernovo.muzika.model.Coordinates;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.AlbumRepository;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.services.DTOConstraintViolationException;
import modernovo.muzika.services.EntityConstraintViolationException;
import modernovo.muzika.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class BandEntityCreatorService implements EntityCreator<MusicBand, MusicBandDTO> {

    private final AlbumRepository albumRepository;
    private final AlbumEntityCreatorService albumEntityCreator;
    private final StudioRepository studioRepository;
    private final  StudioEntityCreatorService studioEntityCreator;
    private final BandRepository bandRepository;
    private final UserService userService;

    BandEntityCreatorService(AlbumRepository albumRepository, StudioRepository studioRepository, BandRepository bandRepository, UserService userService, AlbumEntityCreatorService albumEntityCreator, StudioEntityCreatorService studioEntityCreator) {
        this.albumRepository = albumRepository;
        this.studioRepository = studioRepository;
        this.bandRepository = bandRepository;
        this.userService = userService;
        this.albumEntityCreator = albumEntityCreator;
        this.studioEntityCreator = studioEntityCreator;
    }


    private MusicBand fromDTOGeneral(MusicBandDTO dto) throws DTOConstraintViolationException {
        if (dto.getName() == null || dto.getName().isEmpty())
            throw new DTOConstraintViolationException("Music band Name must be non empty");

        if (dto.getCoordX() == null || dto.getCoordY() == null)
            throw new DTOConstraintViolationException("Music band Coordinates must exist");
        if (dto.getCoordX() > 540 || dto.getCoordY() > 844)
            throw new DTOConstraintViolationException("Music band Coordinates must be within valid range");


        if (dto.getGenre() == null) throw new DTOConstraintViolationException("DTO must contain Genre");
        if (dto.getNumberOfParticipants() != null && dto.getNumberOfParticipants() <= 0)
            throw new DTOConstraintViolationException("Music band must contain at least one participant");

        if (dto.getSinglesCount() != null && dto.getSinglesCount() <= 0)
            throw new DTOConstraintViolationException("Music band singles must be a positive integer");

        if (dto.getAlbumsCount() == null || dto.getAlbumsCount() <= 0)
            throw new DTOConstraintViolationException("Music band albums must be a positive integer");

        if (dto.getEstablishmentDate() == null)
            throw new DTOConstraintViolationException("Music band must have an establishment date");

        if (dto.getAdminOpen() == null)
            throw new DTOConstraintViolationException("Music band must have an admin open status");


        var entity = new MusicBand();

        entity.setName(dto.getName());
        entity.setGenre(dto.getGenre());
        entity.setNumberOfParticipants(dto.getNumberOfParticipants());
        entity.setSinglesCount(dto.getSinglesCount());
        entity.setAlbumsCount(dto.getAlbumsCount());
        entity.setEstablishmentDate(dto.getEstablishmentDate());
        entity.setCoordinates(new Coordinates(dto.getCoordX(), dto.getCoordY()));
        entity.setAdminOpen(dto.getAdminOpen());
        entity.setDescription(dto.getDescription());
        entity.setCoordinates(new Coordinates(dto.getCoordX(), dto.getCoordY()));
        return entity;
    }


    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public MusicBand fromDTONew(MusicBandDTO dto, User owner) throws DTOConstraintViolationException, EntityConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Music band DTO is Null");
        var newEntity = fromDTOGeneral(dto);
        newEntity.setOwner(owner);
        if (dto.getBestAlbumId() != null) {
            var album = albumRepository.findById(dto.getBestAlbumId());
            if (album.isPresent()) if (Objects.equals(album.get().getOwner().getId(), owner.getId())) {
                newEntity.setBestAlbum(album.get());
            } else {
                throw new DTOConstraintViolationException("Provided Album must be owned by owner of this band");
            }
            else {
                throw new DTOConstraintViolationException("Provided Album doesn't exist");
            }
        } else if(dto.getBestAlbum() != null){
            newEntity.setBestAlbum(albumEntityCreator.fromDTONew(dto.getBestAlbum(), owner));
        }

        if (dto.getStudioId() != null) {
            var studio = studioRepository.findById(dto.getStudioId());
            if (studio.isPresent()) if (Objects.equals(studio.get().getOwner().getId(), owner.getId())) {
                newEntity.setStudio(studio.get());
            } else {
                throw new DTOConstraintViolationException("Provided Studio must be owned by owner of this band or by admin caller");
            }
            else {
                throw new DTOConstraintViolationException("Provided Studio doesn't exist");
            }
        }else if(dto.getStudio() != null){
            newEntity.setStudio(studioEntityCreator.fromDTONew(dto.getStudio(), owner));
        }
        return newEntity;

    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public MusicBand fromDTOAdminUpdate(MusicBandDTO dto, User caller) throws DTOConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Music band DTO is Null");
        if (!userService.hasAdminRights(caller)) throw new IllegalArgumentException("Caller doesn't have admin rights");

        if (dto.getId() == null) throw new DTOConstraintViolationException("FromDTOUpdate is called with null band id");
        if (dto.getOwnerId() == null)
            throw new DTOConstraintViolationException("Music band DTO must have an owner id");

        var entityOpt = bandRepository.findById(dto.getId());
        if (entityOpt.isEmpty()) {
            throw new DTOConstraintViolationException("FromDTOUpdate is called with non existing band");
        }
        var entity = entityOpt.get();

        if (dto.getAdminOpen() == null || !dto.getAdminOpen()) {
            throw new DTOConstraintViolationException("Band is not admin open for update");
        }

        var updatingEntity = fromDTOGeneral(dto);
        updatingEntity.setId(entity.getId());
        updatingEntity.setOwner(entity.getOwner());

        if (dto.getBestAlbumId() != null) {
            var album = albumRepository.findById(dto.getBestAlbumId());
            if (album.isPresent())
                if (Objects.equals(album.get().getOwner().getId(), caller.getId()) || Objects.equals(album.get().getOwner().getId(), dto.getOwnerId())) {
                    updatingEntity.setBestAlbum(album.get());
                } else {
                    throw new DTOConstraintViolationException("Provided Album must be owned by owner of this band  or by admin caller");
                }
            else {
                throw new DTOConstraintViolationException("Provided Album doesn't exist");
            }
        }

        if (dto.getStudioId() != null) {
            var studio = studioRepository.findById(dto.getStudioId());
            if (studio.isPresent())
                if (Objects.equals(studio.get().getOwner().getId(), caller.getId()) || Objects.equals(studio.get().getOwner().getId(), dto.getOwnerId())) {
                    updatingEntity.setStudio(studio.get());
                } else {
                    throw new DTOConstraintViolationException("Provided Studio must be owned by owner of this band or by admin caller");
                }
            else {
                throw new DTOConstraintViolationException("Provided Studio doesn't exist");
            }
        }

        return updatingEntity;


    }

    /*
    Caller is used
     */
    @Transactional
    public MusicBand fromDTOUpdate(MusicBandDTO dto, User caller) throws DTOConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Music band DTO is Null");
        if (dto.getId() == null) throw new DTOConstraintViolationException("FromDTOUpdate is called with null band id");
        if (dto.getOwnerId() == null)
            throw new DTOConstraintViolationException("Music band DTO must have an owner id");

        var entityOpt = bandRepository.findById(dto.getId());
        if (entityOpt.isEmpty()) {
            throw new DTOConstraintViolationException("FromDTOUpdate is called with non existing band");
        }

        var persistedEntity = entityOpt.get();

        if (!persistedEntity.getOwner().getId().equals(caller.getId())) {
            throw new DTOConstraintViolationException("FromDTOUpdate is called for DTO owned by different owner");
        }


        if (!Objects.equals(dto.getOwnerId(), caller.getId())) {
            throw new DTOConstraintViolationException("From DTO for regular update is called by a non-owner of DTO");
        }

        var updatingEntity = fromDTOGeneral(dto);
        updatingEntity.setId(persistedEntity.getId());
        updatingEntity.setOwner(caller);

        if (dto.getBestAlbumId() != null) {
            var album = albumRepository.findById(dto.getBestAlbumId());
            if (album.isPresent())
                if (Objects.equals(album.get().getOwner().getId(), caller.getId())) {
                    updatingEntity.setBestAlbum(album.get());
                } else {
                    throw new DTOConstraintViolationException("Provided Album must be owned by owner of this band");
                }
            else {
                throw new DTOConstraintViolationException("Provided Album doesn't exist");
            }
        }

        if (dto.getStudioId() != null) {
            var studio = studioRepository.findById(dto.getStudioId());
            if (studio.isPresent()) if (Objects.equals(studio.get().getOwner().getId(), caller.getId())) {
                updatingEntity.setStudio(studio.get());
            } else {
                throw new DTOConstraintViolationException("Provided Studio must be owned by owner of this band or by admin caller");
            }
            else {
                throw new DTOConstraintViolationException("Provided Studio doesn't exist");
            }
        }

        return updatingEntity;


    }


}
