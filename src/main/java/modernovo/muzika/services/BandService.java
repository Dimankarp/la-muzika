package modernovo.muzika.services;

import jakarta.transaction.Transactional;
import modernovo.muzika.model.MusicBandDTO;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
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
    private final DTOCreatorService dtoCreator;
    private final BandRepository bandRepository;

    public BandService(UserRepository userRepo, DTOCreatorService dtoCreator, BandRepository bandRepository) {
        this.userRepo = userRepo;
        this.dtoCreator = dtoCreator;
        this.bandRepository = bandRepository;
    }

    @Transactional
    public Optional<Page<MusicBandDTO>> getBandsDTObyUsername(String username, Pageable p) {
        var user = userRepo.findByUsername(username);
        if(user.isPresent()){
            var bands = bandRepository.getMusicBandsByOwner(user.get(), p);
            return Optional.of(bands.map(dtoCreator::toDTO));
        } else{
            return Optional.empty();
        }

    }

    @Transactional
    public Page<MusicBandDTO> getBandsDTO(Pageable p) {
        return bandRepository.findAll(p).map(dtoCreator::toDTO);
    }

}
