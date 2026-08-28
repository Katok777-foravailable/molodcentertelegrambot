package com.katok.molodcentertelegrambot.callbacks;

import com.katok.molodcentertelegrambot.telegram.TelegramRegisterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RegisterHandler implements CallbackUpdateHandler {
    private final TelegramRegisterService telegramRegisterService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("register");
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramRegisterService.startRegister(Updates.userId(update), Updates.chatId(update)));
    }
}
