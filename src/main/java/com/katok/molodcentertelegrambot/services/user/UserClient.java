package com.katok.molodcentertelegrambot.services.user;

import com.katok.molodcentertelegrambot.services.usereventregistration.UserEventRegistrationDto;
import com.katok.molodcentertelegrambot.services.usereventsubscription.UserEventSubscriptionDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-client", url = "${servers.molodcenteruserservice}" + "/api/users", dismiss404 = true)
public interface UserClient {
    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/{id}/registration")
    Page<UserEventRegistrationDto> getUserRegistrations(@PathVariable Long id,
                                                               @RequestParam(defaultValue = "0") int page);
    @GetMapping("/{id}/subscription")
    Page<UserEventSubscriptionDto> getUserSubscriptions(@PathVariable Long id,
                                                               @RequestParam(required = false) Long categoryId,
                                                               @RequestParam(required = false) Long youthCenterId,
                                                               @RequestParam(defaultValue = "0") int page);

    @GetMapping("/search")
    ResponseEntity<UserDto> getUser(@RequestParam(required = false) Long telegramId,
                                    @RequestParam(required = false) String phoneNumber,
                                    @RequestParam(required = false) String externalId);

    @GetMapping
    Page<UserDto> getUsers(@RequestParam(defaultValue = "0") int page);

    @PostMapping
    UserDto createUser(@Valid @RequestBody UserDtoCreate userDtoCreate);
}
