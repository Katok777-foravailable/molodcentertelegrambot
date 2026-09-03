package com.katok.molodcentertelegrambot.bot.messages;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Set;

public interface FSMUpdateHandler extends UpdateHandler {
    Set<String> states();
}
