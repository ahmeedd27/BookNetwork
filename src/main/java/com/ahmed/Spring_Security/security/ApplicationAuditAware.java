package com.ahmed.Spring_Security.security;

import com.ahmed.Spring_Security.entities.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication(); // to get currently logged in user
        // checking if no auth or it not authenticated or the token is unknown
        if(auth==null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }
        User user=(User) auth.getPrincipal();

        return Optional.ofNullable(user.getId()); // nullable in case the principle is null
    }
}
