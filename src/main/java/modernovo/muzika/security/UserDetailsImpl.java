package modernovo.muzika.security;

import modernovo.muzika.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private String username;
    private String passHash;
    private List<Role> roles;

    public UserDetailsImpl(String username, String passHash, List<Role> roles) {
        this.username = username;
        this.passHash = passHash;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map((x) -> new SimpleGrantedAuthority(x.name())).toList();
    }

    @Override
    public String getPassword() {
        return passHash;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
