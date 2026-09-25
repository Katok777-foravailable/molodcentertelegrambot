package com.katok.molodcentertelegrambot.bot.youthcenters.category.categoryregister.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.category.categoryregister.telegram.TelegramRegisterYouthCenterCategoryService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RegisterYouthCenterCategoryCallbackHandler implements CallbackUpdateHandler {
    public final static String CALLBACK = "register-youth-center-new-category-";

    private final TelegramBotExecutor executor;
    private final TelegramRegisterYouthCenterCategoryService telegramRegisterYouthCenterCategoryService;

    @Override
    public Set<String> callbacks() {
        return Set.of(CALLBACK);
    }

    @Override
    public void handle(Update update) {
        String externalId = update.callbackQuery().data().substring(CALLBACK.length());
        executor.execute(telegramRegisterYouthCenterCategoryService.startRegister(Updates.chatId(update), Updates.userId(update), externalId));
    }
}
