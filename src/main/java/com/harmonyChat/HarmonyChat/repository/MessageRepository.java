package com.harmonyChat.HarmonyChat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
	@Query("SELECT m FROM Message m WHERE m.chat_id = :chat_id")
    List<Message> findByChatId(@Param("chat_id") int chat_id);
}
