package com.katok.molodcentertelegrambot.bot.telegram;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartService {
    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

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
    }

    public SendMessage getMessage(long userId) {
        SendMessage sendMessage = new SendMessage(userId, message);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.replyMarkup(keyboard);

        return sendMessage;
    }
}
