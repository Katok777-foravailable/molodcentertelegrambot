package com.katok.molodcentertelegrambot.bot.category.categoryedit.telegram;

import com.katok.molodcentertelegrambot.bot.category.categoryedit.CategoryEditService;
import com.katok.molodcentertelegrambot.bot.category.categoryedit.CategoryEditStatus;
import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.katok.molodcentertelegrambot.bot.profile.telegram.TelegramProfileService;
import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.category.CategoryClient;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleClient;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.katok.molodcentertelegrambot.utils.AccessChars;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramCategoryEditService {
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;
    private final CategoryEditService categoryEditService;
    private final FSMService fsmService;
    private final CategoryClient categoryClient;
    private final TelegramProfileService telegramProfileService;
    private final UserRoleClient userRoleClient;

    @Value("${category.not-exists}")
    private String categoryNotExists;
    @Value("${youth-center.not-exists}")
    private String youthCenterNotExists;
    @Value("${general.no-permission}")
    private String noPermission;
    @Value("${category.edit.send-new-name}")
    private String sendNewName;
    @Value("${category.name-warn}")
    private String nameWarn;
    @Value("${category.edit.successful}")
    private String successful;
    @Value("${category.edit.timeout}")
    private String timeout;

    @Value("${permissions.control-youth-centers}")
    private int controlYouthCenters;
    @Value("${user-roles.change-categories}")
    private int changeCategories;

    public SendMessage startEdit(long chatId, long userId, String externalId) {
        if (externalId == null || externalId.length() != CategoryDto.EXTERNAL_ID_LENGTH) {
            SendMessage sendMessage = new SendMessage(chatId, categoryNotExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        UserDto userDto = userClient.getUser(userId, null, null).getBody();
        if (userDto == null) {
            return telegramProfileService.getMessage(chatId, userId);
        }

        CategoryDto categoryDto = categoryClient.getCategoryByExternalId(externalId).getBody();
        if (categoryDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, categoryNotExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        boolean canChange = false;
        if (userDto.getAdminRank() >= controlYouthCenters) {
            canChange = true;
        } else if (categoryDto.getYouthCenterId() != null) {
            YouthCenterDto youthCenterDto = youthCenterClient.getYouthCenterById(categoryDto.getYouthCenterId()).getBody();

            if (youthCenterDto == null) {
                SendMessage sendMessage = new SendMessage(chatId, youthCenterNotExists);
                sendMessage.parseMode(ParseMode.MarkdownV2);

                return sendMessage;
            }

            CustomPage<UserRoleDto> userRoleDtoCustomPage = userRoleClient.getUserRoleByYouthCenterIdAndUserId(userDto.getId(), youthCenterDto.getId(), 0).getBody();
            if (userRoleDtoCustomPage != null && !userRoleDtoCustomPage.getContent().isEmpty() && userRoleDtoCustomPage.getContent().getFirst().getRole() >= changeCategories) {
                canChange = true;
            }
        }

        if (!canChange) {
            SendMessage sendMessage = new SendMessage(chatId, noPermission);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        fsmService.updateState(userId, CategoryEditStatus.CATEGORY_EDIT_NEW_NAME.name());
        categoryEditService.startEdit(userId, categoryDto.getId());

        SendMessage sendMessage = new SendMessage(chatId, sendNewName);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        return sendMessage;
    }

    public SendMessage setNewName(long chatId, long userId, String newCategoryName) {
        for (char letter : newCategoryName.toCharArray()) {
            if (AccessChars.accessChars.contains(letter)) {
                continue;
            }

            SendMessage sendMessage = new SendMessage(chatId, nameWarn);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        fsmService.deleteState(userId);

        SendMessage sendMessage;

        try {
            categoryEditService.setNewName(userId, newCategoryName);
            categoryEditService.finishEdit(userId);

            sendMessage = new SendMessage(chatId, successful);
        } catch (ValueNotFound e) {
            sendMessage = new SendMessage(chatId, timeout);
        }

        sendMessage.parseMode(ParseMode.MarkdownV2);
        return sendMessage;
    }
}
