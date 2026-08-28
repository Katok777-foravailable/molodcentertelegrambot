package com.katok.molodcentertelegrambot.register;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@RedisHash(value = "Register")
@Data
@Builder
public class RegisterDto implements Serializable {
    @Id
    private Long userId;

    @TimeToLive
    private int timeToLive;

    private String name;
    private String lastName;
    private String phoneNumber;
}
