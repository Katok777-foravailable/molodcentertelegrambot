package com.katok.molodcentertelegrambot.services.favouriteyouthcenter;

import com.katok.molodcentertelegrambot.services.CustomPage;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "favourite-youth-center-client", url = "${servers.molodcenteruserservice}" + "/api/favourite-youth-center", dismiss404 = true)
public interface FavouriteYouthCenterClient {
    @GetMapping("/{id}")
    FavouriteYouthCenterDto getFavouriteYouthCenter(@PathVariable Long id);

    @GetMapping
    CustomPage<FavouriteYouthCenterDto> getFavouriteYouthCenters(@RequestParam(required = false) Long youthCenterId,
                                                                 @RequestParam(required = false) Long userId,
                                                                 @RequestParam(defaultValue = "0") int page);

    @DeleteMapping("/{id}")
    void deleteFavouriteYouthCenter(@PathVariable Long id);

    @PostMapping
    FavouriteYouthCenterDto createFavouriteYouthCenter(@RequestBody @Valid FavouriteYouthCenterDtoCreate favouriteYouthCenterDtoCreate);
}
