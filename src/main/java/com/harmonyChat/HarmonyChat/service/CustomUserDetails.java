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
//		User user = userRepository.findByName(username).get();
//		if(user == null) {
//			throw new UsernameNotFoundException("user not found with givn username");
//		}
//		return org.springframework.security.core.userdetails.User.withUsername(user.getName()).password(user.getPassword()).roles(user.getRole()).build();
		User user = userRepository.findByName(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
            user.getName(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority(user.getRole()))
        );
	}

}
