package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.telegram;

import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramYouthCenterLocationPageService {
    private final YouthCenterClient youthCenterClient;

    @Value("${youth-center.location.not-found}")
    private String notFound;
    @Value("${youth-center.location.find}")
    private String find;
    @Value("${youth-center.left}")
    private String left;
    @Value("${youth-center.right}")
    private String right;

    public SendMessage getMessage(long chatId, float latitude, float longitude, int page) {
        CustomPage<YouthCenterDto> youthCenterDtoCustomPage = youthCenterClient.getYouthCentersByLocation(latitude, longitude, 10F, page);
        List<YouthCenterDto> content = youthCenterDtoCustomPage.getContent();

        if (content.isEmpty()) {
            SendMessage sendMessage = new SendMessage(chatId, notFound);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        for (YouthCenterDto youthCenterDto : content) {
            inlineKeyboardMarkup.addRow(
                    new InlineKeyboardButton(youthCenterDto.getName()).callbackData("youth-center-page-" + youthCenterDto.getExternalId())
            );
        }

        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(left).callbackData("youth-center-location-page-" + Math.max(0, page - 1) + "-" + latitude + "-" + longitude),
                new InlineKeyboardButton(String.valueOf(page + 1)).callbackData("easter-egg"),
                new InlineKeyboardButton(right).callbackData("youth-center-location-page-" + (page + 1) + "-" + latitude + "-" + longitude));

        SendMessage sendMessage = new SendMessage(chatId, find);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
