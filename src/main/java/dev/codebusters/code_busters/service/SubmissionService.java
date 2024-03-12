package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.domain.Submission;
import dev.codebusters.code_busters.model.SubmissionDTO;
import dev.codebusters.code_busters.repos.AppUserRepository;
import dev.codebusters.code_busters.repos.ChallengeRepository;
import dev.codebusters.code_busters.repos.SubmissionRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ResourceAlreadyExistsException;
import dev.codebusters.code_busters.util.WrongAnswerException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AppUserRepository appUserRepository;
    private final ChallengeRepository challengeRepository;

    public SubmissionService(final SubmissionRepository submissionRepository,
            final AppUserRepository appUserRepository,
            final ChallengeRepository challengeRepository) {
        this.submissionRepository = submissionRepository;
        this.appUserRepository = appUserRepository;
        this.challengeRepository = challengeRepository;
    }

    public List<SubmissionDTO> findAll() {
        final List<Submission> submissions = submissionRepository.findAll(Sort.by("id"));
        return submissions.stream()
                .map(submission -> mapToDTO(submission, new SubmissionDTO()))
                .toList();
    }

    public SubmissionDTO get(final Long id) {
        System.out.println(challengeRepository.findFlagByChallengeId(1L));
        return submissionRepository.findById(id)
                .map(submission -> mapToDTO(submission, new SubmissionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final SubmissionDTO submissionDTO) {
        if (submissionRepository.existsByUserIdAndChallengeId(submissionDTO.getUser(), submissionDTO.getChallenge())) {
            throw new ResourceAlreadyExistsException("Challenge already solved");
        }

        Challenge challenge = challengeRepository.findById(submissionDTO.getChallenge()).orElseThrow(NotFoundException::new);

        if (!submissionDTO.getFlag().equals(challenge.getFlag())) {
            throw new WrongAnswerException("Keep on trying!");
        }

        AppUser user = appUserRepository.findById(submissionDTO.getUser()).orElseThrow(NotFoundException::new);

        user.setPoints(user.getPoints() + challenge.getPoints());
        final Submission submission = new Submission();
        mapToEntity(submissionDTO, submission);
        return submissionRepository.save(submission).getId();
    }

    public void update(final Long id, final SubmissionDTO submissionDTO) {
        final Submission submission = submissionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(submissionDTO, submission);
        submissionRepository.save(submission);
    }

    public void delete(final Long id) {
        submissionRepository.deleteById(id);
    }

    private SubmissionDTO mapToDTO(final Submission submission, final SubmissionDTO submissionDTO) {
        submissionDTO.setId(submission.getId());
        submissionDTO.setUser(submission.getUser() == null ? null : submission.getUser().getId());
        submissionDTO.setChallenge(submission.getChallenge() == null ? null : submission.getChallenge().getId());
        return submissionDTO;
    }

    private Submission mapToEntity(final SubmissionDTO submissionDTO, final Submission submission) {
        final AppUser user = submissionDTO.getUser() == null ? null : appUserRepository.findById(submissionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        submission.setUser(user);
        final Challenge challenge = submissionDTO.getChallenge() == null ? null : challengeRepository.findById(submissionDTO.getChallenge())
                .orElseThrow(() -> new NotFoundException("challenge not found"));
        submission.setChallenge(challenge);
        return submission;
    }

}
