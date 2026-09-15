package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterpage.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterpage.telegram.TelegramYouthCenterPageService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TelegramYouthCenterPageHandler implements CallbackUpdateHandler {
    private final TelegramYouthCenterPageService telegramYouthCenterPageService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("youth-center-page-");
    }

    @Override
    public void handle(Update update) {
        String callbackData = update.callbackQuery().data();
        String externalId = callbackData.substring(18);

        executor.execute(telegramYouthCenterPageService.getMessage(Updates.chatId(update), Updates.userId(update), externalId));
    }
}
