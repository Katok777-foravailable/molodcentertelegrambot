package com.katok.molodcentertelegrambot.services.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    public final static int EXTERNAL_ID_LENGTH = 20;

    private Long id;
    private String name;
    private String externalId;
    private Long youthCenterId;
}
