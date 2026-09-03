package com.katok.molodcentertelegrambot.bot.youthcenterregister.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenterregister.telegram.TelegramRegisterYouthCenterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterRegisterCallback implements CallbackUpdateHandler {
    private final TelegramRegisterYouthCenterService telegramRegisterYouthCenterService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("create-new-youth-center");
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramRegisterYouthCenterService.startRegister(Updates.userId(update), Updates.chatId(update)));
    }
}
