package com.davisiqueira.fraud_guard.service.user;

import com.davisiqueira.fraud_guard.model.UserModel;
import com.davisiqueira.fraud_guard.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username ::" + username + " was not found"));

        return new UserDetailsImpl(user);
    }
}
