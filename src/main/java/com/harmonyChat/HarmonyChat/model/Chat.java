package com.harmonyChat.HarmonyChat.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

	@ManyToMany
	@JoinTable(
	    name = "chat_participants",
	    joinColumns = @JoinColumn(name = "chat_id"),
	    inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> participants = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "firstUser", referencedColumnName = "id")
	private User firstUser;
	@ManyToOne
	@JoinColumn(name = "secondUser", referencedColumnName = "id")
	private User secondUser;

}
