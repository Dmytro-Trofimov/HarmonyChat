package com.harmonyChat.HarmonyChat.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="harmonymessage")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int chat_id;
	private String author;
	private String text;
	private Date date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getChat_id() {
		return chat_id;
	}
	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", author=" + author + ", text=" + text + ", date=" + date + ", chat_id=" + chat_id
				+ "]";
	}
	public Message(String author, String text, Date date, int chat_id) {
		super();
		this.author = author;
		this.text = text;
		this.date = date;
		this.chat_id = chat_id;
	}
	public Message() {
		super();
	}

}
