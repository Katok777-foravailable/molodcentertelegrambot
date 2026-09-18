package com.katok.molodcentertelegrambot.bot.youthcenters.categories;

import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleClient;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class YouthCenterCategorySecurity {
    private final UserRoleClient userRoleClient;

    @Value("${general.no-permission}")
    private String noPermission;

    @Value("${user-roles.change-categories}")
    private int changeCategories;
    @Value("${permissions.change-user-roles}")
    private int adminChangeUserRoles;

    @Nullable
    public SendMessage canChangeUserRole(long chatId, UserDto userDto, YouthCenterDto youthCenterDto) {
        short adminRank = userDto.getAdminRank();

        if (adminRank < adminChangeUserRoles) {
            ResponseEntity<CustomPage<UserRoleDto>> userRoleDtoResponseEntity = userRoleClient.getUserRoleByYouthCenterIdAndUserId(userDto.getId(), youthCenterDto.getId(), 0);
            CustomPage<UserRoleDto> userRoleDtoCustomPage = userRoleDtoResponseEntity.getBody();

            short userRole = 0;

            if (!userRoleDtoResponseEntity.getStatusCode().is4xxClientError() && userRoleDtoCustomPage != null) {
                List<UserRoleDto> userRoleDtos = userRoleDtoCustomPage.getContent();
                if (userRoleDtos != null && !userRoleDtos.isEmpty()) {
                    userRole = userRoleDtos.getFirst().getRole();
                }
            }

            if (userRole < changeCategories) {
                SendMessage sendMessage = new SendMessage(chatId, noPermission);
                sendMessage.parseMode(ParseMode.MarkdownV2);

                return sendMessage;
            }
        }

        return null;
    }
}
