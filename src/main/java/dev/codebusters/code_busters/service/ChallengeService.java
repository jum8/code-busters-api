package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.Category;
import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.domain.Hint;
import dev.codebusters.code_busters.domain.Submission;
import dev.codebusters.code_busters.model.ChallengeDTO;
import dev.codebusters.code_busters.model.ChallengeSummaryDTO;
import dev.codebusters.code_busters.repos.CategoryRepository;
import dev.codebusters.code_busters.repos.ChallengeRepository;
import dev.codebusters.code_busters.repos.HintRepository;
import dev.codebusters.code_busters.repos.SubmissionRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final SubmissionRepository submissionRepository;
    private final HintRepository hintRepository;

    public ChallengeService(final ChallengeRepository challengeRepository,
            final CategoryRepository categoryRepository,
            final SubmissionRepository submissionRepository, final HintRepository hintRepository) {
        this.challengeRepository = challengeRepository;
        this.categoryRepository = categoryRepository;
        this.submissionRepository = submissionRepository;
        this.hintRepository = hintRepository;
    }

    @Transactional
    public List<ChallengeSummaryDTO> findAll() {
        final List<Challenge> challenges = challengeRepository.findAll(Sort.by("id"));
        return challenges.stream()
                .map(challenge -> mapToSummaryDTO(challenge, new ChallengeSummaryDTO()))
                .toList();
    }

    public ChallengeDTO get(final Long id) {
        return challengeRepository.findById(id)
                .map(challenge -> mapToDTO(challenge, new ChallengeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ChallengeDTO challengeDTO) {
        final Challenge challenge = new Challenge();
        mapToEntity(challengeDTO, challenge);
        return challengeRepository.save(challenge).getId();
    }

    public void update(final Long id, final ChallengeDTO challengeDTO) {
        final Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(challengeDTO, challenge);
        challengeRepository.save(challenge);
    }

    public void delete(final Long id) {
        challengeRepository.deleteById(id);
    }

    private ChallengeDTO mapToDTO(final Challenge challenge, final ChallengeDTO challengeDTO) {
        challengeDTO.setId(challenge.getId());
        challengeDTO.setAdded(challenge.getAdded());
        challengeDTO.setTitle(challenge.getTitle());
        challengeDTO.setDescription(challenge.getDescription());
        challengeDTO.setExposed(challenge.getExposed());
        challengeDTO.setFlag(challenge.getFlag());
        challengeDTO.setPoints(challenge.getPoints());
        challengeDTO.setCredits(challenge.getCredits());
        challengeDTO.setLevel(challenge.getLevel());
        challengeDTO.setCategory(challenge.getCategory() == null ? null : challenge.getCategory().getId());
        return challengeDTO;
    }

    private ChallengeSummaryDTO mapToSummaryDTO(final Challenge challenge, final ChallengeSummaryDTO challengeSummaryDTO) {
        challengeSummaryDTO.setId(challenge.getId());
        challengeSummaryDTO.setTitle(challenge.getTitle());
        challengeSummaryDTO.setExposed(challenge.getExposed());
        challengeSummaryDTO.setPoints(challenge.getPoints());
        challengeSummaryDTO.setCredits(challenge.getCredits());
        challengeSummaryDTO.setLevel(challenge.getLevel());
        challengeSummaryDTO.setCategory(challenge.getCategory() == null ? null : challenge.getCategory().getTitle());
        return challengeSummaryDTO;
    }

    private Challenge mapToEntity(final ChallengeDTO challengeDTO, final Challenge challenge) {
        challenge.setAdded(challengeDTO.getAdded());
        challenge.setTitle(challengeDTO.getTitle());
        challenge.setDescription(challengeDTO.getDescription());
        challenge.setExposed(challengeDTO.getExposed());
        challenge.setFlag(challengeDTO.getFlag());
        challenge.setPoints(challengeDTO.getPoints());
        challenge.setCredits(challengeDTO.getCredits());
        challenge.setLevel(challengeDTO.getLevel());
        final Category category = challengeDTO.getCategory() == null ? null : categoryRepository.findById(challengeDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        challenge.setCategory(category);
        return challenge;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Submission challengeSubmission = submissionRepository.findFirstByChallenge(challenge);
        if (challengeSubmission != null) {
            referencedWarning.setKey("challenge.submission.challenge.referenced");
            referencedWarning.addParam(challengeSubmission.getId());
            return referencedWarning;
        }
        final Hint challengeHint = hintRepository.findFirstByChallenge(challenge);
        if (challengeHint != null) {
            referencedWarning.setKey("challenge.hint.challenge.referenced");
            referencedWarning.addParam(challengeHint.getId());
            return referencedWarning;
        }
        return null;
    }

}
