package dev.codebusters.code_busters.repos;

import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.domain.Hint;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HintRepository extends JpaRepository<Hint, Long> {

    Hint findFirstByChallenge(Challenge challenge);

}
