package com.katok.molodcentertelegrambot.bot.cooldown;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "UserCooldown")
@Data
@Builder
public class CooldownDto {
    @Id
    private Long userId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private int timeToLive;

    @Builder.Default
    private boolean warning = false;
}
