package com.katok.molodcentertelegrambot.bot.userregister.messages;

import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.userregister.UserRegisterStatus;
import com.katok.molodcentertelegrambot.bot.userregister.telegram.TelegramUserRegisterService;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRegisterPhoneNumberHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramUserRegisterService telegramUserRegisterService;

    @Override
    public Set<String> states() {
        return Set.of(UserRegisterStatus.PHONE_NUMBER.name());
    }

    @Override
    public void handle(Update update) {
        Contact contact = update.message().contact();
        if (contact == null) {
            return;
        }

        if (!contact.userId().equals(Updates.userId(update))) {
            return;
        }

        executor.execute(telegramUserRegisterService.setPhoneNumber(Updates.userId(update), Updates.chatId(update), contact.phoneNumber()));
    }
}
