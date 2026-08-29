package com.katok.molodcentertelegrambot.bot.message;

import com.katok.molodcentertelegrambot.bot.fsm.FSMService;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FSMMessageHandler implements MessageUpdateHandler {
    private final List<FSMUpdateHandler> fsmUpdateHandlers;
    private final FSMService fsmService;

    @Override
    public void handle(Update update) {
        Long userId = Updates.userId(update);
        String state = fsmService.getState(userId);

        if (state == null) {
            return;
        }

        for (FSMUpdateHandler fsmUpdateHandler : fsmUpdateHandlers) {
            if (!fsmUpdateHandler.states().contains(state)) {
                continue;
            }

            fsmUpdateHandler.handle(update);
            break;
        }
    }
}
