package com.katok.molodcentertelegrambot.bot.userrole.callbacks;

import com.katok.molodcentertelegrambot.bot.userrole.telegram.TelegramChangeUserRoleService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChangeUserRoleCallbackHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramChangeUserRoleService telegramChangeUserRoleService;

    @Override
    public Set<String> callbacks() {
        return Set.of("add-new-user-role-");
    }

    @Override
    public void handle(Update update) {
        String externalId = update.callbackQuery().data().substring(18);

        executor.execute(telegramChangeUserRoleService.startRegister(Updates.chatId(update), Updates.userId(update), externalId));
    }
}
