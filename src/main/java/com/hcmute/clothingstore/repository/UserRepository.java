package com.hcmute.clothingstore.repository;

import com.hcmute.clothingstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);


    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findByEmailAndActivatedTrue(String email);
}
