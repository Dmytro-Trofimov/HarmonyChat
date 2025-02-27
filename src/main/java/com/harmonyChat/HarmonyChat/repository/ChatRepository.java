package com.harmonyChat.HarmonyChat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.Chat;
import com.harmonyChat.HarmonyChat.model.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

	Chat findByParticipants(List<User> participants);

}
