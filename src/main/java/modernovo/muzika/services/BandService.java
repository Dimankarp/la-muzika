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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class BandService {
    private final Logger logger = LoggerFactory.getLogger(BandService.class);
    private final UserRepository userRepo;
    private final BandDTOCreatorService bandCreator;
    private final AlbumDTOCreatorService albumCreator;
    private final StudioDTOCreatorService studioCreator;
    private final BandRepository bandRepository;
    private final AlbumRepository albumRepository;
    private final StudioRepository studioRepository;

    public BandService(UserRepository userRepo, BandDTOCreatorService bandCreator, AlbumDTOCreatorService albumCreator, StudioDTOCreatorService studioCreator, BandRepository bandRepository, AlbumRepository albumRepository, StudioRepository studioRepository) {
        this.userRepo = userRepo;
        this.bandCreator = bandCreator;
        this.albumCreator = albumCreator;
        this.studioCreator = studioCreator;
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.studioRepository = studioRepository;
    }

    @Transactional
    public Optional<Page<MusicBandDTO>> getBandsDTO(String username, String nameLike, String descLike, Pageable p) {
        Specification<MusicBand> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null : BandSpecs.nameLike(nameLike)).and(!StringUtils.hasLength(descLike) ? null : BandSpecs.descriptionLike(descLike));
        if (StringUtils.hasLength(username)) {
            var user = userRepo.findByUsername(username);
            if (user.isEmpty()) {
                return Optional.empty();
            }
            filters = filters.and(BandSpecs.ownerNameEquals(username));
        }

        var bands = bandRepository.findAll(filters, p);
        return Optional.of(bands.map(bandCreator::toDTO));
    }


    @Transactional
    public Optional<Page<AlbumDTO>> getAlbumsDTO(String username, String nameLike, Pageable p) {
        Specification<Album> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null :
                AlbumSpecs.nameLike(nameLike));
        if (StringUtils.hasLength(username)) {
            var user = userRepo.findByUsername(username);
            if (user.isEmpty()) {
                return Optional.empty();
            }
            filters = filters.and(AlbumSpecs.ownerNameEquals(username));
        }

        var albums = albumRepository.findAll(filters, p);
        return Optional.of(albums.map(albumCreator::toDTO));
    }

    @Transactional
    public Optional<Page<StudioDTO>> getStudiosDTO(String username, String nameLike, String addressLike, Pageable p) {
        Specification<Studio> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null :
                        StudioSpecs.nameLike(nameLike)).
                and(!StringUtils.hasLength(addressLike) ? null :
                        StudioSpecs.addressLike(addressLike));
        if (StringUtils.hasLength(username)) {
            var user = userRepo.findByUsername(username);
            if (user.isEmpty()) {
                return Optional.empty();
            }
            filters = filters.and(StudioSpecs.ownerNameEquals(username));
        }

        var studios = studioRepository.findAll(filters, p);
        return Optional.of(studios.map(studioCreator::toDTO));
    }

}
