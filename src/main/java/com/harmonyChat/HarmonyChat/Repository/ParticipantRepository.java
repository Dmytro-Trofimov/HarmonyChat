package com.harmonyChat.HarmonyChat.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harmonyChat.HarmonyChat.model.Participant;
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer>{

}
