package com.katok.molodcentertelegrambot.services.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private Long telegramUserId;
    private String externalId;
    private String name;
    private String lastName;
    private String phoneNumber;
    private Short adminRank;
}
