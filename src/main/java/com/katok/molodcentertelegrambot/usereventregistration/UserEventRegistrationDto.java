package com.katok.molodcentertelegrambot.usereventregistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventRegistrationDto {
    private Long id;
    private Long userId;
    private Long eventId;
}
