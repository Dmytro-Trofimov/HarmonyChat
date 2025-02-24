package com.harmonyChat.HarmonyChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.harmonyChat.HarmonyChat.model.User;
import com.harmonyChat.HarmonyChat.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void registerUser(String email, String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole("USER");
		user.setEmail(email);
		userRepository.save(user);
	}
	@Transactional
	public User findById(int id) {
		return userRepository.findById(id).get();
	}
	@Transactional
	public User findByUserName(String name) {
		return userRepository.findByUsername(name).get();
	}
	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}

}
