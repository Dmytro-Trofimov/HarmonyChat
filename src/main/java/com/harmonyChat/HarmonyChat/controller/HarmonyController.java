package com.harmonyChat.HarmonyChat.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

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

	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		User user = userService.findByUserName(principal.getName());
		Hibernate.initialize(user.getAllChats()); // Initialize the collection
		List<String> contactNames = getContactNames(user);

		model.addAttribute("user", user);
		model.addAttribute("contactNames", contactNames);
		model.addAttribute("contact", new User());
		return "home";
	}

	@GetMapping("/profil/{name}")
	public String addContact(@PathVariable String name, Model model, Principal principal) {
	    // Отримати поточного користувача
	    User currentUser = userService.findByUserName(principal.getName());
	    System.out.println("this " + currentUser.getUsername());

	    // Отримати користувача, якого хочемо додати до контактів
	    User contactUser = userService.findByUserName(name);
	    if (contactUser == null) {
	        model.addAttribute("error", "User not found");
	        return "error-page"; // Відобразити сторінку помилки
	        //TODO: .../|\
	        //			|
	    }

	    // Перевірка чи вже існує чат між користувачами
	    boolean contactExists = currentUser.getInitiatedChats().stream()
	        .anyMatch(chat -> chat.getSecondUser().getId() == contactUser.getId())
	        || currentUser.getReceivedChats().stream()
	        .anyMatch(chat -> chat.getFirstUser().getId() == contactUser.getId());

	    Chat chat;
	    if (!contactExists) {
	        // Створення нового чату
	        chat = new Chat();
	        chat.setFirstUser(currentUser);
	        chat.setSecondUser(contactUser);
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
	public Message sendMessage(@RequestBody Message message, Principal principal) throws Exception {
		System.out.println("Sending message: " + message);
		User currentUser = userService.findByUserName(principal.getName());

		// Перевірка існування чату
		Chat chat = chatService.findById(message.getChat_id());
		if (chat == null) {
			throw new Exception("Chat not found");
		}

		// Встановлення даних повідомлення
		message.setAuthor(HtmlUtils.htmlEscape(currentUser.getName()));
		message.setText(HtmlUtils.htmlEscape(message.getText()));
		message.setDate(new Date());

		messageService.save(message);

		return message;
	}

	public List<String> getContactNames(User user) {
		List<String> contactNames = new ArrayList<>();

		for (Chat chat : user.getAllChats()) {
			if (chat.getFirstUser().getId() == user.getId()) {
				contactNames.add(userService.findById(chat.getSecondUser().getId()).getUsername());
			} else if (chat.getSecondUser().getId() == user.getId()) {
				contactNames.add(userService.findById(chat.getFirstUser().getId()).getUsername());
			}
		}

		return contactNames;
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

	
	
	private Chat getExistingChat(User currentUser, User contactUser) {
	    return currentUser.getInitiatedChats().stream()
	        .filter(chat -> chat.getSecondUser().getId() == contactUser.getId())
	        .findFirst()
	        .orElse(
	            currentUser.getReceivedChats().stream()
	                .filter(chat -> chat.getFirstUser().getId() == contactUser.getId())
	                .findFirst()
	                .orElse(null)
	        );
	}


}
