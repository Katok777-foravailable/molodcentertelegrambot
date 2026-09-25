package com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.telegram;

import com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.RegisterGlobalCategoryStatus;
import com.katok.molodcentertelegrambot.bot.category.categorypage.callbacks.CategoryPageCallbackHandler;
import com.katok.molodcentertelegrambot.bot.category.categoryregister.CategoryRegisterService;
import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.katok.molodcentertelegrambot.bot.profile.telegram.TelegramProfileService;
import com.katok.molodcentertelegrambot.bot.start.callbacks.StartCallbackHandler;
import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.utils.AccessChars;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramRegisterCategoryService {
    private final UserClient userClient;
    private final TelegramProfileService telegramProfileService;
    private final CategoryRegisterService categoryRegisterService;
    private final FSMService fsmService;

    @Value("${general.no-permission}")
    private String noPermission;
    @Value("${admin-panel.category.send-name}")
    private String sendName;
    @Value("${category.register.timeout}")
    private String timeout;
    @Value("${admin-panel.category.successful}")
    private String successful;
    @Value("${category.name-warn}")
    private String nameWarn;
    @Value("${category.to-page}")
    private String toPage;
    @Value("${general.back-to-menu}")
    private String backToMenu;

    @Value("${permissions.create-global-categories}")
    private int createGlobalCategories;

    public SendMessage startRegister(long chatId, long userId) {
        UserDto userDto = userClient.getUser(userId, null, null).getBody();

        if (userDto == null) {
            return telegramProfileService.getMessage(chatId, userId);
        }

        if (userDto.getAdminRank() < createGlobalCategories) {
            SendMessage sendMessage = new SendMessage(chatId, noPermission);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        categoryRegisterService.startRegister(userId);
        fsmService.updateState(userId, RegisterGlobalCategoryStatus.REGISTER_GLOBAL_CATEGORY_GET_NAME.name());

        SendMessage sendMessage = new SendMessage(chatId, sendName);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }

    public SendMessage setName(long chatId, long userId, String name) {
        if (name.length() > 30) {
            SendMessage sendMessage = new SendMessage(chatId, nameWarn);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        for (char letter : name.toCharArray()) {
            if (AccessChars.accessChars.contains(letter)) {
                continue;
            }

            SendMessage sendMessage = new SendMessage(chatId, nameWarn);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        fsmService.deleteState(userId);

        UserDto userDto = userClient.getUser(userId, null, null).getBody();

        if (userDto == null) {
            return telegramProfileService.getMessage(chatId, userId);
        }

        if (userDto.getAdminRank() < createGlobalCategories) {
            fsmService.deleteState(userId);

            SendMessage sendMessage = new SendMessage(chatId, noPermission);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        CategoryDto categoryDto;
        try {
            categoryRegisterService.setName(userId, name);
            categoryDto = categoryRegisterService.finishRegister(userId);
        } catch (ValueNotFound e) {
            SendMessage sendMessage = new SendMessage(chatId, timeout);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.addRow(
                new InlineKeyboardButton(toPage).callbackData(CategoryPageCallbackHandler.CALLBACK + categoryDto.getExternalId())
        );
        keyboard.addRow(
                new InlineKeyboardButton(backToMenu).callbackData(StartCallbackHandler.CALLBACK)
        );

        SendMessage sendMessage = new SendMessage(chatId, successful);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }
}
