package com.katok.molodcentertelegrambot.bot.callbacks;

import com.katok.molodcentertelegrambot.bot.callbacks.rules.CallbackUpdateRule;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.router.CallbackUpdateRouter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomCallbackUpdateRouter implements CallbackUpdateRouter {
    private final List<CallbackUpdateRule> callbackUpdateRuleList;

    @PostConstruct
    public void sortList() {
        callbackUpdateRuleList.sort(Comparator.comparing(CallbackUpdateRule::getOrder));
    }

    @Override
    public boolean supports(Update update) {
        if (update.callbackQuery() == null) {
            return false;
        }

        for (CallbackUpdateRule callbackUpdateRule : callbackUpdateRuleList) {
            if (callbackUpdateRule.match(update)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean route(Update update) {
        if (update.callbackQuery() == null) {
            return false;
        }

        for (CallbackUpdateRule callbackUpdateRule : callbackUpdateRuleList) {
            if (callbackUpdateRule.handle(update)) {
                return true;
            }
        }

        return false;
    }
}
