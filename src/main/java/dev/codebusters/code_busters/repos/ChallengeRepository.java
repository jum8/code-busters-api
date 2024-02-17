package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Category;
import dev.codebusters.code_busters.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    Challenge findFirstByCategory(Category category);

}
