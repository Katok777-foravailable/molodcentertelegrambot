package com.katok.molodcentertelegrambot.bot.youthcenterregister;

import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.youthcenter.GeoLocation;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterClient;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterCreateDto;
import com.katok.molodcentertelegrambot.services.youthcenter.YouthCenterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YouthCenterRegisterService {
    private final YouthCenterRegisterRepository youthCenterRegisterRepository;
    private final YouthCenterClient youthCenterClient;

    @Value("${app.redis-live-time}")
    private int timeToLive;

    public YouthCenterRegisterDto startRegister(Long userId) {
        YouthCenterRegisterDto youthCenterRegisterDto = YouthCenterRegisterDto.builder()
                .userId(userId)
                .timeToLive(timeToLive)
                .build();

        return youthCenterRegisterRepository.save(youthCenterRegisterDto);
    }

    public YouthCenterRegisterDto setName(Long userId, String name) throws ValueNotFound {
        YouthCenterRegisterDto youthCenterRegisterDto = youthCenterRegisterRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("Реєстрацію молодіжного центра з юзер айді " + userId + " не знайдено!"));

        youthCenterRegisterDto.setName(name);
        youthCenterRegisterDto.setTimeToLive(timeToLive);

        return youthCenterRegisterRepository.save(youthCenterRegisterDto);
    }

    public YouthCenterRegisterDto setLatitude(Long userId, Float latitude) throws ValueNotFound {
        YouthCenterRegisterDto youthCenterRegisterDto = youthCenterRegisterRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("Реєстрацію молодіжного центра з юзер айді " + userId + " не знайдено!"));

        youthCenterRegisterDto.setLatitude(latitude);
        youthCenterRegisterDto.setTimeToLive(timeToLive);

        return youthCenterRegisterRepository.save(youthCenterRegisterDto);
    }

    public YouthCenterRegisterDto setLongitude(Long userId, Float longitude) throws ValueNotFound {
        YouthCenterRegisterDto youthCenterRegisterDto = youthCenterRegisterRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("Реєстрацію молодіжного центра з юзер айді " + userId + " не знайдено!"));

        youthCenterRegisterDto.setLongitude(longitude);
        youthCenterRegisterDto.setTimeToLive(timeToLive);

        return youthCenterRegisterRepository.save(youthCenterRegisterDto);
    }

    public YouthCenterDto finishRegistration(Long userId) throws ValueNotFound {
        YouthCenterRegisterDto youthCenterRegisterDto = youthCenterRegisterRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("Реєстрацію молодіжного центра з юзер айді " + userId + " не знайдено!"));

        if (youthCenterRegisterDto.getLatitude() == null || youthCenterRegisterDto.getLongitude() == null || youthCenterRegisterDto.getName() == null) {
            throw new IllegalArgumentException("Реєстрація молодіжного центру з юзер айді " + userId + " не повністю заповнена!");
        }

        YouthCenterCreateDto youthCenterCreateDto = new YouthCenterCreateDto(
                new GeoLocation(youthCenterRegisterDto.getLatitude(), youthCenterRegisterDto.getLongitude()),
                youthCenterRegisterDto.getName()
        );

        return youthCenterClient.createYouthCenter(youthCenterCreateDto).getBody();
    }
}
