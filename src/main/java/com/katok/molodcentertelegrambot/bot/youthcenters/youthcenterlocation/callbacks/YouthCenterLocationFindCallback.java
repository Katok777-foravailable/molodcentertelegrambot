package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.telegram.TelegramYouthCenterLocationService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterLocationFindCallback implements CallbackUpdateHandler {
    private final TelegramYouthCenterLocationService telegramYouthCenterLocationService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("find-youth-center-by-location");
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramYouthCenterLocationService.getMessage(Updates.chatId(update), Updates.userId(update)));
    }
}
