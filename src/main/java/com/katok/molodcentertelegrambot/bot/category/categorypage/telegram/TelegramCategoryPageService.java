package com.katok.molodcentertelegrambot.bot.category.categorypage.telegram;

import com.katok.molodcentertelegrambot.bot.category.categoryedit.callbacks.ChangeCategoryCallbackHandler;
import com.katok.molodcentertelegrambot.bot.start.callbacks.StartCallbackHandler;
import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.category.CategoryClient;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleClient;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class TelegramCategoryPageService {
    private final UserClient userClient;
    private final UserRoleClient userRoleClient;
    private final CategoryClient categoryClient;
    private final YouthCenterClient youthCenterClient;

    @Value("${category.not-exists}")
    private String notExist;
    @Value("${category.page}")
    private String categoryPage;
    @Value("${general.none}")
    private String none;
    @Value("${category.change}")
    private String change;
    @Value("${general.back-to-menu}")
    private String backToMenu;

    @Value("${permissions.control-youth-centers}")
    private int controlYouthCenters;
    @Value("${user-roles.change-categories}")
    private int changeCategories;

    public SendMessage getMessage(long chatId, long userId, String externalId) {
        if (externalId == null || externalId.length() != CategoryDto.EXTERNAL_ID_LENGTH) {
            SendMessage sendMessage = new SendMessage(chatId, notExist);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        ResponseEntity<CategoryDto> categoryDtoResponseEntity = categoryClient.getCategoryByExternalId(externalId);
        CategoryDto categoryDto = categoryDtoResponseEntity.getBody();

        if (categoryDtoResponseEntity.getStatusCode().is4xxClientError() || categoryDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, notExist);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }
        YouthCenterDto youthCenterDto = null;
        if (categoryDto.getYouthCenterId() != null) {
            ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenterById(categoryDto.getYouthCenterId());
            if (!youthCenterDtoResponseEntity.getStatusCode().is4xxClientError()) {
                youthCenterDto = youthCenterDtoResponseEntity.getBody();
            }
        }

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        boolean canChange = false;

        if (!userDtoResponseEntity.getStatusCode().is4xxClientError() && userDto != null) {
            if (userDto.getAdminRank() >= controlYouthCenters) {
                canChange = true;
            } else {
                if (youthCenterDto != null) {
                    CustomPage<UserRoleDto> userRoleDtoCustomPage = userRoleClient.getUserRoleByYouthCenterIdAndUserId(userDto.getId(), youthCenterDto.getId(), 0).getBody();
                    if (userRoleDtoCustomPage != null && !userRoleDtoCustomPage.getContent().isEmpty()) {
                        UserRoleDto userRoleDto = userRoleDtoCustomPage.getContent().getFirst();
                        if (userRoleDto.getRole() >= changeCategories) {
                            canChange = true;
                        }
                    }
                }
            }
        }

        String message = MessageFormat.format(categoryPage, categoryDto.getName(), youthCenterDto == null? none : youthCenterDto.getExternalId(), categoryDto.getExternalId());

        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        if (canChange) {
            keyboard.addRow(new InlineKeyboardButton(change).callbackData(ChangeCategoryCallbackHandler.CALLBACK + categoryDto.getExternalId()));
        }

        keyboard.addRow(new InlineKeyboardButton(backToMenu).callbackData(StartCallbackHandler.CALLBACK));

        sendMessage.setReplyMarkup(keyboard);

        return sendMessage;
    }
}
