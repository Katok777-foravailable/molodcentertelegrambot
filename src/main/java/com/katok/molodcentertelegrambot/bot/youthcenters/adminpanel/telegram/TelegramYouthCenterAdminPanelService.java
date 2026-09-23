package com.katok.molodcentertelegrambot.bot.youthcenters.adminpanel.telegram;

import com.katok.molodcentertelegrambot.bot.profile.ProfileService;
import com.katok.molodcentertelegrambot.bot.youthcenters.categories.callbacks.YouthCenterCategoriesPageCallbackHandler;
import com.katok.molodcentertelegrambot.services.CustomPage;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramYouthCenterAdminPanelService {
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;
    private final UserRoleClient userRoleClient;
    private final ProfileService profileService;

    @Value("${youth-center.admin-panel.message}")
    private String message;
    @Value("${youth-center.not-exists}")
    private String notExists;
    @Value("${general.no-permission}")
    private String noPermission;
    @Value("${youth-center.admin-panel.change-user-role}")
    private String addUserRole;
    @Value("${general.back-to-menu}")
    private String backToMenu;
    @Value("${youth-center.admin-panel.change-category}")
    private String changeCategory;

    @Value("${user-roles.change-user-roles}")
    private int changeUserRoles;
    @Value("${user-roles.change-categories}")
    private int changeCategories;
    @Value("${permissions.change-user-roles}")
    private int adminChangeUserRoles;
    @Value("${permissions.control-youth-centers}")
    private int controlYouthCenters;

    public SendMessage getMessage(long chatId, long userId, String externalId) {
        if (externalId == null) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            return profileService.getMessage(chatId, userId);
        }
        short adminRank = userDto.getAdminRank();

        ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenter(externalId);
        YouthCenterDto youthCenterDto = youthCenterDtoResponseEntity.getBody();

        if (youthCenterDtoResponseEntity.getStatusCode().is4xxClientError() || youthCenterDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        ResponseEntity<CustomPage<UserRoleDto>> userRoleDtoResponseEntity = userRoleClient.getUserRoleByYouthCenterIdAndUserId(userDto.getId(), youthCenterDto.getId(), 0);
        CustomPage<UserRoleDto> userRoleDtoCustomPage = userRoleDtoResponseEntity.getBody();

        short userRole = 0;

        if (!userRoleDtoResponseEntity.getStatusCode().is4xxClientError() && userRoleDtoCustomPage != null) {
            List<UserRoleDto> userRoleDtos = userRoleDtoCustomPage.getContent();
            if (userRoleDtos != null && !userRoleDtos.isEmpty()) {
                userRole = userRoleDtos.getFirst().getRole();
            }
        }

        if (userRole <= 0 && adminRank <= 0) {
            SendMessage sendMessage = new SendMessage(chatId, noPermission);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        String resultMessage = MessageFormat.format(message, adminRank, userRole);

        SendMessage sendMessage = new SendMessage(chatId, resultMessage);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        if (userRole >= changeUserRoles || adminRank >= adminChangeUserRoles) {
            keyboard.addRow(new InlineKeyboardButton(addUserRole).callbackData("add-new-user-role-" + externalId));
        }
        if (userRole >= changeCategories || adminRank >= controlYouthCenters) {
            keyboard.addRow(new InlineKeyboardButton(changeCategory).callbackData(YouthCenterCategoriesPageCallbackHandler.CALLBACK + externalId + "-0"));
        }

        keyboard.addRow(new InlineKeyboardButton(backToMenu).callbackData("start"));

        sendMessage.setReplyMarkup(keyboard);

        return sendMessage;
    }
}
