package com.katok.molodcentertelegrambot.services.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoCreate {
    private Long telegramUserId;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @Size(max = 20)
    private String phoneNumber;
}
