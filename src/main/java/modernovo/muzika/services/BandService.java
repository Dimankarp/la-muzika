package modernovo.muzika.services;

import jakarta.transaction.Transactional;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.model.ActionType;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.specifications.BandSpecs;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto_creators.BandDTOCreatorService;
import modernovo.muzika.services.entity_creators.BandEntityCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
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

    public BandService(UserRepository userRepo, BandDTOCreatorService bandCreator, BandRepository bandRepository, BandEntityCreatorService bandEntityCreatorService, BandEntityCreatorService bandEntityCreator, ResourceUtils resourceUtils, AuditService auditService) {
        super(resourceUtils, bandRepository, bandEntityCreatorService);
        this.userRepo = userRepo;
        this.bandCreator = bandCreator;
        this.bandRepository = bandRepository;
        this.bandEntityCreator = bandEntityCreator;
        this.resourceUtils = resourceUtils;
        this.auditService = auditService;
    }

    @Transactional
    public Page<MusicBandDTO> getBandsDTO(String username, String nameLike, String descLike, Pageable p) throws IllegalServiceArgumentException {
        Specification<MusicBand> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null : BandSpecs.nameLike(nameLike)).and(!StringUtils.hasLength(descLike) ? null : BandSpecs.descriptionLike(descLike));
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
    public MusicBand createEntity(MusicBandDTO dto) throws DTOConstraintViolationException, CallerIsNotAUser {
        var band = super.createEntity(dto);
        auditService.addEntry(band.getOwner(), band, ActionType.CREATE);
        return band;
    }


    @Transactional
    public Optional<MusicBandDTO> removeBandByEstablishmentDate(ZonedDateTime date) throws CallerIsNotAUser {
        var user = resourceUtils.getCaller();
        return user.getBands().stream().filter((x) -> x.getEstablishmentDate().equals(date)).findAny().map(bandCreator::toDTO);
    }

    @Transactional
    public Long sumAllAlbums() {
        var bands = bandRepository.findAll();
        return bands.stream().map(MusicBand::getAlbumsCount).reduce(0L, Long::sum);
    }

    @Transactional
    public Long countAllWithAlbumsCount(Long albumsCount) {
        var bands = bandRepository.findAll();
        return bands.stream().map(MusicBand::getAlbumsCount).filter((x) -> x.equals(albumsCount)).count();
    }

    @Transactional
    public MusicBandDTO addSingle(Long bandId) throws IllegalServiceArgumentException, CallerIsNotAUser {
        var band = getBandByIdAndOwner(bandId);
        band.setSinglesCount(band.getSinglesCount() + 1);
        return bandCreator.toDTO(band);
    }

    @Transactional
    public MusicBandDTO removeMember(Long bandId) throws IllegalServiceArgumentException, CallerIsNotAUser {
        var band = getBandByIdAndOwner(bandId);
        if(band.getNumberOfParticipants()  <= 1){
            throw new IllegalServiceArgumentException("Requested must always have at least one member");
        }
        band.setNumberOfParticipants(band.getNumberOfParticipants() - 1);
        return bandCreator.toDTO(band);
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
