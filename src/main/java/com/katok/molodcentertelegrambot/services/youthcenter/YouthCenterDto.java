package com.katok.molodcentertelegrambot.services.youthcenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouthCenterDto {
    public final static int EXTERNAL_ID_LENGTH = 20;

    private Long id;
    private GeoLocation geoLocation;
    private String name;
    private String externalId;
}
