package modernovo.muzika.services.entity.constraints;

import modernovo.muzika.model.RequestStatus;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import modernovo.muzika.repositories.StudioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class StudioConstraintsService {

    private final StudioRepository studioRepository;

    public StudioConstraintsService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    @Transactional(readOnly = true)
    public boolean uniqueNameAndAddress(Studio studio) {
        var studios = studioRepository.getStudiosByNameAndAddress(studio.getName(), studio.getAddress());
        return studios.isEmpty() || studios.size() == 1 && Objects.equals(studios.stream().findAny().get().getId(),
                studio.getId());
    }

}
