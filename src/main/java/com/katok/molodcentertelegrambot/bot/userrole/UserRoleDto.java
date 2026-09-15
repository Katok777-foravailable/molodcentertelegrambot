package com.katok.molodcentertelegrambot.bot.userrole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "UserRole")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDto {
    @Id
    private Long adminUserId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private int timeToLive;

    private String youthCenterExternalId;
    private String externalUserId;
    private Short userRole;
}
