package com.katok.molodcentertelegrambot.bot.youthcenters.adminpanel.callbacks;

import com.katok.molodcentertelegrambot.bot.youthcenters.adminpanel.telegram.TelegramYouthCenterAdminPanelService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class YouthCenterAdminPanelCallbackHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor executor;
    private final TelegramYouthCenterAdminPanelService telegramYouthCenterAdminPanelService;

    @Override
    public Set<String> callbacks() {
        return Set.of("youth-center-admin-panel-");
    }

    @Override
    public void handle(Update update) {
        String externalId = update.callbackQuery().data().substring(25);

        executor.execute(telegramYouthCenterAdminPanelService.getMessage(Updates.chatId(update), Updates.userId(update), externalId));
    }
}
