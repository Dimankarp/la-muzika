package modernovo.muzika.services;

import jakarta.transaction.Transactional;
import modernovo.muzika.dto.AlbumDTO;
import modernovo.muzika.dto.MusicBandDTO;
import modernovo.muzika.dto.StudioDTO;
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
import org.springframework.stereotype.Service;

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

    public BandService(UserRepository userRepo, BandDTOCreatorService bandCreator,
                       AlbumDTOCreatorService albumCreator, StudioDTOCreatorService studioCreator, BandRepository bandRepository,
                       AlbumRepository albumRepository, StudioRepository studioRepository) {
        this.userRepo = userRepo;
        this.bandCreator = bandCreator;
        this.albumCreator = albumCreator;
        this.studioCreator = studioCreator;
        this.bandRepository = bandRepository;
        this.albumRepository = albumRepository;
        this.studioRepository = studioRepository;
    }

    @Transactional
    public Optional<Page<MusicBandDTO>> getBandsDTObyUsername(String username, Pageable p) {
        var user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            var bands = bandRepository.getMusicBandsByOwner(user.get(), p);
            return Optional.of(bands.map(bandCreator::toDTO));
        } else {
            return Optional.empty();
        }

    }

    @Transactional
    public Page<MusicBandDTO> getBandsDTO(Pageable p) {
        return bandRepository.findAll(p).map(bandCreator::toDTO);
    }

    @Transactional
    public Optional<Page<AlbumDTO>> getAlbumsDTObyUsername(String username, Pageable p) {
        var user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            var albums = albumRepository.getAlbumsByOwner(user.get(), p);
            return Optional.of(albums.map(albumCreator::toDTO));
        } else {
            return Optional.empty();
        }

    }

    @Transactional
    public Page<AlbumDTO> getAlbumsDTO(Pageable p) {
        return albumRepository.findAll(p).map(albumCreator::toDTO);
    }


    @Transactional
    public Optional<Page<StudioDTO>> getStudiosDTObyUsername(String username, Pageable p) {
        var user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            var studios = studioRepository.getStudiosByOwner(user.get(), p);
            return Optional.of(studios.map(studioCreator::toDTO));
        } else {
            return Optional.empty();
        }

    }

    @Transactional
    public Page<StudioDTO> getStudiosDTO(Pageable p) {
        return studioRepository.findAll(p).map(studioCreator::toDTO);
    }

}
