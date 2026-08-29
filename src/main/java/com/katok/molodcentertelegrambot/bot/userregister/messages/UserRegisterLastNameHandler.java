package com.katok.molodcentertelegrambot.bot.userregister.messages;

import com.katok.molodcentertelegrambot.bot.message.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.userregister.UserRegisterStatus;
import com.katok.molodcentertelegrambot.bot.telegram.TelegramUserRegisterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRegisterLastNameHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramUserRegisterService telegramUserRegisterService;

    @Override
    public Set<String> states() {
        return Set.of(UserRegisterStatus.LAST_NAME.name());
    }

    @Override
    public void handle(Update update) {
        executor.execute(telegramUserRegisterService.setLastName(Updates.userId(update), Updates.chatId(update), update.message().text()));
    }
}
