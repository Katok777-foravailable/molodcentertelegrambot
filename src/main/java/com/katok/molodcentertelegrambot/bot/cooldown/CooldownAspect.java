package com.katok.molodcentertelegrambot.bot.cooldown;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CooldownAspect {
    private final CooldownService cooldownService;
    private final TelegramBotExecutor executor;

    @Value("${general.cooldown}")
    private String cooldown;

    @Around("execution(* io.ksilisk.telegrambot.core.handler.update.UpdateHandler+.handle(..))")
    public Object handleCooldown(ProceedingJoinPoint joinPoint) throws Throwable {
        Update update = (Update) joinPoint.getArgs()[0];

        Long userId = Updates.userId(update);
        Long chatId = Updates.chatId(update);
        if (userId == null || chatId == null) {
            return joinPoint.proceed();
        }

        if (cooldownService.isCooldown(userId)) {
            if (!cooldownService.isWarning(userId)) {
                executor.execute(new SendMessage(chatId.longValue(), cooldown));
                cooldownService.warn(userId);
            }
            return null;
        }

        cooldownService.addCooldown(userId);

        return joinPoint.proceed();
    }
}
