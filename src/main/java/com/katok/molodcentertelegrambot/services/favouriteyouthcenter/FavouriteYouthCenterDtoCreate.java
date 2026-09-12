package com.katok.molodcentertelegrambot.services.favouriteyouthcenter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteYouthCenterDtoCreate {
    @NotNull
    private Long youthCenterId;
    @NotNull
    private Long userId;
}
