package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.Category;
import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.domain.Hint;
import dev.codebusters.code_busters.domain.Submission;
import dev.codebusters.code_busters.model.*;
import dev.codebusters.code_busters.repos.CategoryRepository;
import dev.codebusters.code_busters.repos.ChallengeRepository;
import dev.codebusters.code_busters.repos.HintRepository;
import dev.codebusters.code_busters.repos.SubmissionRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional
    public List<ChallengeSummaryDTO> findExposedChallengesByCategoryId(Long categoryId) {
        final List<Challenge> challenges = challengeRepository.findExposedChallengesByCategoryId(categoryId);
        return challenges.stream()
                .map(challenge -> mapToSummaryDTO(challenge, new ChallengeSummaryDTO()))
                .toList();
    }

    @Transactional
    public List<ChallengeSummaryDTO> searchChallenges(Long categoryId, Boolean exposed, ChallengeLevel level, Boolean premium, String subscription) {
        final List<Challenge> challenges = challengeRepository.searchChallenges(categoryId, exposed, level, premium, subscription);
        return challenges.stream()
                .map(challenge -> mapToSummaryDTO(challenge, new ChallengeSummaryDTO()))
                .toList();
    }

    @Transactional
    public ChallengeUpdateDTO get(final Long id) {
        return challengeRepository.findById(id)
                .map(challenge -> mapToUpdateDTO(challenge, new ChallengeUpdateDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Long create(final ChallengeCreationDTO challengeCreationDTO) {
        final Challenge challenge = new Challenge();

        mapChallegeCreatioDTOToEntity(challengeCreationDTO, challenge);
        return challengeRepository.save(challenge).getId();
    }

    @Transactional
    public void update(final Long id, final ChallengeUpdateDTO challengeUpdateDTO) {
        final Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapChallegeUpdateDTOToEntity(challengeUpdateDTO, challenge);
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
        challengeDTO.setImageUrl(challenge.getImageUrl());
        challengeDTO.setPremium(challenge.getPremium());
        challengeDTO.setCategory(challenge.getCategory() == null ? null : challenge.getCategory().getId());
        return challengeDTO;
    }

    private ChallengeCreationDTO mapToCreationDTO(final Challenge challenge, final ChallengeCreationDTO challengeCreationDTO) {
        challengeCreationDTO.setTitle(challenge.getTitle());
        challengeCreationDTO.setDescription(challenge.getDescription());
        challengeCreationDTO.setExposed(challenge.getExposed());
        challengeCreationDTO.setFlag(challenge.getFlag());
        challengeCreationDTO.setPoints(challenge.getPoints());
        challengeCreationDTO.setCredits(challenge.getCredits());
        challengeCreationDTO.setLevel(challenge.getLevel());
        challengeCreationDTO.setImageUrl(challenge.getImageUrl());
        challengeCreationDTO.setPremium(challenge.getPremium());
        challengeCreationDTO.setCategory(challenge.getCategory() == null ? null : challenge.getCategory().getId());
        final Set<HintCreationDTO> hints = challenge.getHints().stream()
                .map(hint -> mapToHintCreationDTO(hint, new HintCreationDTO()))
                .collect(Collectors.toSet());
        challengeCreationDTO.setHints(hints);

        return challengeCreationDTO;
    }

    private ChallengeUpdateDTO mapToUpdateDTO(final Challenge challenge, final ChallengeUpdateDTO challengeUpdateDTO) {
        challengeUpdateDTO.setTitle(challenge.getTitle());
        challengeUpdateDTO.setDescription(challenge.getDescription());
        challengeUpdateDTO.setExposed(challenge.getExposed());
        challengeUpdateDTO.setFlag(challenge.getFlag());
        challengeUpdateDTO.setPoints(challenge.getPoints());
        challengeUpdateDTO.setCredits(challenge.getCredits());
        challengeUpdateDTO.setLevel(challenge.getLevel());
        challengeUpdateDTO.setImageUrl(challenge.getImageUrl());
        challengeUpdateDTO.setPremium(challenge.getPremium());
        challengeUpdateDTO.setCategory(challenge.getCategory() == null ? null : challenge.getCategory().getId());
        final Set<HintUpdateDTO> hints = challenge.getHints().stream()
                .map(hint -> mapToHintUpdateDTO(hint, new HintUpdateDTO()))
                .collect(Collectors.toSet());
        challengeUpdateDTO.setHints(hints);

        return challengeUpdateDTO;
    }



    private ChallengeSummaryDTO mapToSummaryDTO(final Challenge challenge, final ChallengeSummaryDTO challengeSummaryDTO) {
        challengeSummaryDTO.setId(challenge.getId());
        challengeSummaryDTO.setTitle(challenge.getTitle());
        challengeSummaryDTO.setExposed(challenge.getExposed());
        challengeSummaryDTO.setPoints(challenge.getPoints());
        challengeSummaryDTO.setCredits(challenge.getCredits());
        challengeSummaryDTO.setLevel(challenge.getLevel());
        challengeSummaryDTO.setImageUrl(challenge.getImageUrl());
        challengeSummaryDTO.setPremium(challenge.getPremium());
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
        challenge.setImageUrl(challengeDTO.getImageUrl());
        challenge.setPremium(challengeDTO.getPremium());
        final Category category = challengeDTO.getCategory() == null ? null : categoryRepository.findById(challengeDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        challenge.setCategory(category);
        return challenge;
    }

    private Challenge mapChallegeCreatioDTOToEntity(final ChallengeCreationDTO challengeCreationDTO, final Challenge challenge) {
        challenge.setTitle(challengeCreationDTO.getTitle());
        challenge.setDescription(challengeCreationDTO.getDescription());
        challenge.setExposed(challengeCreationDTO.getExposed());
        challenge.setFlag(challengeCreationDTO.getFlag());
        challenge.setPoints(challengeCreationDTO.getPoints());
        challenge.setCredits(challengeCreationDTO.getCredits());
        challenge.setLevel(challengeCreationDTO.getLevel());
        challenge.setImageUrl(challengeCreationDTO.getImageUrl());
        challenge.setPremium(challengeCreationDTO.getPremium());
        final Category category = challengeCreationDTO.getCategory() == null ? null : categoryRepository.findById(challengeCreationDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        challenge.setCategory(category);
        final Set<Hint> hints = challengeCreationDTO.getHints().stream()
                .map(hintCreationDTO -> mapHintCreationDTOToEntity(hintCreationDTO, new Hint()))
                .collect(Collectors.toSet());
        challenge.setHints(hints);
        hints.forEach(hint -> hint.setChallenge(challenge));
        return challenge;
    }

    private Challenge mapChallegeUpdateDTOToEntity(final ChallengeUpdateDTO challengeUpdateDTO, final Challenge challenge) {
        challenge.setTitle(challengeUpdateDTO.getTitle());
        challenge.setDescription(challengeUpdateDTO.getDescription());
        challenge.setExposed(challengeUpdateDTO.getExposed());
        challenge.setFlag(challengeUpdateDTO.getFlag());
        challenge.setPoints(challengeUpdateDTO.getPoints());
        challenge.setCredits(challengeUpdateDTO.getCredits());
        challenge.setLevel(challengeUpdateDTO.getLevel());
        challenge.setImageUrl(challengeUpdateDTO.getImageUrl());
        challenge.setPremium(challengeUpdateDTO.getPremium());
        final Category category = challengeUpdateDTO.getCategory() == null ? null : categoryRepository.findById(challengeUpdateDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        challenge.setCategory(category);
        final Set<Hint> hints = challengeUpdateDTO.getHints().stream()
                .map(hintUpdateDTO -> mapHintUpdateDTOToEntity(hintUpdateDTO, new Hint()))
                .collect(Collectors.toSet());
        challenge.getHints().clear();
        challenge.getHints().addAll(hints);
        hints.forEach(hint -> hint.setChallenge(challenge));
        return challenge;
    }

    private Hint mapHintCreationDTOToEntity(final HintCreationDTO hintCreationDTO, final Hint hint) {
        hint.setVisible(hintCreationDTO.getVisible());
        hint.setDescription(hintCreationDTO.getDescription());
        return hint;
    }

    private Hint mapHintUpdateDTOToEntity(final HintUpdateDTO hintUpdateDTO, final Hint hint) {
        hint.setId(hintUpdateDTO.getId());
        hint.setVisible(hintUpdateDTO.getVisible());
        hint.setDescription(hintUpdateDTO.getDescription());
        return hint;
    }

    private HintCreationDTO mapToHintCreationDTO(final Hint hint, final HintCreationDTO hintCreationDTO) {
        hintCreationDTO.setVisible(hint.getVisible());
        hintCreationDTO.setDescription(hint.getDescription());
        return hintCreationDTO;
    }

    private HintUpdateDTO mapToHintUpdateDTO(final Hint hint, final HintUpdateDTO hintUpdateDTO) {
        hintUpdateDTO.setId(hint.getId());
        hintUpdateDTO.setVisible(hint.getVisible());
        hintUpdateDTO.setDescription(hint.getDescription());
        return hintUpdateDTO;
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

        return null;
    }

}
