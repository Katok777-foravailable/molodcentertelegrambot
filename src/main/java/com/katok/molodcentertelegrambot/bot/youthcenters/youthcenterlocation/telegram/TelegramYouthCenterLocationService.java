package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.telegram;

import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.messages.YouthCenterLocationPickStates;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramYouthCenterLocationService {
    private final FSMService fsmService;

    private ReplyKeyboardMarkup sendLocationKeyboard;

    @Value("${general.send-your-location}")
    private String sendYourLocation;
    @Value("${youth-center.location.send-youth-center-location}")
    private String sendYouthCenterLocation;

    @PostConstruct
    public void initKeyboards() {
        sendLocationKeyboard = new ReplyKeyboardMarkup(
                new KeyboardButton(sendYourLocation).requestLocation(true)
        );

        sendLocationKeyboard.resizeKeyboard(true);
    }

    public SendMessage getMessage(long chatId, Long userId) {
        fsmService.updateState(userId, YouthCenterLocationPickStates.LOCATION_PICK.name());

        SendMessage sendMessage = new SendMessage(chatId, sendYouthCenterLocation);
        sendMessage.setReplyMarkup(sendLocationKeyboard);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }
}