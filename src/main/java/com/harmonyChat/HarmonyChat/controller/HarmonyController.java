package com.harmonyChat.HarmonyChat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.harmonyChat.HarmonyChat.DTO.MessageDTO;
import com.harmonyChat.HarmonyChat.model.Chat;
import com.harmonyChat.HarmonyChat.model.Message;
import com.harmonyChat.HarmonyChat.model.User;
import com.harmonyChat.HarmonyChat.service.ChatService;
import com.harmonyChat.HarmonyChat.service.MessageService;
import com.harmonyChat.HarmonyChat.service.UserService;

import jakarta.transaction.Transactional;

@Controller
public class HarmonyController {
	@Autowired
	private UserService userService;

	@Autowired
	private ChatService chatService;

	@Autowired
	private MessageService messageService;

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String email, String username, String password) {
		userService.registerUser(email, username, password);
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

//	@PostMapping("/login")
//	public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
//	    User user = userService.authenticate(username, password);
//	    if (user == null) {
//	        model.addAttribute("error", "Invalid username or password");
//	        return "login";
//	    }
//	    return "redirect:/home";
//	}


	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		System.out.println("start home");
		User user = userService.findByName(principal.getName());
		Hibernate.initialize(user.getChats()); // Initialize the collection
		List<String> contactNames = userService.getContactNames(user);

		model.addAttribute("user", user);
		model.addAttribute("contactNames", contactNames);
		model.addAttribute("contact", new User());
		return "home";
	}

	@GetMapping("/profil/{name}")
	public String addContact(@PathVariable String name, Model model, Principal principal) {
		// Отримати поточного користувача
		User currentUser = userService.findByName(principal.getName());
		System.out.println("this " + currentUser.getName());

		// Отримати користувача, якого хочемо додати до контактів
		User contactUser = userService.findByName(name);
		if (contactUser == null) {
			model.addAttribute("error", "User not found");
			return "error-page"; // Відобразити сторінку помилки
			// TODO: .../|\
			// 			 |
		}

		// Перевірка чи вже існує чат між користувачами
		boolean contactExists = currentUser.getChats().stream()
				.anyMatch(chat -> chat.getParticipants().contains(contactUser));

		Chat chat;
		if (!contactExists) {
			// Створення нового чату
			chat = new Chat();
			chat.setParticipants(List.of(currentUser, contactUser));
			chatService.save(chat);
		} else {
			// Отримання вже існуючого чату
			chat = getExistingChat(currentUser, contactUser);
		}

		// Отримати повідомлення для чату
		List<Message> messages = messageService.findByChatId(chat.getId());

		// Отримати імена контактів для поточного користувача
		List<String> contactNames = getContactNames(currentUser);

		// Додати атрибути до моделі для відображення на сторінці
		model.addAttribute("contact", contactUser);
		model.addAttribute("contactNames", contactNames);
		model.addAttribute("messages", messages);
		model.addAttribute("user", currentUser);

		return "home";
	}

	@MessageMapping("/send")
	@SendTo("/topic/messages")
	@Transactional
	public Message sendMessage(@Payload MessageDTO messageDTO, Principal principal) throws Exception {
		System.out.println("Receiving message: " + messageDTO);

		User currentUser = userService.findByName(principal.getName());

		Chat chat = chatService.findById(messageDTO.getChat_id());
		if (chat == null) {
			throw new Exception("Chat not found");
		}

		Message message = new Message();
		message.setAuthor(currentUser);
		message.setChat(chat);
		message.setText(HtmlUtils.htmlEscape(messageDTO.getText()));
		message.setDate(new Date());

		messageService.save(message);

		return message;
	}


	public int getChatId(User currentUser, User contactUser) {
		Chat thisChat = chatService.findByUsers(currentUser, contactUser);
		if (thisChat == null) {
			thisChat = chatService.findByUsers(contactUser, currentUser);
			if (thisChat == null) {
				thisChat = new Chat();
			}
		}
		return thisChat.getId();
	}

//	private Chat getExistingChat(User currentUser, User contactUser) {
//		return currentUser.getInitiatedChats().stream()
//				.filter(chat -> chat.getSecondUser().getId() == contactUser.getId())
//				.findFirst()
//				.orElse(
//						currentUser.getReceivedChats().stream()
//
//								.filter(chat -> chat.getFirstUser().getId() == contactUser.getId())
//
//								.findFirst()
//
//								.orElse(null)
//				);
//	}	

}
