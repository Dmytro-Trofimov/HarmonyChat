package com.harmonyChat.HarmonyChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.harmonyChat.HarmonyChat.model.User;
import com.harmonyChat.HarmonyChat.repository.UserRepository;

@Service
public class CustomUserDetails implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userRepository.findByName(username).orElseThrow(() -> 
	        new UsernameNotFoundException("User not found with username: " + username)
	    );

	    return new CustomUserPrincipal(
	        user.getId(),
	        user.getName(),
	        user.getPassword(),
	        List.of(new SimpleGrantedAuthority(user.getRole()))
	    );
	}


}
