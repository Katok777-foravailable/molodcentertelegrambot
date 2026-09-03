package com.katok.molodcentertelegrambot.bot.userregister.callbacks;

import com.katok.molodcentertelegrambot.bot.userregister.telegram.TelegramUserRegisterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRegisterHandler implements CallbackUpdateHandler {
    private final TelegramUserRegisterService telegramUserRegisterService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("register");
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramUserRegisterService.startRegister(Updates.userId(update), Updates.chatId(update)));
    }
}
