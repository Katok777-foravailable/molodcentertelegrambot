package com.katok.molodcentertelegrambot.bot.userregister;

import org.springframework.data.repository.CrudRepository;

public interface UserRegisterRepository extends CrudRepository<UserRegisterDto, Long> {
}
