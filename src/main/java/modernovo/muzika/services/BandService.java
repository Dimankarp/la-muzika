package modernovo.muzika.services;


import modernovo.muzika.model.RequestStatus;
import modernovo.muzika.model.User;
import modernovo.muzika.model.dto.MusicBandBatchDTO;
import modernovo.muzika.model.dto.MusicBandDTO;
import modernovo.muzika.model.ActionType;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.specifications.BandSpecs;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto.creators.BandDTOCreatorService;
import modernovo.muzika.services.dto.creators.MinIOException;
import modernovo.muzika.services.entity.creators.BandEntityCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BandService extends EntityService<MusicBand, MusicBandDTO, Long> {
    private final Logger logger = LoggerFactory.getLogger(BandService.class);
    private final UserRepository userRepo;
    private final BandDTOCreatorService bandCreator;
    private final BandEntityCreatorService bandEntityCreator;
    private final BandRepository bandRepository;
    private final ResourceUtils resourceUtils;
    private final AuditService auditService;
    private final YAMLService yamlService;
    private final BatchRequestService batchRequestService;
    private final MinIOService minIOService;


    public BandService(UserRepository userRepo, BandDTOCreatorService bandCreator, BandRepository bandRepository, BandEntityCreatorService bandEntityCreatorService, BandEntityCreatorService bandEntityCreator, ResourceUtils resourceUtils, AuditService auditService, YAMLService yamlService, BatchRequestService batchRequestService, MinIOService minIOService) {
        super(resourceUtils, bandRepository, bandEntityCreatorService);
        this.userRepo = userRepo;
        this.bandCreator = bandCreator;
        this.bandRepository = bandRepository;
        this.bandEntityCreator = bandEntityCreator;
        this.resourceUtils = resourceUtils;
        this.auditService = auditService;
        this.yamlService = yamlService;
        this.batchRequestService = batchRequestService;
        this.minIOService = minIOService;
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Page<MusicBandDTO> getBandsDTO(String username, String nameLike, String descLike, Pageable p) throws IllegalServiceArgumentException {
        Specification<MusicBand> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null : BandSpecs.nameLike(nameLike))
                .and(!StringUtils.hasLength(descLike) ? null : BandSpecs.descriptionLike(descLike));
        if (StringUtils.hasLength(username)) {
            var user = userRepo.findByUsername(username);
            if (user.isEmpty()) {
                throw new IllegalServiceArgumentException("Username " + username + " not found");
            }
            filters = filters.and(BandSpecs.ownerNameEquals(username));
        }

        var bands = bandRepository.findAll(filters, p);
        return bands.map(bandCreator::toDTO);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateEntity(MusicBandDTO dto) throws DTOConstraintViolationException, CallerIsNotAUser {
        var caller = resourceUtils.getCaller();
        MusicBand band;
        if (Objects.equals(caller.getId(), dto.getOwnerId())) {
            band = bandEntityCreator.fromDTOUpdate(dto, caller);
        } else {
            band = bandEntityCreator.fromDTOAdminUpdate(dto, caller);
        }
        bandRepository.save(band);
        auditService.addEntry(caller, band, ActionType.UPDATE);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MusicBand createEntity(MusicBandDTO dto) throws DTOConstraintViolationException, CallerIsNotAUser, EntityConstraintViolationException {
        var owner = resourceUtils.getCaller();
        var entity = bandEntityCreator.fromDTONew(dto, owner);
        return saveEntity(entity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    protected MusicBand saveEntity(MusicBand entity) {
        MusicBand saved = bandRepository.save(entity);
        auditService.addEntry(saved.getOwner(), saved, ActionType.CREATE);
        return saved;
    }


    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Optional<MusicBandDTO> removeBandByEstablishmentDate(ZonedDateTime date) throws CallerIsNotAUser {
        var user = resourceUtils.getCaller();
        return user.getBands().stream().filter((x) -> x.getEstablishmentDate().equals(date)).findAny()
                .map(bandCreator::toDTO);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Long sumAllAlbums() {
        var bands = bandRepository.findAll();
        return bands.stream().map(MusicBand::getAlbumsCount).reduce(0L, Long::sum);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Long countAllWithAlbumsCount(Long albumsCount) {
        var bands = bandRepository.findAll();
        return bands.stream().map(MusicBand::getAlbumsCount).filter((x) -> x.equals(albumsCount)).count();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MusicBandDTO addSingle(Long bandId) throws IllegalServiceArgumentException, CallerIsNotAUser {
        var band = getBandByIdAndOwner(bandId);
        band.setSinglesCount(band.getSinglesCount() + 1);
        return bandCreator.toDTO(band);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public MusicBandDTO removeMember(Long bandId) throws IllegalServiceArgumentException, CallerIsNotAUser {
        var band = getBandByIdAndOwner(bandId);
        if (band.getNumberOfParticipants() <= 1) {
            throw new IllegalServiceArgumentException("Requested must always have at least one member");
        }
        band.setNumberOfParticipants(band.getNumberOfParticipants() - 1);
        return bandCreator.toDTO(band);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<MusicBand> importYAML(MultipartFile file) throws CallerIsNotAUser, IOException, EntityConstraintViolationException, DTOConstraintViolationException {

        var caller = resourceUtils.getCaller();
        var request = batchRequestService.createRequest(caller);
        String newfileName = request.getId().toString();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                try {
                    minIOService.unlockFile(newfileName);
                } catch (Exception e) {
                    logger.error("Failed to lift retention from file {}. Because of: {}", newfileName, e.getMessage());
                }
            }

            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_ROLLED_BACK) {
                    try {
                        request.setStatus(RequestStatus.CANCELLED);
                        minIOService.unlockFile(newfileName);
                        minIOService.removeFile(newfileName);
                    } catch (Exception e) {
                        logger.error("Failed to rollback when importing file {}. Because of: {}", newfileName, e.getMessage());
                    }
                }
            }
        });
        var dtos = yamlService.parse(file.getInputStream(), MusicBandBatchDTO.class).getBands();
        var entities = batchDTONewToEntities(dtos, caller);
        var managedEntities = batchSaveEntities(entities);
        minIOService.saveAndLockFile(file.getInputStream(), file.getSize(), newfileName);
        request.setStatus(RequestStatus.ACCEPTED);
        request.setAddedCount(entities.size());
        return managedEntities;
    }

    private List<MusicBand> batchDTONewToEntities(List<MusicBandDTO> dtos, User caller) throws DTOConstraintViolationException, CallerIsNotAUser, EntityConstraintViolationException {
        //Not using Stream API because of checked exception tragedy
        List<MusicBand> entities = new ArrayList<>();
        for (MusicBandDTO dto : dtos) {
            entities.add(bandEntityCreator.fromDTONew(dto, caller));
        }
        return entities;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected List<MusicBand> batchSaveEntities(List<MusicBand> entities) {
        List<MusicBand> saved = new ArrayList<>();
        for (MusicBand entity : entities) {
            saved.add(saveEntity(entity));
        }
        return saved;
    }

    private MusicBand getBandByIdAndOwner(Long bandId) throws IllegalServiceArgumentException, CallerIsNotAUser {
        var caller = resourceUtils.getCaller();
        var bandOpt = bandRepository.findById(bandId);
        if (bandOpt.isEmpty()) {
            throw new IllegalServiceArgumentException("Band " + bandId + " not found");
        }
        var band = bandOpt.get();
        if (!Objects.equals(band.getOwner().getUsername(), caller.getUsername())) {
            throw new IllegalServiceArgumentException("Provided band doesn't belong to caller");
        }
        return band;
    }


}
