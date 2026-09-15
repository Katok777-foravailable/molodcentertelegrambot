package com.katok.molodcentertelegrambot.services.userrole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDtoCreate {
    @NotNull
    private Long youthCenterId;
    @NotNull
    private Long userId;
    @NotNull
    private Short role;
}
