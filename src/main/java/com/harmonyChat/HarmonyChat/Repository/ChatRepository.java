package com.harmonyChat.HarmonyChat.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.Chat;
import com.harmonyChat.HarmonyChat.model.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

	@Query("SELECT c FROM Chat c JOIN c.participants p WHERE p IN :users GROUP BY c HAVING COUNT(p) = :size")
	Chat findByParticipants(@Param("users") List<User> users, @Param("size") long size);

	@Query("SELECT COUNT(c) > 0 " +
		       "FROM Chat c JOIN c.participants p1 JOIN c.participants p2 " +
		       "WHERE p1.id = :currentUserId AND p2.id = :contactUserId")
		boolean existsChatBetweenUsers(@Param("currentUserId") int currentUserId, 
		                               @Param("contactUserId") int contactUserId);

}
