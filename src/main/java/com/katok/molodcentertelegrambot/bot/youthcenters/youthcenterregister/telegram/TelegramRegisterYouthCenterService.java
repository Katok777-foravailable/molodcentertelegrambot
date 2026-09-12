package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterregister.telegram;

import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterregister.YouthCenterRegisterService;
import com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterregister.YouthCenterRegisterStatus;
import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramRegisterYouthCenterService {
    private final YouthCenterRegisterService youthCenterRegisterService;
    private final FSMService fsmService;

    @Value("${register.youth-center.name}")
    private String youthCenterName;
    @Value("${register.youth-center.send-location}")
    private String sendLocation;
    @Value("${general.send-your-location}")
    private String sendYourLocation;
    @Value("${register.youth-center.finish}")
    private String finish;
    @Value("${register.timeout}")
    private String timeout;

    private ReplyKeyboardMarkup keyboardSendLocationMarkup;

    @PostConstruct
    public void initKeyboard() {
        KeyboardButton keyboardButton = new KeyboardButton(sendYourLocation);
        keyboardButton.requestLocation(true);

        keyboardSendLocationMarkup = new ReplyKeyboardMarkup(keyboardButton);
        keyboardSendLocationMarkup.resizeKeyboard(true);
    }

    public SendMessage startRegister(Long userId, long chatId) {
        youthCenterRegisterService.startRegister(userId);
        fsmService.updateState(userId, YouthCenterRegisterStatus.YOUTH_CENTER_REGISTER_NAME.name());

        SendMessage sendMessage = new SendMessage(chatId, youthCenterName);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }

    public SendMessage setName(Long userId, long chatId, String name) {
        SendMessage sendMessage;

        try {
            youthCenterRegisterService.setName(userId, name);
            fsmService.updateState(userId, YouthCenterRegisterStatus.YOUTH_CENTER_REGISTER_LOCATION.name());

            sendMessage = new SendMessage(chatId, sendLocation);
            sendMessage.setReplyMarkup(keyboardSendLocationMarkup);
        } catch (ValueNotFound e) {
            sendMessage = new SendMessage(chatId, timeout);
        }
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }

    public SendMessage setLocation(Long userId, long chatId, Location location) {
        SendMessage sendMessage;

        try {
            youthCenterRegisterService.setLatitude(userId, location.latitude());
            youthCenterRegisterService.setLongitude(userId, location.longitude());
            youthCenterRegisterService.finishRegistration(userId);
            fsmService.deleteState(userId);

            sendMessage = new SendMessage(chatId, finish);
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        } catch (ValueNotFound e) {
            sendMessage = new SendMessage(chatId, timeout);
        }
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }
}
