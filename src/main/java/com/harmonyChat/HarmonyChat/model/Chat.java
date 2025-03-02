package com.harmonyChat.HarmonyChat.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "harmonychat")
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @Column(nullable = true)
    private String name;
	
	@ManyToMany
	@JoinTable(
	    name = "participants",
	    joinColumns = @JoinColumn(name = "chat_id"),
	    inverseJoinColumns = @JoinColumn(name = "user_id")
	)
    @ToString.Exclude
    @JsonIgnore
	private List<User> participants = new ArrayList<>();
	
	
    public boolean isGroupChat() {
        return name != null;
    }

    public String getChatNameForUser(User user) {
        if (isGroupChat()) {
            return name;
        }
        return participants.stream()
                .filter(u -> !u.equals(user))
                .map(User::getName)
                .findFirst()
                .orElse("Unknown Chat");
    }
}
