package com.katok.molodcentertelegrambot.services.category;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "category-client", url = "${servers.molodcenter}" + "/api/categories", dismiss404 = true)
public interface CategoryClient {
    @GetMapping
    Page<CategoryDto> getAllGlobalCategories(@RequestParam(defaultValue = "0") int page);

    @GetMapping("/{id}")
    ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id);

    @GetMapping("/external-id/{externalId}")
    ResponseEntity<CategoryDto> getCategoryByExternalId(@PathVariable String externalId);

    @PostMapping
    ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryCreateDto categoryCreateDto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long id);

    @PatchMapping("/{id}")
    ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreateDto categoryCreateDto);
}
