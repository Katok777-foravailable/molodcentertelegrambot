package com.katok.molodcentertelegrambot.bot.youthcenters.favouriteyouthcenter.telegram;

import com.katok.molodcentertelegrambot.bot.profile.ProfileService;
import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.favouriteyouthcenter.FavouriteYouthCenterClient;
import com.katok.molodcentertelegrambot.services.favouriteyouthcenter.FavouriteYouthCenterDto;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramYouthCenterService {
    private final YouthCenterClient youthCenterClient;
    private final FavouriteYouthCenterClient favouriteYouthCenterClient;
    private final UserClient userClient;
    private final ProfileService profileService;

    @Value("${youth-center.left}")
    private String left;
    @Value("${youth-center.right}")
    private String right;
    @Value("${youth-center.empty}")
    private String empty;
    @Value("${youth-center.location.find-youth-center-by-location}")
    private String findYouthCenterByLocation;
    @Value("${youth-center.your-favourite-youth-centers}")
    private String yourFavouriteYouthCenters;

    public SendMessage getMessage(Long userId, long chatId, int page) {
        ResponseEntity<UserDto> responseEntityUserDto = userClient.getUser(userId, null, null);
        UserDto userDto = responseEntityUserDto.getBody();
        if (responseEntityUserDto.getStatusCode().is4xxClientError() || userDto == null) {
            return profileService.getMessage(userId);
        }

        CustomPage<FavouriteYouthCenterDto> favouriteYouthCenterDtos = favouriteYouthCenterClient.getFavouriteYouthCenters(null, userDto.getId(), page);
        List<YouthCenterDto> youthCenterDtos = new ArrayList<>();

        for (FavouriteYouthCenterDto favouriteYouthCenterDto : favouriteYouthCenterDtos.getContent()) {
            ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenterById(favouriteYouthCenterDto.getYouthCenterId());
            YouthCenterDto youthCenterDto = youthCenterDtoResponseEntity.getBody();

            if (youthCenterDtoResponseEntity.getStatusCode().is4xxClientError() || youthCenterDto == null) {
                favouriteYouthCenterClient.deleteFavouriteYouthCenter(favouriteYouthCenterDto.getId());
                continue;
            }

            youthCenterDtos.add(youthCenterDto);
        }
        InlineKeyboardMarkup youthCenterKeyboard = new InlineKeyboardMarkup();

        for (YouthCenterDto youthCenterDto : youthCenterDtos) {
            youthCenterKeyboard.addRow(
                    new InlineKeyboardButton(youthCenterDto.getName()).callbackData("youth-center-page-" + youthCenterDto.getExternalId())
            );
        }

        youthCenterKeyboard.addRow(
                new InlineKeyboardButton(findYouthCenterByLocation).callbackData("find-youth-center-by-location")
        );
        youthCenterKeyboard.addRow(
                new InlineKeyboardButton(left).callbackData("favourite-youth-center-page-" + Math.max(0, page - 1)),
                new InlineKeyboardButton(String.valueOf(page + 1)).callbackData("easter-egg"),
                new InlineKeyboardButton(right).callbackData("favourite-youth-center-page-" + (page + 1)));

        SendMessage sendMessage = new SendMessage(chatId, yourFavouriteYouthCenters);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.replyMarkup(youthCenterKeyboard);
        return sendMessage;
    }
}
