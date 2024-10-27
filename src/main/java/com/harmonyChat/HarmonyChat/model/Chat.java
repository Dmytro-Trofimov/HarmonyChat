package com.harmonyChat.HarmonyChat.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Chat {
	public Chat() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    @Column(name = "first_id")
	private int first_id;
    @Column(name = "second_id")
	private int second_id;
	public int getFirst_id() {
		return first_id;
	}
	public Chat(int first_id, int second_id) {
		super();
		this.first_id = first_id;
		this.second_id = second_id;
	}
	public void setFirst_id(int first_id) {
		this.first_id = first_id;
	}
	public int getSecond_id() {
		return second_id;
	}
	public void setSecond_id(int second_id) {
		this.second_id = second_id;
	}
	@OneToMany(mappedBy = "chat_id", cascade = CascadeType.ALL)
    private List<Message> messages;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    public List<Message> getMessages() {
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
