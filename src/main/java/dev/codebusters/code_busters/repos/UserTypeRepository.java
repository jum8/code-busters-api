package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByTitle(String title);
}
