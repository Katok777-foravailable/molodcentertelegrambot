package com.katok.molodcentertelegrambot.bot.userregister.telegram;

import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.katok.molodcentertelegrambot.bot.userregister.UserRegisterService;
import com.katok.molodcentertelegrambot.bot.userregister.UserRegisterStatus;
import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TelegramUserRegisterService {
    public final static Set<Character> accessChars = Collections.unmodifiableSet(new HashSet<>() {{
        for (char letter : "АаБбВвГгҐґДдЕеЄєЖжЗзИиІіЇїЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЬьЮюЯя'".toCharArray()) {
            add(letter);
        }
    }});

    private final FSMService fsmService;
    private final UserRegisterService userRegisterService;
    private final UserClient userClient;

    @Value("${register.user.already-register}")
    private String alreadyRegister;
    @Value("${register.user.last-name}")
    private String askLastName;
    @Value("${register.user.last-name-warn}")
    private String lastNameWarn;
    @Value("${register.user.name}")
    private String askName;
    @Value("${register.user.name-warn}")
    private String nameWarn;
    @Value("${register.user.contact}")
    private String askContact;
    @Value("${register.user.finish}")
    private String finish;
    @Value("${register.timeout}")
    private String timeout;
    @Value("${register.user.send-contact}")
    private String sendContact;

    private ReplyKeyboardMarkup keyboardMarkup;

    @PostConstruct
    public void initKeyboard() {
        KeyboardButton keyboardButton = new KeyboardButton(sendContact);
        keyboardButton.requestContact(true);

        keyboardMarkup = new ReplyKeyboardMarkup(keyboardButton);
        keyboardMarkup.resizeKeyboard(true);
    }

    public SendMessage startRegister(Long userId, long chatId) {
        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);

        SendMessage sendMessage;

        if (userDtoResponseEntity.hasBody() && !userDtoResponseEntity.getStatusCode().is4xxClientError()) {
            sendMessage = new SendMessage(chatId, alreadyRegister);
        } else {
            sendMessage = new SendMessage(chatId, askLastName);
            userRegisterService.startRegister(userId);
            fsmService.updateState(userId, UserRegisterStatus.USER_REGISTER_LAST_NAME.name());
        }

        return sendMessage;
    }

    public SendMessage setName(Long userId, long chatId, String name) {
        SendMessage sendMessage;

        if (name.length() > 30) {
            return new SendMessage(chatId, nameWarn);
        }
        for (char letter : name.toCharArray()) {
            if (accessChars.contains(letter)) {
                continue;
            }

            return new SendMessage(chatId, nameWarn);
        }

        try {
            userRegisterService.setName(userId, name);

            sendMessage = new SendMessage(chatId, askContact);
            sendMessage.setReplyMarkup(keyboardMarkup);
            fsmService.updateState(userId, UserRegisterStatus.USER_REGISTER_PHONE_NUMBER.name());
        } catch (ValueNotFound e) {
            sendMessage = new SendMessage(chatId, timeout);
        }

        return sendMessage;
    }

    public SendMessage setLastName(Long userId, long chatId, String lastName) {
        SendMessage sendMessage;

        if (lastName.length() > 30) {
            return new SendMessage(chatId, lastNameWarn);
        }
        for (char letter : lastName.toCharArray()) {
            if (accessChars.contains(letter)) {
                continue;
            }

            return new SendMessage(chatId, lastNameWarn);
        }

        try {
            userRegisterService.setLastName(userId, lastName);

            sendMessage = new SendMessage(chatId, askName);
            fsmService.updateState(userId, UserRegisterStatus.USER_REGISTER_NAME.name());
        } catch (ValueNotFound e) {
            sendMessage = new SendMessage(chatId, timeout);
        }

        return sendMessage;
    }

    public SendMessage setPhoneNumber(Long userId, long chatId, String phoneNumber) {
        SendMessage sendMessage;

        try {
            userRegisterService.setPhoneNumber(userId, phoneNumber);
            userRegisterService.finishRegistration(userId);
            fsmService.deleteState(userId);

            sendMessage = new SendMessage(chatId, finish);
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        } catch (ValueNotFound e) {
            sendMessage = new SendMessage(chatId, timeout);
        }

        return sendMessage;
    }
}
