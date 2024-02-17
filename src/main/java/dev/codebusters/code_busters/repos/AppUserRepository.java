package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
