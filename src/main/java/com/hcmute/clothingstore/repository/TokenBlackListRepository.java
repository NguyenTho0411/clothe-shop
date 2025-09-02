package com.hcmute.clothingstore.repository;

import com.hcmute.clothingstore.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlacklist,Long> {
}
