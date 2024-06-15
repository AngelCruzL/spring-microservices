package dev.angelcruzl.msvc.auth.services;

import dev.angelcruzl.msvc.auth.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private WebClient client;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = client.get()
                .uri("http://msvc-users:8001/login", uri -> uri.queryParam("email", email).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class)
                .block();

            logger.info("User found: " + user.getName() + " with email: " + user.getEmail());

            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (RuntimeException e) {
            String error = "Error at login, user with email: " + email + " not found";
            logger.error(error);
            logger.error(e.getMessage());

            throw new UsernameNotFoundException(error);
        }
    }

}
