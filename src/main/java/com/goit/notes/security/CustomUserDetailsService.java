package com.goit.notes.security;

import com.goit.notes.user.User;
import com.goit.notes.user.UserDetailsImpl;
import com.goit.notes.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return UserDetailsImpl.build(user);
    }
}
