package com.katok.molodcentertelegrambot.bot.category.categoryregister;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("CategoryRegister")
public class CategoryRegisterDto {
    @Id
    private Long telegramUserId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private int timeToLive;

    private String name;
    private Long youthCenterId;
}
