package com.melly.domain.repository;

import com.melly.domain.entity.UserAuthProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthProviderRepository extends JpaRepository<UserAuthProviderEntity, Long> {
}
