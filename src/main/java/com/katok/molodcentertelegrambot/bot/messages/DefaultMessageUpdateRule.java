package com.katok.molodcentertelegrambot.bot.messages;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultMessageUpdateRule implements MessageUpdateRule {
    private final FSMMessageHandler FSMMessageHandler;

    @Override
    public Matcher<Message> matcher() {
        return (m) -> true;
    }

    @Override
    public UpdateHandler handler() {
        return FSMMessageHandler;
    }
}
