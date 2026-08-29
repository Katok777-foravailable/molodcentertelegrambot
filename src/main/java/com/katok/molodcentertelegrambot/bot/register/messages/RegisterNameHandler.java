package com.katok.molodcentertelegrambot.bot.register.messages;

import com.katok.molodcentertelegrambot.bot.message.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.register.RegisterStatus;
import com.katok.molodcentertelegrambot.bot.telegram.TelegramRegisterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RegisterNameHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramRegisterService telegramRegisterService;

    @Override
    public Set<String> states() {
        return Set.of(RegisterStatus.NAME.name());
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramRegisterService.setName(Updates.userId(update), Updates.chatId(update), update.message().text()));
    }
}
