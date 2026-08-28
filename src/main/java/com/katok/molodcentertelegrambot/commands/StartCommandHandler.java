package com.katok.molodcentertelegrambot.commands;

import com.katok.molodcentertelegrambot.telegram.StartService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandUpdateHandler {
    private final StartService startService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> commands() {
        return Set.of("/start");
    }

    @Override
    public void handle(Update update) {
        executor.execute(startService.getMessage(Updates.chatId(update)));
    }
}
