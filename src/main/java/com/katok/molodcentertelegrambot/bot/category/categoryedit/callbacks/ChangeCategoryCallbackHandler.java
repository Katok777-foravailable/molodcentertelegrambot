package com.katok.molodcentertelegrambot.bot.category.categoryedit.callbacks;

import com.katok.molodcentertelegrambot.bot.category.categoryedit.telegram.TelegramCategoryEditService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChangeCategoryCallbackHandler implements CallbackUpdateHandler {
    public final static String CALLBACK = "change-category-";

    private final TelegramCategoryEditService telegramCategoryEditService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of(CALLBACK);
    }

    @Override
    public void handle(Update update) {
        String externalId = update.callbackQuery().data().substring(CALLBACK.length());

        executor.execute(telegramCategoryEditService.startEdit(Updates.chatId(update), Updates.userId(update), externalId));
    }
}
