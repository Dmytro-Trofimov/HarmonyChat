package com.harmonyChat.HarmonyChat.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "harmonyuser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String email;
    private String username;
    private String password;
    private String role;
    @OneToMany(mappedBy = "firstUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> initiatedChats = new ArrayList<>();
    @OneToMany(mappedBy = "secondUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> receivedChats = new ArrayList<>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<Chat> getInitiatedChats() {
		return initiatedChats;
	}
	public void setInitiatedChats(List<Chat> initiatedChats) {
		this.initiatedChats = initiatedChats;
	}
	public List<Chat> getReceivedChats() {
		return receivedChats;
	}
	public void setReceivedChats(List<Chat> receivedChats) {
		this.receivedChats = receivedChats;
	}
	public User(int id, String email, String username, String password, String role, List<Chat> initiatedChats,
			List<Chat> receivedChats) {
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
		this.initiatedChats = initiatedChats;
		this.receivedChats = receivedChats;
	}

	public List<Chat> getAllChats() {
	    List<Chat> allChats = new ArrayList<>();
	    allChats.addAll(initiatedChats);
	    allChats.addAll(receivedChats);
	    return allChats;
	}

	public User() {}
}
