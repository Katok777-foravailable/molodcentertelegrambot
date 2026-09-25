package com.katok.molodcentertelegrambot.bot.category.categoryregister;

import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.category.CategoryClient;
import com.katok.molodcentertelegrambot.services.category.CategoryCreateDto;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryRegisterService {
    private final CategoryRegisterRepository categoryRegisterRepository;
    private final CategoryClient categoryClient;

    @Value("${app.redis-live-time}")
    private int timeToLive;

    public CategoryRegisterDto startRegister(long telegramUserId) {
        CategoryRegisterDto categoryRegisterDto = new CategoryRegisterDto(telegramUserId, timeToLive, null, null);

        return categoryRegisterRepository.save(categoryRegisterDto);
    }

    public CategoryRegisterDto setName(long telegramUserId, String name) {
        CategoryRegisterDto categoryRegisterDto = categoryRegisterRepository.findById(telegramUserId)
                .orElseThrow(() -> new ValueNotFound("CategoryRegisterDto з telegramUserId " + telegramUserId + " не знайдено!"));

        categoryRegisterDto.setName(name);
        categoryRegisterDto.setTimeToLive(timeToLive);

        return categoryRegisterRepository.save(categoryRegisterDto);
    }

    public CategoryRegisterDto setYouthCenterId(long telegramUserId, long youthCenterId) throws ValueNotFound {
        CategoryRegisterDto categoryRegisterDto = categoryRegisterRepository.findById(telegramUserId)
                .orElseThrow(() -> new ValueNotFound("CategoryRegisterDto з telegramUserId " + telegramUserId + " не знайдено!"));

        categoryRegisterDto.setYouthCenterId(youthCenterId);
        categoryRegisterDto.setTimeToLive(timeToLive);

        return categoryRegisterRepository.save(categoryRegisterDto);
    }

    public CategoryDto finishRegister(long telegramUserId) throws ValueNotFound {
        CategoryRegisterDto categoryRegisterDto = categoryRegisterRepository.findById(telegramUserId)
                .orElseThrow(() -> new ValueNotFound("CategoryRegisterDto з telegramUserId " + telegramUserId + " не знайдено!"));

        CategoryCreateDto categoryCreateDto = new CategoryCreateDto(categoryRegisterDto.getName(), categoryRegisterDto.getYouthCenterId());

        return categoryClient.createCategory(categoryCreateDto).getBody();
    }
}
