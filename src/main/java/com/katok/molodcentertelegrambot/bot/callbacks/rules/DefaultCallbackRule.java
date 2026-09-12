package com.katok.molodcentertelegrambot.bot.callbacks.rules;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultCallbackRule implements CallbackUpdateRule {
    private final List<CallbackUpdateHandler> callbackUpdateHandlerList;

    @Override
    public boolean match(Update update) {
        String callbackData = update.callbackQuery().data();

        for (CallbackUpdateHandler callbackUpdateHandler : callbackUpdateHandlerList) {
            for (String callback : callbackUpdateHandler.callbacks()) {
                if (!callbackData.startsWith(callback)) {
                    continue;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean handle(Update update) {
        String callbackData = update.callbackQuery().data();

        for (CallbackUpdateHandler callbackUpdateHandler : callbackUpdateHandlerList) {
            for (String callback : callbackUpdateHandler.callbacks()) {
                if (!callbackData.startsWith(callback)) {
                    continue;
                }

                callbackUpdateHandler.handle(update);
                return true;
            }
        }

        return false;
    }
}
