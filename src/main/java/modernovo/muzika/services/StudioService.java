package modernovo.muzika.services;

import jakarta.transaction.Transactional;
import modernovo.muzika.model.dto.StudioDTO;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.specifications.StudioSpecs;
import modernovo.muzika.repositories.StudioRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto.creators.StudioDTOCreatorService;
import modernovo.muzika.services.entity.creators.StudioEntityCreatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StudioService extends EntityService<Studio, StudioDTO, Long> {

    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    private final StudioDTOCreatorService studioCreator;

    public StudioService(UserRepository userRepository, StudioRepository studioRepository, StudioDTOCreatorService studioCreator, ResourceUtils resourceUtils, StudioEntityCreatorService studioEntityCreatorService) {
        super(resourceUtils, studioRepository, studioEntityCreatorService);
        this.userRepository = userRepository;
        this.studioRepository = studioRepository;
        this.studioCreator = studioCreator;
    }

    @Transactional
    public Page<StudioDTO> getStudiosDTO(String username, String nameLike, String addressLike, Pageable p) throws IllegalServiceArgumentException {
        Specification<Studio> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null :
                        StudioSpecs.nameLike(nameLike)).
                and(!StringUtils.hasLength(addressLike) ? null :
                        StudioSpecs.addressLike(addressLike));
        if (StringUtils.hasLength(username)) {
            var user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new IllegalServiceArgumentException("Username " + username + " not found");
            }
            filters = filters.and(StudioSpecs.ownerNameEquals(username));
        }

        var studios = studioRepository.findAll(filters, p);
        return studios.map(studioCreator::toDTO);
    }

}
