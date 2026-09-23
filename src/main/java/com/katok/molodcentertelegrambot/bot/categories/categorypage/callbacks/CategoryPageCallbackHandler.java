package com.katok.molodcentertelegrambot.bot.categories.categorypage.callbacks;

import com.katok.molodcentertelegrambot.bot.categories.categorypage.telegram.TelegramCategoryPageService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CategoryPageCallbackHandler implements CallbackUpdateHandler {
    public final static String CALLBACK = "category-page-";

    private final TelegramCategoryPageService telegramCategoryPageService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of(CALLBACK);
    }

    @Override
    public void handle(Update update) {
        String externalId = update.callbackQuery().data().substring(CALLBACK.length());

        executor.execute(telegramCategoryPageService.getMessage(Updates.chatId(update), Updates.userId(update), externalId));
    }
}
