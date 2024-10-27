package com.harmonyChat.HarmonyChat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmonyChat.HarmonyChat.model.Chat;
import com.harmonyChat.HarmonyChat.repository.ChatRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {

	@Autowired
	private ChatRepository repo;
	@Transactional
	public void save(Chat chat) {
		repo.save(chat);
	}
	@Transactional
	public Chat findById(int id) {
		return repo.findById(id).get();
	}
	@Transactional
	public Chat findByFirstIdAndSecondId(int firstId, int secondId) {
        return repo.findByFirstIdAndSecondId(firstId, secondId);
    }

}
