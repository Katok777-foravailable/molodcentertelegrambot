package com.katok.molodcentertelegrambot.bot.youthcenters.category.categoryregister.telegram;

import com.katok.molodcentertelegrambot.bot.category.categorypage.callbacks.CategoryPageCallbackHandler;
import com.katok.molodcentertelegrambot.bot.category.categoryregister.CategoryRegisterService;
import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.katok.molodcentertelegrambot.bot.profile.telegram.TelegramProfileService;
import com.katok.molodcentertelegrambot.bot.start.callbacks.StartCallbackHandler;
import com.katok.molodcentertelegrambot.bot.youthcenters.category.categoryregister.RegisterYouthCenterCategoryStatus;
import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleClient;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
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
public class TelegramRegisterYouthCenterCategoryService {
    private final UserClient userClient;
    private final TelegramProfileService telegramProfileService;
    private final CategoryRegisterService categoryRegisterService;
    private final FSMService fsmService;
    private final YouthCenterClient youthCenterClient;
    private final UserRoleClient userRoleClient;

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
    @Value("${youth-center.not-exists}")
    private String notExists;

    @Value("${permissions.create-global-categories}")
    private int createGlobalCategories;
    @Value("${user-roles.change-categories}")
    private int changeCategories;

    public SendMessage startRegister(long chatId, long userId, String externalId) {
        if (externalId == null || externalId.length() != YouthCenterDto.EXTERNAL_ID_LENGTH) {
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
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        boolean canRegister = false;

        if (userDto.getAdminRank() >= createGlobalCategories) {
            canRegister = true;
        } else {
            CustomPage<UserRoleDto> userRoleDtoCustomPage = userRoleClient.getUserRoleByYouthCenterIdAndUserId(userDto.getId(), youthCenterDto.getId(), 0).getBody();
            if (userRoleDtoCustomPage != null && !userRoleDtoCustomPage.getContent().isEmpty()) {
                UserRoleDto userRoleDto = userRoleDtoCustomPage.getContent().getFirst();

                canRegister = userRoleDto.getRole() >= changeCategories;
            }
        }

        if (!canRegister) {
            SendMessage sendMessage = new SendMessage(chatId, noPermission);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        categoryRegisterService.startRegister(userId);
        categoryRegisterService.setYouthCenterId(userId, youthCenterDto.getId());
        fsmService.updateState(userId, RegisterYouthCenterCategoryStatus.REGISTER_YOUTH_CENTER_CATEGORY_GET_NAME.name());

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
