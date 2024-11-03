package modernovo.muzika.services;

import jakarta.transaction.Transactional;
import modernovo.muzika.dto.AlbumDTO;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.dto.StudioDTO;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.specifications.AlbumSpecs;
import modernovo.muzika.model.specifications.BandSpecs;
import modernovo.muzika.model.specifications.StudioSpecs;
import modernovo.muzika.repositories.AlbumRepository;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto_creators.AlbumDTOCreatorService;
import modernovo.muzika.services.dto_creators.BandDTOCreatorService;
import modernovo.muzika.services.dto_creators.StudioDTOCreatorService;
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
    private final BandRepository bandRepository;

    public BandService(UserRepository userRepo, BandDTOCreatorService bandCreator, BandRepository bandRepository,  ResourceUtils resourceUtils, BandEntityCreatorService bandEntityCreatorService) {
        super(resourceUtils, bandRepository, bandEntityCreatorService);
        this.userRepo = userRepo;
        this.bandCreator = bandCreator;
        this.bandRepository = bandRepository;
    }

    @Transactional
    public Optional<Page<MusicBandDTO>> getBandsDTO(String username, String nameLike, String descLike, Pageable p) throws IllegalServiceArgumentException {
        Specification<MusicBand> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null : BandSpecs.nameLike(nameLike)).and(!StringUtils.hasLength(descLike) ? null : BandSpecs.descriptionLike(descLike));
        if (StringUtils.hasLength(username)) {
            var user = userRepo.findByUsername(username);
            if (user.isEmpty()) {
                throw new IllegalServiceArgumentException("Username " + username + " not found");
            }
            filters = filters.and(BandSpecs.ownerNameEquals(username));
        }

        var bands = bandRepository.findAll(filters, p);
        return Optional.of(bands.map(bandCreator::toDTO));
    }


    @Transactional
    public Optional<MusicBandDTO> removeBandByEstablishmentDate(String username, ZonedDateTime date) throws IllegalServiceArgumentException {
        var userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalServiceArgumentException("Username " + username + " not found");
        }
        var user = userOpt.get();
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
    public void addSingle(String username, Long bandId) throws IllegalServiceArgumentException {
        var band = getBandByIdAndOwner(username, bandId);
        band.setSinglesCount(band.getSinglesCount() + 1);
    }

    @Transactional
    public void removeMember(String username, Long bandId) throws IllegalServiceArgumentException {
        var band = getBandByIdAndOwner(username, bandId);
        band.setNumberOfParticipants(band.getNumberOfParticipants() - 1);
    }

    private MusicBand getBandByIdAndOwner(String username, Long bandId) throws IllegalServiceArgumentException {
        var bandOpt = bandRepository.findById(bandId);
        if (bandOpt.isEmpty()) {
            throw new IllegalServiceArgumentException("Band " + bandId + " not found");
        }
        var band = bandOpt.get();
        if (!Objects.equals(band.getOwner().getUsername(), username)) {
            throw new IllegalServiceArgumentException("Provided band doesn't belong to caller");
        }
        return band;
    }

}
