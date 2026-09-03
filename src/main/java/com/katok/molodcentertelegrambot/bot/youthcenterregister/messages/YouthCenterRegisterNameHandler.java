package com.katok.molodcentertelegrambot.bot.youthcenterregister.messages;

import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.youthcenterregister.YouthCenterRegisterStatus;
import com.katok.molodcentertelegrambot.bot.youthcenterregister.telegram.TelegramRegisterYouthCenterService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterRegisterNameHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramRegisterYouthCenterService telegramRegisterYouthCenterService;

    @Override
    public Set<String> states() {
        return Set.of(YouthCenterRegisterStatus.YOUTH_CENTER_REGISTER_NAME.name());
    }

    @Override
    public void handle(Update update) {
        String text = update.message().text();
        if (text == null) {
            return;
        }

        executor.execute(telegramRegisterYouthCenterService.setName(Updates.userId(update), Updates.chatId(update), text));
    }
}
