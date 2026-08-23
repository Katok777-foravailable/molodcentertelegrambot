package com.katok.molodcentertelegrambot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartHandler implements CommandUpdateHandler {
    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

    private final TelegramBotExecutor executor;

    @Value("${commands.start.message}")
    private String message;
    @Value("${commands.start.buttons.molod-centers}")
    private String molodCenters;
    @Value("${commands.start.buttons.profile}")
    private String profile;
    @Value("${commands.start.buttons.subscription-events}")
    private String subscriptionEvents;
    @Value("${commands.start.buttons.subscription-categories}")
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

    @Override
    public Set<String> commands() {
        return Set.of("/start");
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(Updates.chatId(update).longValue(), message);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.replyMarkup(keyboard);
        executor.execute(sendMessage);
    }
}
