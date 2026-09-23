package com.katok.molodcentertelegrambot.bot.youthcenters.categories.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.categories.telegram.YouthCenterCategoriesPageService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterCategoriesPageCallbackHandler implements CallbackUpdateHandler {
    public final static String CALLBACK = "youth-center-categories-";

    private final TelegramBotExecutor executor;
    private final YouthCenterCategoriesPageService youthCenterCategoriesPageService;

    @Override
    public Set<String> callbacks() {
        return Set.of(CALLBACK);
    }

    @Override
    public void handle(Update update) {
        String[] data = update.callbackQuery().data().substring(CALLBACK.length()).split("-");

        if (data.length < 2) {
            return;
        }

        String externalId = data[0];
        int page;

        try {
            page = Integer.parseInt(data[1]);
        } catch (NumberFormatException ignored) {return;}

        executor.execute(youthCenterCategoriesPageService.getMessage(Updates.chatId(update), Updates.userId(update), externalId, page));
    }
}
