package com.katok.molodcentertelegrambot.bot.youthcenters.category.categorypage;

import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.category.CategoryClient;
import com.katok.molodcentertelegrambot.services.category.CategoryCreateDto;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YouthCenterCategoryPageService {
    private final YouthCenterCategoryPageRepository youthCenterCategoryPageRepository;
    private final CategoryClient categoryClient;

    @Value("${app.redis-live-time}")
    private int timeToLive;

    public YouthCenterCategoryPageDto startRegister(Long userId) {
        YouthCenterCategoryPageDto youthCenterCategoryPageDto = new YouthCenterCategoryPageDto(userId, timeToLive, null, null);

        return youthCenterCategoryPageRepository.save(youthCenterCategoryPageDto);
    }

    public YouthCenterCategoryPageDto setExternalId(Long userId, Long youthCenterId) throws ValueNotFound {
        YouthCenterCategoryPageDto youthCenterCategoryPageDto = youthCenterCategoryPageRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("YouthCenterCategoryDto з userId " + userId + " не знайдено!"));

        youthCenterCategoryPageDto.setYouthCenterId(youthCenterId);
        youthCenterCategoryPageDto.setTimeToLive(timeToLive);

        return youthCenterCategoryPageRepository.save(youthCenterCategoryPageDto);
    }

    public YouthCenterCategoryPageDto setCategoryName(Long userId, String categoryName) {
        YouthCenterCategoryPageDto youthCenterCategoryPageDto = youthCenterCategoryPageRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("YouthCenterCategoryDto з userId " + userId + " не знайдено!"));

        youthCenterCategoryPageDto.setCategoryName(categoryName);
        youthCenterCategoryPageDto.setTimeToLive(timeToLive);

        return youthCenterCategoryPageRepository.save(youthCenterCategoryPageDto);
    }

    public CategoryDto finishRegister(Long userId) {
        YouthCenterCategoryPageDto youthCenterCategoryPageDto = youthCenterCategoryPageRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("YouthCenterCategoryDto з userId " + userId + " не знайдено!"));

        CategoryCreateDto categoryCreateDto = new CategoryCreateDto(youthCenterCategoryPageDto.getCategoryName(), youthCenterCategoryPageDto.getYouthCenterId());
        return categoryClient.createCategory(categoryCreateDto).getBody();
    }
}
