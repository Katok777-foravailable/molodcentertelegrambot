package com.katok.molodcentertelegrambot.bot.register.messages;

import com.katok.molodcentertelegrambot.bot.message.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.register.RegisterStatus;
import com.katok.molodcentertelegrambot.bot.telegram.TelegramRegisterService;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RegisterPhoneNumberHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramRegisterService telegramRegisterService;

    @Override
    public Set<String> states() {
        return Set.of(RegisterStatus.PHONE_NUMBER.name());
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

        executor.execute(telegramRegisterService.setPhoneNumber(Updates.userId(update), Updates.chatId(update), contact.phoneNumber()));
    }
}
