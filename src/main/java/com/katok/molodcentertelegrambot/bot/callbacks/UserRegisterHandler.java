package com.katok.molodcentertelegrambot.bot.callbacks;

import com.katok.molodcentertelegrambot.bot.telegram.TelegramUserRegisterService;
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
public class UserRegisterHandler implements CallbackUpdateHandler {
    private final TelegramUserRegisterService telegramUserRegisterService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("register");
    }

    @Override
    public void handle(Update update) {
        String callbackQueryId = update.callbackQuery().id();
        AnswerCallbackQuery answer = new AnswerCallbackQuery(callbackQueryId);
        executor.execute(answer);

        executor.execute(telegramUserRegisterService.startRegister(Updates.userId(update), Updates.chatId(update)));
    }
}
