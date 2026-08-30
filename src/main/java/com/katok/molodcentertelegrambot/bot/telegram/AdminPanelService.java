package com.katok.molodcentertelegrambot.bot.telegram;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminPanelService {
    public SendMessage getMessage(Long userId, long chatId) {
        return null;
    }
}
