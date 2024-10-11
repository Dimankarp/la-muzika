package modernovo.muzika.api;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@DeclareRoles({"user", "admin"})
@ApplicationPath("/api")
public class APIConfiguration extends Application {
}
