package com.katok.molodcentertelegrambot.bot.adminpanel.callbacks;

import com.katok.molodcentertelegrambot.bot.adminpanel.telegram.TelegramAdminPanelService;
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
    private final TelegramAdminPanelService telegramAdminPanelService;

    @Override
    public Set<String> callbacks() {
        return Set.of("admin-panel");
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramAdminPanelService.getMessage(Updates.userId(update), Updates.chatId(update)));
    }
}
