package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterpage.telegram;

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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class TelegramYouthCenterPageService {
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;
    private final UserRoleClient userRoleClient;

    @Value("${general.back-to-menu}")
    private String backToMenuTitle;
    @Value("${youth-center.favourite.make-youth-center-favourite}")
    private String makeYouthCenterFavourite;
    @Value("${youth-center.not-exists}")
    private String notExists;
    @Value("${youth-center.youth-center-page}")
    private String youthCenterPage;
    @Value("${admin-panel.admin-panel}")
    private String adminPanel;

    private InlineKeyboardButton backToMenu;

    @PostConstruct
    public void initKeyboards() {
        backToMenu = new InlineKeyboardButton(backToMenuTitle).callbackData("start");
    }

    public SendMessage getMessage(long chatId, long userId, String externalId) {
        if (externalId == null || externalId.length() != 20) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenter(externalId);
        YouthCenterDto youthCenterDto = youthCenterDtoResponseEntity.getBody();

        if (youthCenterDtoResponseEntity.getStatusCode().is4xxClientError() || youthCenterDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        String message = MessageFormat.format(
                youthCenterPage,
                youthCenterDto.getName(),
                youthCenterDto.getGeoLocation().getLatitude() + " " + youthCenterDto.getGeoLocation().getLongitude(),
                youthCenterDto.getExternalId())
                .replace(".", "\\.");

        SendMessage sendMessage = new SendMessage(chatId, message);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(backToMenu)
                .addRow(new InlineKeyboardButton(makeYouthCenterFavourite).callbackData("make-youth-center-favourite-" + youthCenterDto.getExternalId()));

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (!userDtoResponseEntity.getStatusCode().is4xxClientError() && userDto != null) {
            if (userDto.getAdminRank() > 0) {
                keyboard.addRow(getAdminButton(externalId));
            } else {
                ResponseEntity<CustomPage<UserRoleDto>> userRoleDtoResponseEntity = userRoleClient.getUserRoleByYouthCenterIdAndUserId(userDto.getId(), youthCenterDto.getId(), 0);
                CustomPage<UserRoleDto> userRoleDtoCustomPage = userRoleDtoResponseEntity.getBody();

                if (!userDtoResponseEntity.getStatusCode().is4xxClientError() && userRoleDtoCustomPage != null) {
                    if (!userRoleDtoCustomPage.getContent().isEmpty()) {
                        keyboard.addRow(getAdminButton(externalId));
                    }
                }
            }
        }

        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.setReplyMarkup(keyboard);

        return sendMessage;
    }

    private InlineKeyboardButton getAdminButton(String externalId) {
        return new InlineKeyboardButton(adminPanel).callbackData("youth-center-admin-panel-" + externalId);
    }
}
