package com.katok.molodcentertelegrambot.bot.cooldown;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CooldownService {
    private final CooldownRepository cooldownRepository;

    @Value("${app.user-action-cooldown}")
    private int cooldown;

    public void addCooldown(Long userId) {
        CooldownDto cooldownDto = cooldownRepository.findById(userId)
                .orElse(null);

        if (cooldownDto == null) {
            cooldownDto = new CooldownDto(userId, cooldown, false);
        } else {
            cooldownDto.setTimeToLive(cooldown);
        }

        cooldownRepository.save(cooldownDto);
    }

    public boolean isCooldown(Long userId) {
        return cooldownRepository.findById(userId).isPresent();
    }

    public void warn(Long userId) {
        CooldownDto cooldownDto = cooldownRepository.findById(userId)
                .orElse(null);

        if (cooldownDto == null) {
            return;
        }

        cooldownDto.setWarning(true);
        cooldownDto.setTimeToLive(cooldown);

        cooldownRepository.save(cooldownDto);
    }

    public boolean isWarning(Long userId) {
        CooldownDto cooldownDto = cooldownRepository.findById(userId)
                .orElse(null);

        if (cooldownDto == null) {
            return false;
        }

        return cooldownDto.isWarning();
    }

    public void clearCooldown(Long userId) {
        cooldownRepository.deleteById(userId);
    }
}
