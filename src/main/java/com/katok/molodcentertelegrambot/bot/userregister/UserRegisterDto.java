package com.katok.molodcentertelegrambot.bot.userregister;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@RedisHash(value = "UserRegister")
@Data
@Builder
public class UserRegisterDto implements Serializable {
    @Id
    private Long userId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private int timeToLive;

    private String name;
    private String lastName;
    private String phoneNumber;
}
