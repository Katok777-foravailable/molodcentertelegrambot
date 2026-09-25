package com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.callbacks;

import com.katok.molodcentertelegrambot.bot.adminpanel.category.categoryregister.telegram.TelegramRegisterCategoryService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RegisterCategoryCallbackHandler implements CallbackUpdateHandler {
    public final static String CALLBACK = "register-new-category";

    private final TelegramRegisterCategoryService telegramRegisterCategoryService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of(CALLBACK);
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramRegisterCategoryService.startRegister(Updates.chatId(update), Updates.userId(update)));
    }
}
