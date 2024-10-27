package com.harmonyChat.HarmonyChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
	@Query("SELECT c FROM Chat c WHERE c.first_id = :firstId AND c.second_id = :secondId")
    Chat findByFirstIdAndSecondId(@Param("firstId") int firstId, @Param("secondId") int secondId);

}
