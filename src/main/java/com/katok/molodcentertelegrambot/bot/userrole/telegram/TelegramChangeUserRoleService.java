package com.katok.molodcentertelegrambot.bot.userrole.telegram;

import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.katok.molodcentertelegrambot.bot.profile.ProfileService;
import com.katok.molodcentertelegrambot.bot.userrole.UserRoleChangeStates;
import com.katok.molodcentertelegrambot.bot.userrole.UserRoleSecurity;
import com.katok.molodcentertelegrambot.bot.userrole.UserRoleService;
import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class TelegramChangeUserRoleService {
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;
    private final ProfileService profileService;
    private final UserRoleSecurity userRoleSecurity;
    private final FSMService fsmService;
    private final UserRoleService userRoleService;

    @Value("${youth-center.not-exists}")
    private String notExists;
    @Value("${user-role.send-external-user-id}")
    private String sendExternalUserId;
    @Value("${user.id-not-exist}")
    private String idNotExist;
    @Value("${user-role.send-user-role}")
    private String sendUserRole;
    @Value("${general.timeout}")
    private String timeout;
    @Value("${user-role.successful}")
    private String successful;
    @Value("${user-role.you-cant}")
    private String youCant;

    @Value("${user-roles.change-user-roles}")
    private int changeUserRolesRank;
    @Value("${permissions.change-user-roles}")
    private int changeUserRolesAdminRank;

    public SendMessage startRegister(long chatId, long adminUserId, String externalId) {
        if (externalId == null || externalId.length() > 20) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(adminUserId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            return profileService.getMessage(chatId, adminUserId);
        }

        ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenter(externalId);
        YouthCenterDto youthCenterDto = youthCenterDtoResponseEntity.getBody();

        if (youthCenterDtoResponseEntity.getStatusCode().is4xxClientError() || youthCenterDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        SendMessage userRoleSecuritySendMessage = userRoleSecurity.canChangeUserRole(chatId, userDto, youthCenterDto);
        if (userRoleSecuritySendMessage != null) {
            return userRoleSecuritySendMessage;
        }

        fsmService.updateState(adminUserId, UserRoleChangeStates.USER_EXTERNAL_ID.name());
        userRoleService.startRegister(adminUserId, externalId);

        SendMessage sendMessage = new SendMessage(chatId, sendExternalUserId);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }

    public SendMessage setUserExternalId(long chatId, long adminUserId, String externalUserId) {
        if (externalUserId == null || externalUserId.length() > 20) {
            SendMessage sendMessage = new SendMessage(chatId, idNotExist);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(null, null, externalUserId);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, idNotExist);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        fsmService.updateState(adminUserId, UserRoleChangeStates.USER_ROLE.name());
        try {
            userRoleService.setUserExternalId(adminUserId, externalUserId);
        } catch (ValueNotFound e) {
            return timeout(chatId);
        }

        SendMessage sendMessage = new SendMessage(chatId, sendUserRole);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }

    public SendMessage setUserRole(long chatId, long adminUserId, short userRole) {
        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(adminUserId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            return profileService.getMessage(chatId, adminUserId);
        }

        if (userRole >= changeUserRolesRank && userDto.getAdminRank() < changeUserRolesAdminRank) {
            SendMessage sendMessage = new SendMessage(chatId, MessageFormat.format(youCant, changeUserRolesRank - 1));
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        fsmService.deleteState(adminUserId);

        try {
            userRoleService.setUserRole(adminUserId, userRole);
            userRoleService.finishRegister(adminUserId);
        } catch (ValueNotFound e) {
            return timeout(chatId);
        }

        SendMessage sendMessage = new SendMessage(chatId, successful);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        return sendMessage;
    }

    private SendMessage timeout(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, timeout);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        return sendMessage;
    }
}
