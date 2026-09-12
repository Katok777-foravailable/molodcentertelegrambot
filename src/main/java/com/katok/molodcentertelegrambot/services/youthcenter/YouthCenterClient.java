package com.katok.molodcentertelegrambot.services.youthcenter;

import com.katok.molodcentertelegrambot.services.CustomPage;
import com.katok.molodcentertelegrambot.services.category.CategoryDto;
import com.katok.molodcentertelegrambot.services.event.EventDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "youthcenter-client", url = "${servers.molodcenter}" + "/api/youth-centers", dismiss404 = true)
public interface YouthCenterClient {
    @GetMapping("/{id}")
    ResponseEntity<YouthCenterDto> getYouthCenterById(@PathVariable Long id);

    @GetMapping("/search")
    ResponseEntity<YouthCenterDto> getYouthCenter(@RequestParam String externalId);

    @GetMapping
    CustomPage<YouthCenterDto> getYouthCentersByLocation(@RequestParam Float latitude,
                                                          @RequestParam Float longitude,
                                                          @RequestParam Float radius,
                                                          @RequestParam(defaultValue = "0") int page);

    @GetMapping("/{id}/events")
    CustomPage<EventDto> getEventsByYouthCenter(@PathVariable Long id,
                                          @RequestParam(required = false) Long categoryId,
                                          @RequestParam(defaultValue = "0") int page);

    @GetMapping("/{id}/categories")
    CustomPage<CategoryDto> getCategoriesByYouthCenter(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "0") int page);

    @PostMapping
    ResponseEntity<YouthCenterDto> createYouthCenter(@Valid @RequestBody YouthCenterCreateDto youthCenterCreateDto);

    @PatchMapping("/{id}")
    ResponseEntity<YouthCenterDto> updateYouthCenter(@PathVariable Long id, @Valid @RequestBody YouthCenterCreateDto youthCenterCreateDto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteYouthCenter(@PathVariable Long id);
}
