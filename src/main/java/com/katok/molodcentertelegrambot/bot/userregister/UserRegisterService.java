package com.katok.molodcentertelegrambot.bot.userregister;

import com.katok.molodcentertelegrambot.services.user.UserClient;
import com.katok.molodcentertelegrambot.services.user.UserDto;
import com.katok.molodcentertelegrambot.services.user.UserDtoCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegisterService {
    private final UserRegisterRepository userRegisterRepository;
    private final UserClient userClient;

    @Value("${app.redis-live-time}")
    private int timeToLive;

    public UserRegisterDto startRegister(Long userId) {
        UserRegisterDto userRegisterDto = UserRegisterDto.builder()
                .userId(userId)
                .timeToLive(timeToLive)
                .build();

        return userRegisterRepository.save(userRegisterDto);
    }

    public UserRegisterDto setName(Long userId, String name) throws UserRegistrationNotFound {
        UserRegisterDto userRegisterDto = userRegisterRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        userRegisterDto.setName(name);
        userRegisterDto.setTimeToLive(timeToLive);

        return userRegisterRepository.save(userRegisterDto);
    }

    public UserRegisterDto setLastName(Long userId, String lastName) throws UserRegistrationNotFound {
        UserRegisterDto userRegisterDto = userRegisterRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        userRegisterDto.setLastName(lastName);
        userRegisterDto.setTimeToLive(timeToLive);

        return userRegisterRepository.save(userRegisterDto);
    }

    public UserRegisterDto setPhoneNumber(Long userId, String phoneNumber) throws UserRegistrationNotFound {
        UserRegisterDto userRegisterDto = userRegisterRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        userRegisterDto.setPhoneNumber(phoneNumber);
        userRegisterDto.setTimeToLive(timeToLive);

        return userRegisterRepository.save(userRegisterDto);
    }

    public UserDto finishRegistration(Long userId) throws UserRegistrationNotFound {
        UserRegisterDto userRegisterDto = userRegisterRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        if (userRegisterDto.getName() == null || userRegisterDto.getLastName() == null || userRegisterDto.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Реєстрація юзера з айді " + userId + " не повністю заповнена!");
        }

        UserDtoCreate userDtoCreate = new UserDtoCreate(userId, userRegisterDto.getName(), userRegisterDto.getLastName(), userRegisterDto.getPhoneNumber());
        userRegisterRepository.delete(userRegisterDto);
        return userClient.createUser(userDtoCreate);
    }
}
