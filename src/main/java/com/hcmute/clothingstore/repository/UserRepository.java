package com.hcmute.clothingstore.repository;

import com.hcmute.clothingstore.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);


    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findByEmailAndActivatedTrue(String email);

    Optional<User> findByResetKey(String key);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.activationKey = :key")
    Optional<User> findByActivationKeyWithLock(@Param("key") String key);

    Optional<User> findByActivationKey(String key);

    Optional<User> findByEmailAndActivatedFalse(String email);
}
