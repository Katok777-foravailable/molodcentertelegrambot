package com.katok.molodcentertelegrambot.bot.category.categoryedit.messages;

import com.katok.molodcentertelegrambot.bot.category.categoryedit.CategoryEditStatus;
import com.katok.molodcentertelegrambot.bot.category.categoryedit.telegram.TelegramCategoryEditService;
import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChangeCategorySetNewNameMessageHandler implements FSMUpdateHandler {
    private final TelegramCategoryEditService telegramCategoryEditService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> states() {
        return Set.of(CategoryEditStatus.CATEGORY_EDIT_NEW_NAME.name());
    }

    @Override
    public void handle(Update update) {
        String message = update.message().text();
        if (message == null) {
            return;
        }

        executor.execute(telegramCategoryEditService.setNewName(Updates.chatId(update), Updates.userId(update), message));
    }
}
