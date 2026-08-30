package com.katok.molodcentertelegrambot.services.youthcenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouthCenterDto {
    private Long id;
    private GeoLocation geoLocation;
    private String name;
}
