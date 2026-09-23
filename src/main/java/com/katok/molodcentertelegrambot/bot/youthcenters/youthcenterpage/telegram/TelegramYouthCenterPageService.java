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

        YouthCenterDto youthCenterDto = youthCenterClient.getYouthCenter(externalId).getBody();

        if (youthCenterDto == null) {
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

        UserDto userDto = userClient.getUser(userId, null, null).getBody();

        if (userDto != null) {
            if (userDto.getAdminRank() > 0) {
                keyboard.addRow(getAdminButton(externalId));
            } else {
                CustomPage<UserRoleDto> userRoleDtoCustomPage = userRoleClient.getUserRoleByYouthCenterIdAndUserId(userDto.getId(), youthCenterDto.getId(), 0).getBody();

                if (userRoleDtoCustomPage != null) {
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
