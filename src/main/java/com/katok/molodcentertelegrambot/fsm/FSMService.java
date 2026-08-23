package com.katok.molodcentertelegrambot.fsm;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FSMService {
    public final static String PREFIX = "user:state:";

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${app.redis-live-time}")
    private Long duration;

    public void updateState(long userId, String state) {
        String key = PREFIX + userId;

        stringRedisTemplate.opsForValue().set(key, state, Duration.ofMillis(duration));
    }

    @Nullable
    public String getState(long userId) {
        String key = PREFIX + userId;

        return stringRedisTemplate.opsForValue().get(key);
    }

    public void deleteState(long userId) {
        String key = PREFIX + userId;

        stringRedisTemplate.opsForValue().getAndDelete(key);
    }
}
