package com.harmonyChat.HarmonyChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.harmonyChat.HarmonyChat.model.User;
import com.harmonyChat.HarmonyChat.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void registerUser(String email, String username, String password) {
		User user = new User();
		user.setName(username);
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
	public User findByName(String name) {
		return userRepository.findByName(name).get();
	}
	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}
	public User authenticate(String username, String password) {
		User user = userRepository.findByName(username).get();
		boolean correctData = bCryptPasswordEncoder.encode(user.getPassword()) == password;
		if(correctData) {
			return user;
		}
		return null;
	}
	
	public List<String> getContactNames(User user) {
	    return userRepository.findContactNamesByUserId(user.getId());
	    		/*user.getChats().stream()
	            .map(chat -> chat.getChatNameForUser(user))
	            .toList();*/
	}

}
