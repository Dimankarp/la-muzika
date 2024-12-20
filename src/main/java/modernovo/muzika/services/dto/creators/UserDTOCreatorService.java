package modernovo.muzika.services.dto.creators;

import modernovo.muzika.model.dto.UserDTO;
import modernovo.muzika.model.User;
import modernovo.muzika.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserDTOCreatorService {

    private final UserService userService;

    UserDTOCreatorService(UserService userService) {
        this.userService = userService;
    }

    public UserDTO toDTO(User user) {
        var dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setIsAdmin(userService.hasAdminRights(user));
        return dto;
    }
}
