package com.katok.molodcentertelegrambot.bot.start;

import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StartService {
    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
    private final InlineKeyboardMarkup adminKeyboard = new InlineKeyboardMarkup();

    private final UserClient userClient;

    @Value("${start.message}")
    private String message;
    @Value("${start.buttons.molod-centers}")
    private String molodCenters;
    @Value("${start.buttons.profile}")
    private String profile;
    @Value("${start.buttons.subscription-events}")
    private String subscriptionEvents;
    @Value("${start.buttons.subscription-categories}")
    private String subscriptionCategories;
    @Value("${start.buttons.admin-panel}")
    private String adminPanel;

    @PostConstruct
    public void initKeyboard() {
        keyboard.addRow(
                new InlineKeyboardButton(molodCenters).callbackData("molod-centers"),
                new InlineKeyboardButton(profile).callbackData("profile")
        );

        keyboard.addRow(
                new InlineKeyboardButton(subscriptionEvents).callbackData("subscription-events"),
                new InlineKeyboardButton(subscriptionCategories).callbackData("subscription-categories")
        );

        adminKeyboard.addRow(
                new InlineKeyboardButton(molodCenters).callbackData("molod-centers"),
                new InlineKeyboardButton(profile).callbackData("profile")
        );

        adminKeyboard.addRow(
                new InlineKeyboardButton(subscriptionEvents).callbackData("subscription-events"),
                new InlineKeyboardButton(subscriptionCategories).callbackData("subscription-categories")
        );

        adminKeyboard.addRow(
                new InlineKeyboardButton(adminPanel).callbackData("admin-panel")
        );
    }

    public SendMessage getMessage(long userId, long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        if (userDtoResponseEntity.hasBody() && !userDtoResponseEntity.getStatusCode().is4xxClientError() && Objects.requireNonNull(userDtoResponseEntity.getBody()).getAdminRank() > 0) {
            sendMessage.replyMarkup(adminKeyboard);
        } else {
            sendMessage.replyMarkup(keyboard);
        }

        return sendMessage;
    }
}
