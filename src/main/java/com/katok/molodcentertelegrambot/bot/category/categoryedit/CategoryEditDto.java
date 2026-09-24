package com.katok.molodcentertelegrambot.bot.category.categoryedit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("CategoryEdit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEditDto {
    @Id
    private Long userTelegramId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private int timeToLive;

    private Long categoryId;
    private String newName;
}
