package com.katok.molodcentertelegrambot.bot.userrole.messages;

import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.userrole.UserRoleChangeStates;
import com.katok.molodcentertelegrambot.bot.userrole.telegram.TelegramChangeUserRoleService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class SetUserRoleMessageHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramChangeUserRoleService telegramChangeUserRoleService;

    @Override
    public Set<String> states() {
        return Set.of(UserRoleChangeStates.USER_ROLE.name());
    }

    @Override
    public void handle(Update update) {
        String message = update.message().text();
        if (message == null) {
            return;
        }

        short userRole;

        try {
             userRole = Short.parseShort(message);
        } catch (NumberFormatException ignored) {
            return;
        }

        if (userRole < 0) {
            return;
        }

        executor.execute(telegramChangeUserRoleService.setUserRole(Updates.chatId(update), Updates.userId(update), userRole));
    }
}
