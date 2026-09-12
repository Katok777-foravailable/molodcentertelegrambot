package com.katok.molodcentertelegrambot.services.favouriteyouthcenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteYouthCenterDto {
    private Long id;
    private Long youthCenterId;
    private Long userId;
}
