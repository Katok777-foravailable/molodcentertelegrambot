package com.katok.molodcentertelegrambot.bot.youthcenters.categories;

import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.category.CategoryClient;
import com.katok.molodcentertelegrambot.services.category.CategoryCreateDto;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YouthCenterCategoryService {
    private final YouthCenterCategoryRepository youthCenterCategoryRepository;
    private final CategoryClient categoryClient;

    @Value("${app.redis-live-time}")
    private int timeToLive;

    public YouthCenterCategoryDto startRegister(Long userId) {
        YouthCenterCategoryDto youthCenterCategoryDto = new YouthCenterCategoryDto(userId, timeToLive, null, null);

        return youthCenterCategoryRepository.save(youthCenterCategoryDto);
    }

    public YouthCenterCategoryDto setExternalId(Long userId, Long youthCenterId) throws ValueNotFound {
        YouthCenterCategoryDto youthCenterCategoryDto = youthCenterCategoryRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("YouthCenterCategoryDto з userId " + userId + " не знайдено!"));

        youthCenterCategoryDto.setYouthCenterId(youthCenterId);
        youthCenterCategoryDto.setTimeToLive(timeToLive);

        return youthCenterCategoryRepository.save(youthCenterCategoryDto);
    }

    public YouthCenterCategoryDto setCategoryName(Long userId, String categoryName) {
        YouthCenterCategoryDto youthCenterCategoryDto = youthCenterCategoryRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("YouthCenterCategoryDto з userId " + userId + " не знайдено!"));

        youthCenterCategoryDto.setCategoryName(categoryName);
        youthCenterCategoryDto.setTimeToLive(timeToLive);

        return youthCenterCategoryRepository.save(youthCenterCategoryDto);
    }

    public CategoryDto finishRegister(Long userId) {
        YouthCenterCategoryDto youthCenterCategoryDto = youthCenterCategoryRepository.findById(userId)
                .orElseThrow(() -> new ValueNotFound("YouthCenterCategoryDto з userId " + userId + " не знайдено!"));

        CategoryCreateDto categoryCreateDto = new CategoryCreateDto(youthCenterCategoryDto.getCategoryName(), youthCenterCategoryDto.getYouthCenterId());
        return categoryClient.createCategory(categoryCreateDto).getBody();
    }
}
