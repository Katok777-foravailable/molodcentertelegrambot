package com.katok.molodcentertelegrambot.bot.start.callbacks;

import com.katok.molodcentertelegrambot.bot.start.StartService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartCallbackHandler implements CallbackUpdateHandler {
    public final static String CALLBACK = "start";

    private final StartService startService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of(CALLBACK);
    }

    @Override
    public void handle(Update update) {
        executor.execute(startService.getMessage(Updates.userId(update), Updates.chatId(update)));
    }
}
