package com.katok.molodcentertelegrambot.bot.youthcenters.categories.telegram;

import com.katok.molodcentertelegrambot.bot.profile.ProfileService;
import com.katok.molodcentertelegrambot.bot.youthcenters.categories.YouthCenterCategorySecurity;
import com.katok.molodcentertelegrambot.services.category.CategoryClient;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeYouthCenterCategoriesService {
    private final CategoryClient categoryClient;
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;
    private final ProfileService profileService;
    private final YouthCenterCategorySecurity youthCenterCategorySecurity;

    @Value("${youth-center.category.list-message}")
    private String listMessage;
    @Value("${youth-center.not-exists}")
    private String notExists;

    public SendMessage getMessage(long chatId, long userId, String externalId) {
        if (externalId == null || externalId.length() > 20) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.parseMode(ParseMode.MarkdownV2);
            return sendMessage;
        }

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            return profileService.getMessage(chatId, userId);
        }

        ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenter(externalId);
        YouthCenterDto youthCenterDto = youthCenterDtoResponseEntity.getBody();

        if (youthCenterDtoResponseEntity.getStatusCode().is4xxClientError() || youthCenterDto == null) {
            SendMessage sendMessage = new SendMessage(chatId, notExists);
            sendMessage.setParseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }


    }
}
