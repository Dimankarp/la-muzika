package modernovo.muzika.services;

import jakarta.transaction.Transactional;
import modernovo.muzika.model.dto.AlbumDTO;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.specifications.AlbumSpecs;
import modernovo.muzika.repositories.AlbumRepository;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.services.dto.creators.AlbumDTOCreatorService;
import modernovo.muzika.services.entity.creators.AlbumEntityCreatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AlbumService extends EntityService<Album, AlbumDTO, Long> {


    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final AlbumDTOCreatorService albumDTOCreator;


    public AlbumService(UserRepository userRepository, AlbumRepository albumRepository, AlbumDTOCreatorService albumDTOCreator, ResourceUtils resourceUtils, AlbumEntityCreatorService albumEntityCreator) {
        super(resourceUtils, albumRepository, albumEntityCreator);
        this.userRepository = userRepository;
        this.albumRepository = albumRepository;
        this.albumDTOCreator = albumDTOCreator;
    }

    @Transactional
    public Page<AlbumDTO> getAlbumsDTO(String username, String nameLike, Pageable p) throws IllegalServiceArgumentException {
        Specification<Album> filters = Specification.where(!StringUtils.hasLength(nameLike) ? null :
                AlbumSpecs.nameLike(nameLike));
        if (StringUtils.hasLength(username)) {
            var user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new IllegalServiceArgumentException("Username " + username + " not found");
            }
            filters = filters.and(AlbumSpecs.ownerNameEquals(username));
        }

        var albums = albumRepository.findAll(filters, p);
        return albums.map(albumDTOCreator::toDTO);
    }

}
