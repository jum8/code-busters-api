package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserTypeRepository extends JpaRepository<UserType, Long> {
}
