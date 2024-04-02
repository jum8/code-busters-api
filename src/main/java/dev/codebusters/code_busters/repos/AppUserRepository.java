package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM AppUser u " +
            "JOIN UserType ut ON u.userType.id = ut.id " +
            "WHERE ut.title != 'ADMIN' " +
            "ORDER BY u.points DESC " +
            "LIMIT :limit")
    List<AppUser> findAllUsersSortedByPoints(@Param("limit") Integer limit);

    default List<AppUser> findAllUsersSortedByPointsWithDefaultLimit(Integer limit) {
        return findAllUsersSortedByPoints(limit == null ? 10 : limit);
    }
}
