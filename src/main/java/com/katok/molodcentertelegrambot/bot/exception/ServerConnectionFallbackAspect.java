package com.katok.molodcentertelegrambot.bot.exception;

import com.pengrad.telegrambot.request.SendMessage;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ServerConnectionFallbackAspect {
    @Value("${general.technical-issues}")
    private String technicalIssues;

    @Around("execution(com.pengrad.telegrambot.request.SendMessage com.katok.molodcentertelegrambot.bot..*(..))")
    public Object handleConnectionErrors(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (RetryableException e) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = methodSignature.getParameterNames();

            if (parameterNames == null) {
                throw e;
            }

            for (int i = 0; i < parameterNames.length; i++) {
                if (!parameterNames[i].equalsIgnoreCase("chatId")) {
                    continue;
                }

                return new SendMessage((long) joinPoint.getArgs()[i], technicalIssues);
            }

            for (int i = 0; i < parameterNames.length; i++) {
                if (!parameterNames[i].equalsIgnoreCase("userId")) {
                    continue;
                }

                return new SendMessage((long) joinPoint.getArgs()[i], technicalIssues);
            }

            throw e;
        }
    }
}
