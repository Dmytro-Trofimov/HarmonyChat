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
public class AuthController {
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
    public String welcome(Model model, Principal principal) {
        User user = userService.findByUserName(principal.getName());
        Hibernate.initialize(user.getChats()); // Initialize the collection
        List<String> contactNames = getContactNames(user);
        
        model.addAttribute("user", user);
        model.addAttribute("contactNames", contactNames);
        model.addAttribute("contact", new User());
        return "home";
    }



    @GetMapping("/profil/{name}")
    public String addContact(@PathVariable String name, Model model, Principal principal) {
        // Fetch the current user
        User currentUser = userService.findByUserName(principal.getName());
        
        // Fetch the user to be added as contact
        User contactUser = userService.findByUserName(name);
        if(contactUser==null) {
        	model.addAttribute("contact", new User());
            model.addAttribute("contactNames", new ArrayList<>());
            model.addAttribute("messages", new ArrayList<>());
            return "home";
        }
        // Check if the contact already exists
        boolean contactExists = currentUser.getChats().stream()
            .anyMatch(chat -> (chat.getFirst_id() == contactUser.getId() || chat.getSecond_id() == contactUser.getId()));

        // If contact doesn't exist, create a new chat
        if (!contactExists) {
            Chat newChat = new Chat();
            newChat.setFirst_id(currentUser.getId());
            newChat.setSecond_id(contactUser.getId());
            chatService.save(newChat); // Save the new chat

            currentUser.getChats().add(newChat); // Add new chat to user's chats
            userService.save(currentUser); // Update the user
            
            contactUser.getChats().add(newChat);
            userService.save(contactUser);
        }
        int chatId = getChatId(currentUser, contactUser);
        List<Message> messages = messageService.findByChatId(chatId);
        
        List<String> contactNames = getContactNames(currentUser);
        model.addAttribute("contact", contactUser);
        model.addAttribute("contactNames", contactNames);
        model.addAttribute("messages", messages);
        return "home";
    }
    
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    @Transactional
    public Message sendMessage(@RequestBody Message message, Principal principal) throws Exception {
        // Fetch the current user
        User currentUser = userService.findByUserName(principal.getName());
        System.out.println("=====Current user: " + currentUser);
        System.out.println(message);
        
        
        String name =message.getChat_id()+"";
        
        User contactUser = userService.findByUserName(name);
        System.out.println("Contact user: " + contactUser);
        
        message.setAuthor(HtmlUtils.htmlEscape(currentUser.getUsername()));
        message.setText(HtmlUtils.htmlEscape(message.getText()));
        message.setDate(new Date());
        int chatId = getChatId(currentUser, contactUser);
        
        System.out.println("Chat ID: " + chatId);
        
        message.setChat_id(chatId);
        
        System.out.println("Saving message...");
        messageService.save(message);
        
        return message;
    }



    
    
    public List<String> getContactNames(User user) {
        List<String> contactNames = new ArrayList<>();

        for (Chat chat : user.getChats()) {
            if (chat.getFirst_id() == user.getId()) {
                contactNames.add(userService.findById(chat.getSecond_id()).getUsername());
            } else if (chat.getSecond_id() == user.getId()) {
                contactNames.add(userService.findById(chat.getFirst_id()).getUsername());
            }
        }

        return contactNames;
    }
    public int getChatId(User currentUser, User contactUser) {
        Chat thisChat = chatService.findByFirstIdAndSecondId(currentUser.getId(), contactUser.getId());
        if(thisChat==null) {
        	thisChat = chatService.findByFirstIdAndSecondId(contactUser.getId(), currentUser.getId());
        	if(thisChat==null) {
        		thisChat = new Chat();
        	}
        }
        return thisChat.getId();
    }
    

}

