package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.User;
import org.example.javacalendarwebapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return Optional.of(userRepository.findById(id).orElse(null));
    }
}
