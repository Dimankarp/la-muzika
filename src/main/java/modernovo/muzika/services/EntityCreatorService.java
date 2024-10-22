package modernovo.muzika.services;

import modernovo.muzika.model.Coordinates;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.MusicBandDTO;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class EntityCreatorService {

    private final BandRepository bandRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public EntityCreatorService(BandRepository bandRepository, UserService userService, UserRepository userRepository) {
        this.bandRepository = bandRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public MusicBand fromDTONew(MusicBandDTO dto, User owner) throws DTOConstraintViolationException {
        if (dto == null) throw new DTOConstraintViolationException("Music band DTO is Null");
        var newEntity = fromDTOGeneral(dto);
        newEntity.setOwner(owner);
        if (dto.getBestAlbumId() != null) {
            var album = bandRepository.findAlbumById(dto.getBestAlbumId());
            if (album.isPresent())
                if (Objects.equals(album.get().getOwner().getId(), owner.getId())) {
                    newEntity.setBestAlbum(album.get());
                } else {
                    throw new DTOConstraintViolationException("Provided Album must be owned by owner of this band");
                }
            else {
                throw new DTOConstraintViolationException("Provided Album doesn't exist");
            }
        }

        if (dto.getStudioId() != null) {
            var studio = bandRepository.findStudiobyId(dto.getStudioId());
            if (studio.isPresent())
                if (Objects.equals(studio.get().getOwner().getId(), owner.getId())) {
                    newEntity.setStudio(studio.get());
                } else {
                    throw new DTOConstraintViolationException("Provided Studio must be owned by owner of this band or by admin caller");
                }
            else {
                throw new DTOConstraintViolationException("Provided Studio doesn't exist");
            }
        }
    return newEntity;

    }

    @Transactional
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
        updatingEntity.setOwner(entity.getOwner());

        if (dto.getBestAlbumId() != null) {
            var album = bandRepository.findAlbumById(dto.getBestAlbumId());
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
            var studio = bandRepository.findStudiobyId(dto.getStudioId());
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
    public MusicBand fromDTORegularUpdate(MusicBandDTO dto, User caller) throws DTOConstraintViolationException {
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
        updatingEntity.setOwner(caller);

        if (dto.getBestAlbumId() != null) {
            var album = bandRepository.findAlbumById(dto.getBestAlbumId());
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
            var studio = bandRepository.findStudiobyId(dto.getStudioId());
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

        var entity = new MusicBand();

        entity.setName(dto.getName());
        entity.setGenre(dto.getGenre());
        entity.setNumberOfParticipants(dto.getNumberOfParticipants());
        entity.setSinglesCount(dto.getSinglesCount());
        entity.setAlbumsCount(dto.getAlbumsCount());
        entity.setEstablishmentDate(dto.getEstablishmentDate());
        entity.setCoordinates(new Coordinates(dto.getCoordX(), dto.getCoordY()));
        return entity;
    }



}
