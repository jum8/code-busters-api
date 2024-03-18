package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Submission findFirstByUser(AppUser appUser);

    Submission findFirstByChallenge(Challenge challenge);

    Boolean existsByUserIdAndChallengeId(Long userId, Long challengeId);
}
