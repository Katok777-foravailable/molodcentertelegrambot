package com.katok.molodcentertelegrambot.bot.youthcenters.category.categoryregister.messages;

import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.youthcenters.category.categoryregister.RegisterYouthCenterCategoryStatus;
import com.katok.molodcentertelegrambot.bot.youthcenters.category.categoryregister.telegram.TelegramRegisterYouthCenterCategoryService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RegisterYouthCenterCategorySetNameMessageHandler implements FSMUpdateHandler {
    public final static String STATE = RegisterYouthCenterCategoryStatus.REGISTER_YOUTH_CENTER_CATEGORY_GET_NAME.name();

    private final TelegramBotExecutor executor;
    private final TelegramRegisterYouthCenterCategoryService telegramRegisterYouthCenterCategoryService;

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

        executor.execute(telegramRegisterYouthCenterCategoryService.setName(Updates.chatId(update), Updates.userId(update), message));
    }
}
