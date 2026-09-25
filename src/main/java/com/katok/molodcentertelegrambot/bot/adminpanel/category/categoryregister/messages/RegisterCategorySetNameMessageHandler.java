package com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.messages;

import com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.RegisterGlobalCategoryStatus;
import com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.telegram.TelegramRegisterCategoryService;
import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RegisterCategorySetNameMessageHandler implements FSMUpdateHandler {
    private final static String STATE = RegisterGlobalCategoryStatus.REGISTER_GLOBAL_CATEGORY_GET_NAME.name();

    private final TelegramBotExecutor executor;
    private final TelegramRegisterCategoryService telegramRegisterCategoryService;

    @Override
    public Set<String> states() {
        return Set.of(STATE);
    }

    @Override
    public void handle(Update update) {
        String message = update.message().text();
        if (message == null) {
            return;
        }

        executor.execute(telegramRegisterCategoryService.setName(Updates.chatId(update), Updates.userId(update), message));
    }
}
