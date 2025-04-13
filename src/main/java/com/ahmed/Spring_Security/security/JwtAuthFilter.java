package com.ahmed.Spring_Security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserDetailsService uds;

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request
            , @NonNull HttpServletResponse response
            , @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if(request.getServletPath().contains("/api/v1/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt=authHeader.substring(7);
        userEmail=jwtService.extractUserName(jwt);
       if(userEmail!=null || SecurityContextHolder.getContext().getAuthentication()==null){
           UserDetails user=uds.loadUserByUsername(userEmail);
           if(jwtService.isTokenValid(jwt,user)){
               UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                       user ,
                       null
                       ,user.getAuthorities()
               );
               authToken.setDetails(
                       new WebAuthenticationDetailsSource().buildDetails(request)
               );
               SecurityContextHolder.getContext().setAuthentication(authToken);
           }

       }

      filterChain.doFilter(request,response);


    }
}
