package com.katok.molodcentertelegrambot.bot.category.categoryedit;

import com.katok.molodcentertelegrambot.exception.ValueNotFound;
import com.katok.molodcentertelegrambot.services.category.CategoryClient;
import com.katok.molodcentertelegrambot.services.category.CategoryCreateDto;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryEditService {
    private final CategoryEditRepository categoryEditRepository;
    private final CategoryClient categoryClient;

    @Value("${app.redis-live-time}")
    private int timeToLive;

    public CategoryEditDto startEdit(Long userTelegramId, Long categoryId) {
        CategoryEditDto categoryEditDto = new CategoryEditDto(userTelegramId, timeToLive, categoryId, null);

        return categoryEditRepository.save(categoryEditDto);
    }

    public CategoryEditDto setNewName(Long userTelegramId, String newName) throws ValueNotFound {
        CategoryEditDto categoryEditDto = categoryEditRepository.findById(userTelegramId)
                .orElseThrow(() -> new ValueNotFound("CategoryEditDto з юзер айді " + userTelegramId + " не знайдено!"));

        categoryEditDto.setNewName(newName);
        categoryEditDto.setTimeToLive(timeToLive);

        return categoryEditRepository.save(categoryEditDto);
    }

    public CategoryDto finishEdit(Long userTelegramId) throws ValueNotFound {
        CategoryEditDto categoryEditDto = categoryEditRepository.findById(userTelegramId)
                .orElseThrow(() -> new ValueNotFound("CategoryEditDto з юзер айді " + userTelegramId + " не знайдено!"));

        CategoryCreateDto categoryCreateDto = new CategoryCreateDto(categoryEditDto.getNewName(), null);

        return categoryClient.updateCategory(categoryEditDto.getCategoryId(), categoryCreateDto).getBody();
    }
}
