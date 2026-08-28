package com.katok.molodcentertelegrambot.register;

import com.katok.molodcentertelegrambot.user.UserClient;
import com.katok.molodcentertelegrambot.user.UserDto;
import com.katok.molodcentertelegrambot.user.UserDtoCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final UserClient userClient;

    @Value("${app.redis-live-time}")
    private int timeToLive;

    public RegisterDto startRegister(Long userId) {
        RegisterDto registerDto = RegisterDto.builder()
                .userId(userId)
                .timeToLive(timeToLive)
                .build();

        return registerRepository.save(registerDto);
    }

    public RegisterDto setName(Long userId, String name) throws UserRegistrationNotFound {
        RegisterDto registerDto = registerRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        registerDto.setName(name);
        registerDto.setTimeToLive(timeToLive);

        return registerRepository.save(registerDto);
    }

    public RegisterDto setLastName(Long userId, String lastName) throws UserRegistrationNotFound {
        RegisterDto registerDto = registerRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        registerDto.setLastName(lastName);
        registerDto.setTimeToLive(timeToLive);

        return registerRepository.save(registerDto);
    }

    public RegisterDto setPhoneNumber(Long userId, String phoneNumber) throws UserRegistrationNotFound {
        RegisterDto registerDto = registerRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        registerDto.setPhoneNumber(phoneNumber);
        registerDto.setTimeToLive(timeToLive);

        return registerRepository.save(registerDto);
    }

    public UserDto finishRegistration(Long userId) throws UserRegistrationNotFound {
        RegisterDto registerDto = registerRepository.findById(userId)
                .orElseThrow(() -> new UserRegistrationNotFound("Реєстрацію юзера з айді " + userId + " не знайдено!"));

        if (registerDto.getName() == null || registerDto.getLastName() == null || registerDto.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Реєстрація юзера з айді " + userId + " не повністю заповнена!");
        }

        UserDtoCreate userDtoCreate = new UserDtoCreate(userId, registerDto.getName(), registerDto.getLastName(), registerDto.getPhoneNumber());
        registerRepository.delete(registerDto);
        return userClient.createUser(userDtoCreate);
    }
}
