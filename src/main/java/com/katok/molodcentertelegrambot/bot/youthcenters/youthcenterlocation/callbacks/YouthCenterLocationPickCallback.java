package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterlocation.telegram.TelegramYouthCenterLocationPageService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterLocationPickCallback implements CallbackUpdateHandler {
    private final TelegramYouthCenterLocationPageService telegramYouthCenterLocationPageService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("youth-center-location-page-");
    }

    @Override
    public void handle(Update update) {
        String data = update.callbackQuery().data().substring(27);

        String[] args = data.split("-");

        if (args.length < 3) {
            return;
        }

        int page = Integer.parseInt(args[0]);
        float latitude = Float.parseFloat(args[1]);
        float longitude = Float.parseFloat(args[2]);

        executor.execute(telegramYouthCenterLocationPageService.getMessage(Updates.chatId(update), latitude, longitude, page));
    }
}
