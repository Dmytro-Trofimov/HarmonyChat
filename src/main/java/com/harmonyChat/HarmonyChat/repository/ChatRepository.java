package com.harmonyChat.HarmonyChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

}
