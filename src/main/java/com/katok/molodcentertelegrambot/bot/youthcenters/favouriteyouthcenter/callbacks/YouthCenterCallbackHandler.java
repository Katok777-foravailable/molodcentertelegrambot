package com.katok.molodcentertelegrambot.bot.youthcenters.favouriteyouthcenter.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.favouriteyouthcenter.telegram.TelegramYouthCenterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterCallbackHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramYouthCenterService telegramYouthCenterService;

    @Override
    public Set<String> callbacks() {
        return Set.of("favourite-youth-center-page-");
    }

    @Override
    public void handle(Update update) {
        int page;
        String pageString = update.callbackQuery().data().substring(28);
        if (pageString.isEmpty()) {
            page = 0;
        } else {
            page = Integer.parseInt(pageString);
        }

        executor.execute(telegramYouthCenterService.getMessage(Updates.userId(update), Updates.chatId(update), page));
    }
}
