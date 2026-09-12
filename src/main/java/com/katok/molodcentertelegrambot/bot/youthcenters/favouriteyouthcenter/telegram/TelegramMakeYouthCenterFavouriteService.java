package com.katok.molodcentertelegrambot.bot.youthcenters.favouriteyouthcenter.telegram;

import com.katok.molodcentertelegrambot.bot.profile.ProfileService;
import com.katok.molodcentertelegrambot.services.favouriteyouthcenter.FavouriteYouthCenterClient;
import com.katok.molodcentertelegrambot.services.favouriteyouthcenter.FavouriteYouthCenterDtoCreate;
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

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class TelegramMakeYouthCenterFavouriteService {
    private final FavouriteYouthCenterClient favouriteYouthCenterClient;
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;
    private final ProfileService profileService;

    @Value("${youth-center.favourite.successfully-added}")
    private String successful;
    @Value("${youth-center.favourite.already-added}")
    private String alreadyAdded;
    @Value("${youth-center.not-exists}")
    private String notExists;

    public SendMessage getMessage(long userId, long chatId, String externalYouthCenterId) {
        if (externalYouthCenterId == null || externalYouthCenterId.length() != 20) {
            return notExists(chatId);
        }

        ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenter(externalYouthCenterId);
        YouthCenterDto youthCenterDto = youthCenterDtoResponseEntity.getBody();

        if (youthCenterDtoResponseEntity.getStatusCode().is4xxClientError() || youthCenterDto == null) {
            return notExists(chatId);
        }

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(userId, null, null);
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            return profileService.getMessage(userId);
        }

        if (!favouriteYouthCenterClient.getFavouriteYouthCenters(youthCenterDto.getId(), userDto.getId(), 0).getContent().isEmpty()) {
            String message = MessageFormat.format(alreadyAdded, youthCenterDto.getName());

            SendMessage sendMessage = new SendMessage(chatId, message);
            sendMessage.parseMode(ParseMode.MarkdownV2);

            return sendMessage;
        }

        favouriteYouthCenterClient.createFavouriteYouthCenter(new FavouriteYouthCenterDtoCreate(
                youthCenterDto.getId(),
                userDto.getId()
        ));

        String message = MessageFormat.format(successful, youthCenterDto.getName());

        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        return sendMessage;
    }

    private SendMessage notExists(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, notExists);
        sendMessage.parseMode(ParseMode.MarkdownV2);

        return sendMessage;
    }
}
