package com.harmonyChat.HarmonyChat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByName(String name);
	
	@Query("SELECT DISTINCT u.name " +
		       "FROM Participant p1 " +
		       "JOIN Participant p2 ON p1.chat.id = p2.chat.id " +
		       "JOIN User u ON p2.user.id = u.id " +
		       "WHERE p1.user.id = :userId AND u.id != :userId")
		List<String> findContactNamesByUserId(@Param("userId") Integer userId);


}
