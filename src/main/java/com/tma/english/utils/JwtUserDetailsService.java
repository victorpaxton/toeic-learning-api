package com.tma.english.utils;

import com.tma.english.models.entities.user.AppUser;
import com.tma.english.models.entities.user.SessionUser;
import com.tma.english.repositories.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found account"));
        return new SessionUser(user);
    }

    public UserDetails loadUserById(String id) {
        try {
            AppUser user = userRepository.findById(Long.parseLong(id))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found id: " + id));
            return new SessionUser(user);
        } catch (Exception ex) {
            LOGGER.error("Failed to load user from sql server: " + ex);
            return null;
        }
    }
}

