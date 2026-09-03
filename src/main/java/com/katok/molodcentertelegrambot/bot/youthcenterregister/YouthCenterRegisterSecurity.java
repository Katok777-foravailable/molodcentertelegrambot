package com.katok.molodcentertelegrambot.bot.youthcenterregister;

import com.katok.molodcentertelegrambot.bot.profile.ProfileService;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.pengrad.telegrambot.model.Update;
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
    private final ProfileService profileService;

    @Value("${general.no-permission}")
    private String noPermission;
    @Value("${permissions.create-and-modify-youth-centers}")
    private int permissionRank;

    @Around("within(com.katok.molodcentertelegrambot.bot.youthcenterregister..*) && execution(* io.ksilisk.telegrambot.core.handler.update.UpdateHandler+.handle(..))")
    public Object userAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Update update = (Update) joinPoint.getArgs()[0];
        Long userId = Updates.userId(update);
        long chatId = Updates.userId(update);

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            executor.execute(profileService.getMessage(userId));
            return null;
        }

        if (userDto.getAdminRank() < permissionRank) {
            executor.execute(new SendMessage(chatId, noPermission));
            return null;
        }

        return joinPoint.proceed();
    }
}
