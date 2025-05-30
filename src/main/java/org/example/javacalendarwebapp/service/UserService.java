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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return Optional.of(userRepository.findById(id).orElse(null));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        if (!userRepository.existsById(id)) {
            return null; // or throw an exception
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            return; // or throw an exception
        }
        userRepository.deleteById(id);
    }
}
