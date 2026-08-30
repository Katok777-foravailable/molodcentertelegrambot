package com.katok.molodcentertelegrambot.bot.callbacks;

import com.katok.molodcentertelegrambot.bot.telegram.AdminPanelService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminPanelCallbackHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor executor;
    private final AdminPanelService adminPanelService;

    @Override
    public Set<String> callbacks() {
        return Set.of("admin-panel");
    }

    @Override
    public void handle(Update update) {
        executor.execute(adminPanelService.getMessage(Updates.userId(update), Updates.chatId(update)));
    }
}
