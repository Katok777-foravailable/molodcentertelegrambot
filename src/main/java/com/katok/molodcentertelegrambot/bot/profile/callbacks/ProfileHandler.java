package com.katok.molodcentertelegrambot.bot.profile.callbacks;

import com.katok.molodcentertelegrambot.bot.profile.telegram.TelegramProfileService;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProfileHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramProfileService telegramProfileService;

    @Override
    public Set<String> callbacks() {
        return Set.of("profile");
    }

    @Override
    public void handle(Update update) {
        String callbackQueryId = update.callbackQuery().id();
        AnswerCallbackQuery answer = new AnswerCallbackQuery(callbackQueryId);
        executor.execute(answer);

        executor.execute(telegramProfileService.getMessage(Updates.chatId(update), Updates.userId(update)));
    }
}
