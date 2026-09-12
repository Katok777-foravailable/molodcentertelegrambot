package com.katok.molodcentertelegrambot.bot.callbacks.rules;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.order.CoreOrdered;

public interface CallbackUpdateRule extends CoreOrdered {
    boolean match(Update update);
    boolean handle(Update update);
}