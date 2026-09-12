package com.katok.molodcentertelegrambot.bot.callbacks.callbacks;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EasterEggCallback implements CallbackUpdateHandler {
    @Override
    public Set<String> callbacks() {
        return Set.of("easter-egg");
    }

    @Override
    public void handle(Update update) {}
}
