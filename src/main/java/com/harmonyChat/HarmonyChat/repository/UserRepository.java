package com.harmonyChat.HarmonyChat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	
	@EntityGraph(attributePaths = {"initiatedChats", "receivedChats"})
	Optional<User> findByUsername(String username);


}
