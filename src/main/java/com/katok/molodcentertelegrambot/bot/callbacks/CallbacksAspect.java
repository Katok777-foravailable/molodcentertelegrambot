package com.katok.molodcentertelegrambot.bot.callbacks;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CallbacksAspect {
    private final TelegramBotExecutor executor;

    @Around("execution(* io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler+.handle(..))")
    public Object handleCallbacks(ProceedingJoinPoint joinPoint) throws Throwable {
        Update update = (Update) joinPoint.getArgs()[0];

        String callbackQueryId = update.callbackQuery().id();
        AnswerCallbackQuery answer = new AnswerCallbackQuery(callbackQueryId);
        executor.execute(answer);

        return joinPoint.proceed();
    }
}
