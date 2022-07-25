package com.miniproject.config.security.repository;

import com.miniproject.config.security.domain.RefreshTokenRedis;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenRedis, String> {

}
