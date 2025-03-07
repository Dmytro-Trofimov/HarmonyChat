package com.harmonyChat.HarmonyChat.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.harmonyChat.HarmonyChat.DTO.MessageDTO;
import com.harmonyChat.HarmonyChat.DTO.MessageResponseDTO;
import com.harmonyChat.HarmonyChat.Repository.ChatRepository;
import com.harmonyChat.HarmonyChat.Service.ChatService;
import com.harmonyChat.HarmonyChat.Service.MessageService;
import com.harmonyChat.HarmonyChat.Service.UserService;
import com.harmonyChat.HarmonyChat.model.Chat;
import com.harmonyChat.HarmonyChat.model.Message;
import com.harmonyChat.HarmonyChat.model.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HarmonyController {
	private final UserService userService;
	private final ChatService chatService;
	private final MessageService messageService;
	private final PasswordEncoder passwordEncoder;
	private final ChatRepository chatRepository;

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String email, String username, String password) {
		userService.registerUser(email, username, passwordEncoder.encode(password));
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
		User user = userService.findByName(principal.getName());
		//Hibernate.initialize(user.getChats()); // Initialize the collection
		List<String> contactNames = userService.getContactNames(user);

		model.addAttribute("user", user);
		model.addAttribute("contactNames", contactNames);
		model.addAttribute("contact", new User());
		return "home";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/profil/{name}")
	public String addContact(@PathVariable String name, Model model, Principal principal) {
		// Отримати поточного користувача
		User currentUser = userService.findByName(principal.getName());

		// Отримати користувача, якого хочемо додати до контактів
		User contactUser = userService.findByName(name);
		if (contactUser == null) {
			model.addAttribute("user", currentUser);
			model.addAttribute("contactNames", userService.getContactNames(currentUser));
			model.addAttribute("contact", new User());
			model.addAttribute("error", "User not found");
			return "home";
		}

		boolean contactExists = chatRepository.existsChatBetweenUsers(currentUser.getId(), contactUser.getId());/*currentUser.getChats().stream()
				.anyMatch(chat -> chat.getParticipants().contains(contactUser));*/
		
		Chat chat;
		if (!contactExists) {
			chat = new Chat();
			chat.setParticipants(List.of(currentUser, contactUser));
			chatService.save(chat);
		} else {
			chat = chatService.findByUsers(List.of(contactUser, currentUser));
		}
		List<Message> messages = messageService.findByChatId(chat.getId());

		List<String> contactNames = userService.getContactNames(currentUser);

		model.addAttribute("contact", contactUser);
		model.addAttribute("contactNames", contactNames);
		model.addAttribute("messages", messages);
		model.addAttribute("user", currentUser);
		model.addAttribute("current_user", currentUser.getId());
		model.addAttribute("chatId", chat.getId());

		return "home";
	}

	@MessageMapping("/send")
	@SendTo("/topic/messages")
	public MessageResponseDTO sendMessage(@Payload MessageDTO messageDTO, Principal principal) {
	    
	    User currentUser = userService.findByName(principal.getName());
	    if (currentUser == null) {
	        throw new IllegalArgumentException("User not found: " + principal.getName());
	    }

	    Chat chat = chatService.findById(messageDTO.getChatId());
	    if (chat == null) {
	        throw new IllegalArgumentException("Chat not found: " + messageDTO.getChatId());
	    }

	    Message message = new Message();
	    message.setAuthor(currentUser);
	    message.setChat(chat);
	    message.setText(URLDecoder.decode(messageDTO.getText(), StandardCharsets.UTF_8));
	    message.setDate(new Date());

	    messageService.save(message);

	    return new MessageResponseDTO(
	        message.getId(), 
	        message.getText(), 
	        currentUser.getId(),
	        chat.getId()
	    );
	}






	public int getChatId(User currentUser, User contactUser) {
		Chat thisChat = chatService.findByUsers(List.of(currentUser, contactUser));
		if (thisChat == null) {
			thisChat = chatService.findByUsers(List.of(contactUser, currentUser));
			if (thisChat == null) {
				thisChat = new Chat();
			}
		}
		return thisChat.getId();
	}
}
