package modernovo.muzika.services;

import jakarta.transaction.Transactional;
import modernovo.muzika.model.MusicBandDTO;
import modernovo.muzika.repositories.BandRepository;
import modernovo.muzika.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BandService {

    private final UserRepository userRepo;
    private final DTOCreatorService dtoCreator;
    private final BandRepository bandRepository;

    public BandService(UserRepository userRepo, DTOCreatorService dtoCreator, BandRepository bandRepository) {
        this.userRepo = userRepo;
        this.dtoCreator = dtoCreator;
        this.bandRepository = bandRepository;
    }

    @Transactional
    public Optional<List<MusicBandDTO>> getBandDTObyUsername(String username) {
        var user = userRepo.findByUsername(username);
        if(user.isPresent()){
            var bands = user.get().getBands();
            return Optional.of(bands.stream().map(dtoCreator::toDTO).toList());
        } else{
            return Optional.empty();
        }

    }

    @Transactional
    public List<MusicBandDTO> getBandDTO() {
        return bandRepository.findAll().stream().map(dtoCreator::toDTO).toList();
    }

}
