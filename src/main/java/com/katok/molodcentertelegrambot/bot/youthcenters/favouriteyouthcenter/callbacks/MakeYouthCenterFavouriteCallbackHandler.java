package com.katok.molodcentertelegrambot.bot.youthcenters.favouriteyouthcenter.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.favouriteyouthcenter.telegram.TelegramMakeYouthCenterFavouriteService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class MakeYouthCenterFavouriteCallbackHandler implements CallbackUpdateHandler {
    private final TelegramMakeYouthCenterFavouriteService telegramMakeYouthCenterFavouriteService;
    private final TelegramBotExecutor executor;

    @Override
    public Set<String> callbacks() {
        return Set.of("make-youth-center-favourite-");
    }

    @Override
    public void handle(Update update) {
        String externalId = update.callbackQuery().data().substring(28);

        executor.execute(telegramMakeYouthCenterFavouriteService.getMessage(
                Updates.userId(update),
                Updates.chatId(update),
                externalId
        ));
    }
}
