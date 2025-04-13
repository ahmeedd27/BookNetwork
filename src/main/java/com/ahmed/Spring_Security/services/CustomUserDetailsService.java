package com.ahmed.Spring_Security.services;

import com.ahmed.Spring_Security.dao.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo ur;
    @Override
    @Transactional // to load the roles with the loaded user
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       return ur.findByEmail(email).orElseThrow();

    }
}
