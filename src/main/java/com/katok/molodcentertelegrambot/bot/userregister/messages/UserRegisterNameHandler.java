package com.katok.molodcentertelegrambot.bot.userregister.messages;

import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.userregister.UserRegisterStatus;
import com.katok.molodcentertelegrambot.bot.userregister.telegram.TelegramUserRegisterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRegisterNameHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramUserRegisterService telegramUserRegisterService;

    @Override
    public Set<String> states() {
        return Set.of(UserRegisterStatus.USER_REGISTER_NAME.name());
    }

    @Override
    public void handle(Update update) {
        String text = update.message().text();
        if (text == null) {
            return;
        }

        executor.execute(telegramUserRegisterService.setName(Updates.userId(update), Updates.chatId(update), text));
    }
}
