package com.harmonyChat.HarmonyChat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.harmonyChat.HarmonyChat.service.CustomUserDetails;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetails customUserDetails;

	public SecurityConfig(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}
	@Bean
	public SessionRegistry sessionRegistry() {
	    return new SessionRegistryImpl();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(request -> request
	            .requestMatchers("/register", "/login").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .loginProcessingUrl("/login")
	            .defaultSuccessUrl("/home", true)
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutSuccessUrl("/login")
	            .permitAll()
	        )
	        .userDetailsService(customUserDetails)
	        .sessionManagement(session -> session
	            .maximumSessions(1)
	            .sessionRegistry(sessionRegistry())
	        );
	    return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
