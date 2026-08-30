package com.katok.molodcentertelegrambot.services.youthcenter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation {
    @NotNull
    private Float latitude;

    @NotNull
    private Float longitude;
}
