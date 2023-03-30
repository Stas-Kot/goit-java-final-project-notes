package com.goit.notes.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private final CustomAuthProvider authProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/images/**", "/css/**");
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions()
                .sameOrigin()
                .and()
                .csrf()
                .disable()
                .cors()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/note/example", "/note/share/**")
                .permitAll()
                .requestMatchers("/register")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin(form -> form.loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/note/list", true))
                .logout(logout -> logout.permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/note/list");
                    response.setStatus(HttpServletResponse.SC_OK);
                }));

        return http.build();
    }
}
