package com.katok.molodcentertelegrambot.message;

import com.pengrad.telegrambot.model.Update;

import java.util.Set;

public interface FSMUpdateHandler {
    Set<String> states();
    void handle(Update update);
}
