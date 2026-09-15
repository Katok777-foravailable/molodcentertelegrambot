package com.katok.molodcentertelegrambot.bot.userrole;

import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleClient;
import com.katok.molodcentertelegrambot.services.userrole.UserRoleDtoCreate;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleClient userRoleClient;
    private final YouthCenterClient youthCenterClient;
    private final UserClient userClient;

    @Value("${app.redis-live-time}")
    private int redisLiveTime;

    public UserRoleDto startRegister(long adminUserId, String youthCenterExternalId) {
        UserRoleDto userRoleDto = new UserRoleDto(adminUserId, redisLiveTime, youthCenterExternalId, null, null);

        return userRoleRepository.save(userRoleDto);
    }

    public UserRoleDto setUserExternalId(long adminUserId, String externalUserId) throws ValueNotFound {
        UserRoleDto userRoleDto = userRoleRepository.findById(adminUserId)
                .orElseThrow(() -> new ValueNotFound("UserRoleDto з adminUserId " + adminUserId + " не знайдено!"));

        userRoleDto.setExternalUserId(externalUserId);
        userRoleDto.setTimeToLive(redisLiveTime);

        return userRoleRepository.save(userRoleDto);
    }

    public UserRoleDto setUserRole(long adminUserId, short userRole) throws ValueNotFound {
        UserRoleDto userRoleDto = userRoleRepository.findById(adminUserId)
                .orElseThrow(() -> new ValueNotFound("UserRoleDto з adminUserId " + adminUserId + " не знайдено!"));

        userRoleDto.setUserRole(userRole);
        userRoleDto.setTimeToLive(redisLiveTime);

        return userRoleRepository.save(userRoleDto);
    }

    public com.katok.molodcentertelegrambot.services.userrole.UserRoleDto finishRegister(long adminUserId) throws ValueNotFound {
        UserRoleDto userRoleDto = userRoleRepository.findById(adminUserId)
                .orElseThrow(() -> new ValueNotFound("UserRoleDto з adminUserId " + adminUserId + " не знайдено!"));

        ResponseEntity<UserDto> userDtoResponseEntity = userClient.getUser(null, null, userRoleDto.getExternalUserId());
        UserDto userDto = userDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userDto == null) {
            throw new ValueNotFound("Юзера з зовнішнім айді " + userRoleDto.getExternalUserId() + " не знайдено!");
        }

        ResponseEntity<YouthCenterDto> youthCenterDtoResponseEntity = youthCenterClient.getYouthCenter(userRoleDto.getYouthCenterExternalId());
        YouthCenterDto youthCenterDto = youthCenterDtoResponseEntity.getBody();

        if (youthCenterDtoResponseEntity.getStatusCode().is4xxClientError() || youthCenterDto == null) {
            throw new ValueNotFound("Молодіжний центр з зовнішнім айді " + userRoleDto.getYouthCenterExternalId() + " не знайдено!");
        }

        UserRoleDtoCreate userRoleDtoCreate = new UserRoleDtoCreate(youthCenterDto.getId(), userDto.getId(), userRoleDto.getUserRole());
        ResponseEntity<com.katok.molodcentertelegrambot.services.userrole.UserRoleDto> userRoleDtoResponseEntity = userRoleClient.createUserRole(userRoleDtoCreate);
        com.katok.molodcentertelegrambot.services.userrole.UserRoleDto userRoleDtoResult = userRoleDtoResponseEntity.getBody();

        if (userDtoResponseEntity.getStatusCode().is4xxClientError() || userRoleDtoResult == null) {
            return null;
        }

        return userRoleDtoResult;
    }
}
