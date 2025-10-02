package com.platform.tickets.filters;

import com.platform.tickets.domain.entities.User;
import com.platform.tickets.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserProvisioningFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            UUID keyCloakId = UUID.fromString(jwt.getSubject());

            if(!userRepository.existsById(keyCloakId)){
                User user = new User();
                user.setId(keyCloakId);
                user.setName(jwt.getClaims().get("preferred_name").toString());
                user.setEmail(jwt.getClaims().get("email").toString());
                userRepository.save(user);
            }
        }
        filterChain.doFilter(request, response);
    }
}
