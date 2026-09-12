package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.messages;

import com.katok.molodcentertelegrambot.bot.messages.FSMUpdateHandler;
import com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.telegram.TelegramYouthCenterLocationPageService;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterLocationPickHandler implements FSMUpdateHandler {
    private final TelegramYouthCenterLocationPageService telegramYouthCenterLocationPageService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> states() {
        return Set.of(YouthCenterLocationPickStates.LOCATION_PICK.name());
    }

    @Override
    public void handle(Update update) {
        Location location = update.message().location();
        if (location == null) {
            return;
        }

        executor.execute(telegramYouthCenterLocationPageService.getMessage(Updates.chatId(update), location.latitude(), location.longitude(), 0));
    }
}
