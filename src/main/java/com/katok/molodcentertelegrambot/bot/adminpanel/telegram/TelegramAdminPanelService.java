package com.katok.molodcentertelegrambot.bot.adminpanel.telegram;

import com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.callbacks.RegisterCategoryCallbackHandler;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class TelegramAdminPanelService {
    private final UserClient userClient;
    private final InlineKeyboardMarkup adminKeyboard = new InlineKeyboardMarkup();

    @Value("${general.no-permission}")
    private String noPermission;
    @Value("${admin-panel.message}")
    private String message;
    @Value("${admin-panel.create-new-youth-center}")
    private String createNewYouthCenter;
    @Value("${admin-panel.create-new-global-category}")
    private String createNewGlobalCategory;

    @PostConstruct
    public void keyboardInit() {
        adminKeyboard.addRow(
                new InlineKeyboardButton(createNewYouthCenter).callbackData("create-new-youth-center")
        );
        adminKeyboard.addRow(
                new InlineKeyboardButton(createNewGlobalCategory).callbackData(RegisterCategoryCallbackHandler.CALLBACK)
        );
    }

    public SendMessage getMessage(Long userId, long chatId) {
        UserDto userDto = userClient.getUser(userId, null, null).getBody();
        if (userDto == null || userDto.getAdminRank() < 1) {
            return new SendMessage(chatId, noPermission);
        }

        SendMessage sendMessage = new SendMessage(chatId, MessageFormat.format(message, userDto.getAdminRank()));
        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.replyMarkup(adminKeyboard);
        return sendMessage;
    }
}
