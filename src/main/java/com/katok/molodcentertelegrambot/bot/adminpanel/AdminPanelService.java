package com.katok.molodcentertelegrambot.bot.adminpanel;

import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class AdminPanelService {
    private final UserClient userClient;
    private final InlineKeyboardMarkup adminKeyboard = new InlineKeyboardMarkup();

    @Value("${general.no-permission}")
    private String noPermission;
    @Value("${admin-panel.message}")
    private String message;
    @Value("${admin-panel.create-new-youth-center}")
    private String createNewYouthCenter;

    @PostConstruct
    public void keyboardInit() {
        adminKeyboard.addRow(
                new InlineKeyboardButton(createNewYouthCenter).callbackData("create-new-youth-center")
        );
    }

    public SendMessage getMessage(Long userId, long chatId) {
        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();
        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null || userDto.getAdminRank() < 1) {
            return new SendMessage(chatId, noPermission);
        }

        SendMessage sendMessage = new SendMessage(chatId, MessageFormat.format(message, userDto.getAdminRank()));
        sendMessage.replyMarkup(adminKeyboard);
        return sendMessage;
    }
}
