package com.katok.molodcentertelegrambot.services.userrole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {
    private Long id;
    private Long youthCenterId;
    private Long userId;
    private Short role;
}
