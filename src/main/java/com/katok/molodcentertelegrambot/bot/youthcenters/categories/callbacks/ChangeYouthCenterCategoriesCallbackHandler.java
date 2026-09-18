package com.katok.molodcentertelegrambot.bot.youthcenters.categories.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.categories.telegram.ChangeYouthCenterCategoriesService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChangeYouthCenterCategoriesCallbackHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor executor;
    private final ChangeYouthCenterCategoriesService changeYouthCenterCategoriesService;

    @Override
    public Set<String> callbacks() {
        return Set.of("change-youth-center-categories-");
    }

    @Override
    public void handle(Update update) {
        String externalId = update.callbackQuery().data().substring(31);

        executor.execute(changeYouthCenterCategoriesService.getMessage(Updates.chatId(update), Updates.userId(update), externalId));
    }
}
