package com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterregister;

import com.katok.molodcentertelegrambot.bot.profile.telegram.TelegramProfileService;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class YouthCenterRegisterSecurity {
    private final TelegramBotExecutor executor;
    private final UserClient userClient;
    private final TelegramProfileService telegramProfileService;

    @Value("${general.no-permission}")
    private String noPermission;
    @Value("${permissions.create-youth-centers}")
    private int permissionRank;

    @Around("within(com.katok.molodcentertelegrambot.bot.youthcenters.youthcenterregister..*) && execution(* io.ksilisk.telegrambot.core.handler.update.UpdateHandler+.handle(..))")
    public Object userAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Update update = (Update) joinPoint.getArgs()[0];
        Long userId = Updates.userId(update);
        long chatId = Updates.userId(update);

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            executor.execute(telegramProfileService.getMessage(chatId, userId));
            return null;
        }

        if (userDto.getAdminRank() < permissionRank) {
            SendMessage sendMessage = new SendMessage(chatId, noPermission);
            sendMessage.setParseMode(ParseMode.MarkdownV2);

            executor.execute(sendMessage);
            return null;
        }

        return joinPoint.proceed();
    }
}
