package com.harmonyChat.HarmonyChat.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="harmonymessage")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "chat_id", referencedColumnName = "id")
	private Chat chat;

	@ManyToOne
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	private User author;

	@Column(nullable = false)
	private String text;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

}
