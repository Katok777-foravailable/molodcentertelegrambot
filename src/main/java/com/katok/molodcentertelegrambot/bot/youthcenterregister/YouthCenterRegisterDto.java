package com.katok.molodcentertelegrambot.bot.youthcenterregister;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "YouthCenterRegister")
@Data
@Builder
public class YouthCenterRegisterDto {
    @Id
    private Long userId;

    @TimeToLive
    private int timeToLive;

    private String name;
    private Float latitude;
    private Float longitude;
}
