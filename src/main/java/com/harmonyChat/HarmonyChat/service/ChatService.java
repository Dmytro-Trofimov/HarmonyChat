package com.harmonyChat.HarmonyChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmonyChat.HarmonyChat.model.Chat;
import com.harmonyChat.HarmonyChat.model.User;
import com.harmonyChat.HarmonyChat.repository.ChatRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {

	@Autowired
	private ChatRepository repo;
	
	
	public void save(Chat chat) {
		repo.save(chat);
	}
	public Chat findById(int id) {
		return repo.findById(id).get();
	}
	public Chat findByUsers(User firstUser, User secondUser) {
	    return repo.findByFirstUserAndSecondUser(firstUser.getId(), secondUser.getId());
	}
}
