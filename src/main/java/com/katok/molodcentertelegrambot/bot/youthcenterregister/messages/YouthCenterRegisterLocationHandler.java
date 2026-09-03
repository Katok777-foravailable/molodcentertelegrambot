package com.katok.molodcentertelegrambot.bot.youthcenterregister.messages;

import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.youthcenterregister.YouthCenterRegisterStatus;
import com.katok.molodcentertelegrambot.bot.youthcenterregister.telegram.TelegramRegisterYouthCenterService;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterRegisterLocationHandler implements FSMUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramRegisterYouthCenterService telegramRegisterYouthCenterService;

    @Override
    public Set<String> states() {
        return Set.of(YouthCenterRegisterStatus.YOUTH_CENTER_REGISTER_LOCATION.name());
    }

    @Override
    public void handle(Update update) {
        Location location = update.message().location();
        if (location == null) {
            return;
        }

        executor.execute(telegramRegisterYouthCenterService.setLocation(Updates.userId(update), Updates.chatId(update), location));
    }
}
