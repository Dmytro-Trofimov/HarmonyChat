package com.harmonyChat.HarmonyChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmonyChat.HarmonyChat.model.Message;
import com.harmonyChat.HarmonyChat.repository.MessageRepository;

import jakarta.transaction.Transactional;

@Service
public class MessageService {
	
    @Autowired
    private MessageRepository repo;
    
    @Transactional
    public void save(Message message) {
        repo.save(message);
    }
    @Transactional
    public Message findById(int id) {
        return repo.findById(id).get();
    }
    @Transactional
    public List<Message> findByChatId(int chat_id) {
        return repo.findByChatId(chat_id);
    }
}
