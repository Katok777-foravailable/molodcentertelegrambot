package com.katok.molodcentertelegrambot.bot.youthcenters.category.categorypage.telegram;

import com.katok.molodcentertelegrambot.bot.category.categorypage.callbacks.CategoryPageCallbackHandler;
import com.katok.molodcentertelegrambot.bot.profile.telegram.TelegramProfileService;
import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class TelegramYouthCenterCategoriesPageService {
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;
    private final TelegramProfileService telegramProfileService;

    @Value("${youth-center.category.list-message}")
    private String listMessage;
    @Value("${youth-center.not-exists}")
    private String notExists;
    @Value("${category.add-new-category}")
    private String addNewCategory;

    public SendMessage getMessage(long chatId, long userId, String externalId, int page) {
        if (externalId == null || externalId.length() > 20) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        UserDto userDto = userClient.getUser(userId, null, null).getBody();

        if (userDto == null) {
            return telegramProfileService.getMessage(chatId, userId);
        }

        YouthCenterDto youthCenterDto = youthCenterClient.getYouthCenter(externalId).getBody();

        if (youthCenterDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.setParseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        String message = MessageFormat.format(listMessage, youthCenterDto.getName());

        SendMessage sendMessage = new SendMessage(chatId, message);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        keyboard.addRow(new InlineKeyboardButton(addNewCategory).callbackData("youth-center-add-new-category-" + youthCenterDto.getExternalId()));

        CustomPage<CategoryDto> categoryDtoCustomPage = youthCenterClient.getCategoriesByYouthCenter(youthCenterDto.getId(), page);

        for (CategoryDto categoryDto : categoryDtoCustomPage.getContent()) {
            keyboard.addRow(new InlineKeyboardButton(categoryDto.getName()).callbackData(CategoryPageCallbackHandler.CALLBACK + categoryDto.getExternalId()));
        }

        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.setReplyMarkup(keyboard);

        return sendMessage;
    }
}
