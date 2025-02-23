package com.harmonyChat.HarmonyChat.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "harmonychat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "firstUser", referencedColumnName = "id")
    private User firstUser;

    @ManyToOne
    @JoinColumn(name = "secondUser", referencedColumnName = "id")
    private User secondUser;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getFirstUser() {
		return firstUser;
	}

	public void setFirstUser(User firstUser) {
		this.firstUser = firstUser;
	}

	public User getSecondUser() {
		return secondUser;
	}

	public void setSecondUser(User secondUser) {
		this.secondUser = secondUser;
	}

	
	public Chat(int id, User firstUser, User secondUser) {
		super();
		this.id = id;
		this.firstUser = firstUser;
		this.secondUser = secondUser;
	}
	public Chat() {}


}
