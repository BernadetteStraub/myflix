package com.example.myflix;

import com.example.myflix.model.Viewer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class UserPrincipal implements UserDetails {
    // FOR CREATING USERS

    private final Viewer viewer;

    public UserPrincipal(Viewer driver) {
        this.viewer = driver;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> authorities = viewer.getAuthorities();
        return AuthorityUtils.createAuthorityList(authorities.toArray(new
                String[authorities.size()]));
    }
    @Override
    public String getPassword() {
        return viewer.getPassword();
    }
    @Override
    public String getUsername() {
        return viewer.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
