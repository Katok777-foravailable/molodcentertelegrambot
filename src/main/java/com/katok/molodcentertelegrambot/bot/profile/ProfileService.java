package com.katok.molodcentertelegrambot.bot.profile;

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

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final InlineKeyboardMarkup registerKeyboard = new InlineKeyboardMarkup();
    private final InlineKeyboardMarkup backToMenuKeyboard = new InlineKeyboardMarkup();

    private final UserClient userClient;

    @Value("${profile.message}")
    private String message;
    @Value("${profile.not-found}")
    private String notFound;
    @Value("${profile.register}")
    private String register;
    @Value("${general.back-to-menu}")
    private String backToMenu;
    @Value("${profile.admin-rank}")
    private String adminRank;

    @PostConstruct
    public void initKeyboard() {
        registerKeyboard.addRow(
                new InlineKeyboardButton(register).callbackData("register")
        );

        backToMenuKeyboard.addRow(
                new InlineKeyboardButton(backToMenu).callbackData("start")
        );
    }

    public SendMessage getMessage(long userId) {
        ResponseEntity<UserDto> responseUser = userClient.getUser(userId, null, null);

        UserDto userDto = responseUser.getBody();

        SendMessage sendMessage;

        if (responseUser.getStatusCode().is4xxClientError() || userDto == null) {
            sendMessage = new SendMessage(userId, notFound);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            sendMessage.replyMarkup(registerKeyboard);
        } else {
            String adminRankMessage;
            if (userDto.getAdminRank() > 0) {
                adminRankMessage = MessageFormat.format(adminRank, userDto.getAdminRank());
            } else {
                adminRankMessage = "";
            }

            sendMessage = new SendMessage(userId, MessageFormat.format(message, userDto.getName(), userDto.getLastName(), userDto.getPhoneNumber().replaceAll("\\+", "\\\\+"), userDto.getExternalId(), adminRankMessage));
            sendMessage.parseMode(ParseMode.MarkdownV2);
            sendMessage.replyMarkup(backToMenuKeyboard);
        }

        return sendMessage;
    }
}
