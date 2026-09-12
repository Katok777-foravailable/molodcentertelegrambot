package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterpage.telegram;

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

    @Value("${general.back-to-menu}")
    private String backToMenuTitle;
    @Value("${youth-center.favourite.make-youth-center-favourite}")
    private String makeYouthCenterFavourite;
    @Value("${youth-center.not-exists}")
    private String notExists;
    @Value("${youth-center.youth-center-page}")
    private String youthCenterPage;

    private InlineKeyboardButton backToMenu;

    @PostConstruct
    public void initKeyboards() {
        backToMenu = new InlineKeyboardButton(backToMenuTitle).callbackData("start");
    }

    public SendMessage getMessage(long chatId, String externalId) {
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

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                backToMenu
        ).addRow(new InlineKeyboardButton(makeYouthCenterFavourite).callbackData("make-youth-center-favourite-" + youthCenterDto.getExternalId()));

        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.setReplyMarkup(keyboard);

        return sendMessage;
    }
}
