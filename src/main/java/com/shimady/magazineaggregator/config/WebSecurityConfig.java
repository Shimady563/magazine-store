package com.shimady.magazineaggregator.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.regex.Pattern;

@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(new IdPathRequestMatcher()).permitAll()
                        .requestMatchers("/users/sign-up", "/error", "/magazines", "/articles", "/article/*", "/articles/*").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/magazines")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/magazines")
                        .permitAll()
                );
        return http.build();
    }

    private static class IdPathRequestMatcher implements RequestMatcher {
        private final Pattern pattern = Pattern.compile("^/magazines/\\d+$");

        @Override
        public boolean matches(HttpServletRequest request) {
            return pattern.matcher(request.getRequestURI()).matches();
        }
    }
}
