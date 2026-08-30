package com.katok.molodcentertelegrambot.services.youthcenter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouthCenterCreateDto {
    @NotNull
    private GeoLocation geoLocation;
    @NotBlank
    private String name;
}