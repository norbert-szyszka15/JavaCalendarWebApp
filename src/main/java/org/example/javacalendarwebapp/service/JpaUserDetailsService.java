package org.example.javacalendarwebapp.service;

import org.example.javacalendarwebapp.entity.User;
import org.example.javacalendarwebapp.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public JpaUserDetailsService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        var builder = org.springframework.security.core.userdetails.User.builder();
        builder.username(u.getUsername());
        builder.password(u.getPassword());
        builder.roles(u.getRoles().toArray(new String[0]));
        return builder.build();
    }

    public User registerNew(String username, String rawPassword) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setRoles(Set.of("USER"));
        return repo.save(u);
    }
}
