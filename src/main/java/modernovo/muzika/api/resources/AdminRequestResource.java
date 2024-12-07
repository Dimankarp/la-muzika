package modernovo.muzika.api.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import modernovo.muzika.model.dto.AdminRequestDTO;
import modernovo.muzika.services.AdminRequestService;
import modernovo.muzika.services.CallerIsNotAUser;
import modernovo.muzika.services.IllegalServiceArgumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
public class AdminRequestResource {

    private final AdminRequestService adminRequestService;


    AdminRequestResource(final AdminRequestService adminRequestService) {
        this.adminRequestService = adminRequestService;
    }

    @RolesAllowed(value = {"ADMIN"})
    @GetMapping(value = "")
    public Page<AdminRequestDTO> getPendingRequests(Pageable p) {
        return adminRequestService.getPendingRequestsDTO(p);

    }

    @RolesAllowed(value = {"ADMIN"})
    @PostMapping(value = "/accept/{id}")
    public void acceptRequest(@PathVariable Long id) throws IllegalServiceArgumentException {
        adminRequestService.acceptRequset(id);
    }

    @PostMapping(value = "")
    public void createRequest() throws IllegalServiceArgumentException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        adminRequestService.createRequest(auth.getName());
    }

    @GetMapping(value = "/pending")
    public AdminRequestDTO getMyRequest(HttpServletResponse response) throws IllegalServiceArgumentException, CallerIsNotAUser {
        var pendingOpt = adminRequestService.getMyRequest();
        if (pendingOpt.isPresent()) {
            return pendingOpt.get();
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }


}
