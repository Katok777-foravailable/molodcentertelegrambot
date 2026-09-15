package com.katok.molodcentertelegrambot.services.userrole;

import com.katok.molodcentertelegrambot.services.CustomPage;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-role-client", url = "${servers.molodcenteruserservice}" + "/api/roles", dismiss404 = true)
public interface UserRoleClient {
    @PutMapping
    ResponseEntity<UserRoleDto> createUserRole(@RequestBody @Valid UserRoleDtoCreate userRoleDtoCreate);

    @GetMapping("/{id}")
    ResponseEntity<UserRoleDto> getUserRoleById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<CustomPage<UserRoleDto>> getUserRoleByYouthCenterIdAndUserId(@RequestParam(required = false) Long userId,
                                                                                       @RequestParam(required = false) Long youthCenterId,
                                                                                       @RequestParam(defaultValue = "0") int page);
}