package com.goit.notes.user;

import com.goit.notes.exceptions.*;
import com.goit.notes.note.Note;
import com.goit.notes.user.views.EViewType;
import com.goit.notes.utils.Validation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final Validation validation;
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public synchronized User save(User user) throws UserAlreadyExistException {
        if (user.getUsername() == null || !validation.isValidName(user.getUsername())) {
            throw new NotValidUserNameException("Not valid username");
        }

        if (user.getPassword() == null || !validation.isValidPassword(user.getPassword())) {
            throw new NotValidPasswordException("Not valid password");
        }

        if (isUserExists(user.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username: "
                    + user.getUsername());
        }

        user.setRole(EUserRole.ROLE_USER);
        user.setViewType(EViewType.LIST);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    private boolean isUserExists(String username) {
        return repository.findByUsername(username) != null;
    }


    public User getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id=" + id + " not found"));
    }

    public void setViewType(String username, String viewType) {
        User user = repository.findByUsername(username);
        user.setViewType(EViewType.valueOf(viewType));
        repository.save(user);
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public synchronized void update(User user) {
        User updatedUser = getById(user.getId());
        if(!updatedUser.getUsername().equals(user.getUsername())) {
            if (user.getUsername() == null || !validation.isValidName(user.getUsername())) {
                throw new NotValidUserNameException("Not valid username!");
            }
            if (isUserExists(user.getUsername())) {
                throw new UserAlreadyExistException("There is an account with that username: "
                        + user.getUsername());
            }
        }

        updatedUser.setUsername(user.getUsername());
        updatedUser.setRole(user.getRole());
        repository.save(updatedUser);
    }
}
