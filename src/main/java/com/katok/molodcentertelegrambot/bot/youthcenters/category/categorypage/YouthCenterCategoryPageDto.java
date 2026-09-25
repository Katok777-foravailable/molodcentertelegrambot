package com.katok.molodcentertelegrambot.bot.youthcenters.category.categorypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("YouthCenterCategory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouthCenterCategoryPageDto {
    @Id
    private Long userId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private int timeToLive;

    private Long youthCenterId;
    private String categoryName;
}
