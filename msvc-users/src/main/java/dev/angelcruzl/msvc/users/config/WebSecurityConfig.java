package dev.angelcruzl.msvc.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .requestMatchers("/authorized").permitAll()
            .requestMatchers(HttpMethod.GET, "/", "/{id}").hasAnyAuthority("SCOPE_read", "SCOPE_write")
            .requestMatchers(HttpMethod.POST, "/").hasAuthority("SCOPE_write")
            .requestMatchers(HttpMethod.PUT, "/{id}").hasAuthority("SCOPE_write")
            .requestMatchers(HttpMethod.DELETE, "/{id}").hasAuthority("SCOPE_write")
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/msvc-users-client"))
            .oauth2Client(withDefaults())
            .oauth2ResourceServer().jwt();

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .authorizeHttpRequests(authorizedReq ->
//                authorizedReq
//                    .requestMatchers("/**").permitAll()
//            );
//
//        return http.build();
//    }
}
