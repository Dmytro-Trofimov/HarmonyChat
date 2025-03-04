package com.harmonyChat.HarmonyChat.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponseDTO {
    private int id;
    private String text;
    private int authorId;
    private int chatId;
}

