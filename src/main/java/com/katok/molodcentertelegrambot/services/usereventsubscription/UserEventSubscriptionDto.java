package com.katok.molodcentertelegrambot.services.usereventsubscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventSubscriptionDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Long youthCenterId;
}
