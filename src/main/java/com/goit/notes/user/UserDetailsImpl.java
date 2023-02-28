package com.goit.notes.user;


import com.goit.notes.note.Note;
import com.goit.notes.user.views.EViewType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final List<Note> notes;
    private final EViewType viewType;
    private final Collection<? extends GrantedAuthority> authorities;



    public static UserDetailsImpl build(User user) {
        EUserRole role = user.getRole();
        Set<GrantedAuthority> authorities = Collections.singleton(role::name);
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getNotes(),
                user.getViewType(),
                authorities

        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getViewType() {
        return viewType.name();
    }

    public List<Note> getNotes() {
        return notes;
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
