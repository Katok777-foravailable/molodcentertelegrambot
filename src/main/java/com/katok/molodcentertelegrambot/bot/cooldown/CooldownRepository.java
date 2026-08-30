package com.katok.molodcentertelegrambot.bot.cooldown;

import org.springframework.data.repository.CrudRepository;

public interface CooldownRepository extends CrudRepository<CooldownDto, Long> {}