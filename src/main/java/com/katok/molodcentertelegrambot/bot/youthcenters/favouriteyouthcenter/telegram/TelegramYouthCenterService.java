package com.katok.molodcentertelegrambot.bot.youthcenters.favouriteyouthcenter.telegram;

import com.katok.molodcentertelegrambot.bot.profile.telegram.TelegramProfileService;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramYouthCenterService {
    private final YouthCenterClient youthCenterClient;
    private final FavouriteYouthCenterClient favouriteYouthCenterClient;
    private final UserClient userClient;
    private final TelegramProfileService telegramProfileService;

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
        UserDto userDto = userClient.getUser(userId, null, null).getBody();
        InlineKeyboardMarkup youthCenterKeyboard = new InlineKeyboardMarkup();
        if (userDto != null) {
            CustomPage<FavouriteYouthCenterDto> favouriteYouthCenterDtos = favouriteYouthCenterClient.getFavouriteYouthCenters(null, userDto.getId(), page);
            List<YouthCenterDto> youthCenterDtos = new ArrayList<>();

            for (FavouriteYouthCenterDto favouriteYouthCenterDto : favouriteYouthCenterDtos.getContent()) {
                YouthCenterDto youthCenterDto = youthCenterClient.getYouthCenterById(favouriteYouthCenterDto.getYouthCenterId()).getBody();

                if (youthCenterDto == null) {
                    favouriteYouthCenterClient.deleteFavouriteYouthCenter(favouriteYouthCenterDto.getId());
                    continue;
                }

                youthCenterDtos.add(youthCenterDto);
            }

            for (YouthCenterDto youthCenterDto : youthCenterDtos) {
                youthCenterKeyboard.addRow(
                        new InlineKeyboardButton(youthCenterDto.getName()).callbackData("youth-center-page-" + youthCenterDto.getExternalId())
                );
            }
        }

        youthCenterKeyboard.addRow(
                new InlineKeyboardButton(findYouthCenterByLocation).callbackData("find-youth-center-by-location")
        );

        if (userDto != null) {
            youthCenterKeyboard.addRow(
                    new InlineKeyboardButton(left).callbackData("favourite-youth-center-page-" + Math.max(0, page - 1)),
                    new InlineKeyboardButton(String.valueOf(page + 1)).callbackData("easter-egg"),
                    new InlineKeyboardButton(right).callbackData("favourite-youth-center-page-" + (page + 1)));
        }

        SendMessage sendMessage = new SendMessage(chatId, yourFavouriteYouthCenters);
        sendMessage.parseMode(ParseMode.MarkdownV2);
        sendMessage.replyMarkup(youthCenterKeyboard);
        return sendMessage;
    }
}
