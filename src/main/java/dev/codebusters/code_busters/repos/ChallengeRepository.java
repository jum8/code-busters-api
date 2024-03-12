package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Category;
import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.model.ChallengeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    Challenge findFirstByCategory(Category category);

    List<Challenge> findByExposedTrue();
    @Query("SELECT c FROM Challenge c WHERE "
            + "(:categoryId IS NULL OR c.category.id = :categoryId) AND "
            + "c.exposed = true")
    List<Challenge> findExposedChallengesByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT c FROM Challenge c WHERE "
            + "(:categoryId IS NULL OR c.category.id = :categoryId) AND "
            + "(:exposed IS NULL OR c.exposed = :exposed) AND "
            + "(:level IS NULL OR c.level = :level) AND "
            + "(:premium IS NULL OR c.premium = :premium) AND "
            + "(:subscription IS NULL OR (:subscription = 'FREE' AND c.credits = 0) OR (:subscription = 'PAID' AND c.credits > 0))")
    List<Challenge> searchChallenges(@Param("categoryId") Long categoryId,
                                         @Param("exposed") Boolean exposed,
                                         @Param("level") ChallengeLevel level,
                                         @Param("premium") Boolean premium,
                                         @Param("subscription") String subscription);

    @Query("SELECT c.flag FROM Challenge c WHERE "
            + "c.id = :id")
    String findFlagByChallengeId(@Param("id") Long id);
}
