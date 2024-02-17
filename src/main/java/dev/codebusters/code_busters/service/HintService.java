package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.domain.Hint;
import dev.codebusters.code_busters.model.HintDTO;
import dev.codebusters.code_busters.repos.ChallengeRepository;
import dev.codebusters.code_busters.repos.HintRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HintService {

    private final HintRepository hintRepository;
    private final ChallengeRepository challengeRepository;

    public HintService(final HintRepository hintRepository,
            final ChallengeRepository challengeRepository) {
        this.hintRepository = hintRepository;
        this.challengeRepository = challengeRepository;
    }

    public List<HintDTO> findAll() {
        final List<Hint> hints = hintRepository.findAll(Sort.by("id"));
        return hints.stream()
                .map(hint -> mapToDTO(hint, new HintDTO()))
                .toList();
    }

    public HintDTO get(final Long id) {
        return hintRepository.findById(id)
                .map(hint -> mapToDTO(hint, new HintDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final HintDTO hintDTO) {
        final Hint hint = new Hint();
        mapToEntity(hintDTO, hint);
        return hintRepository.save(hint).getId();
    }

    public void update(final Long id, final HintDTO hintDTO) {
        final Hint hint = hintRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(hintDTO, hint);
        hintRepository.save(hint);
    }

    public void delete(final Long id) {
        hintRepository.deleteById(id);
    }

    private HintDTO mapToDTO(final Hint hint, final HintDTO hintDTO) {
        hintDTO.setId(hint.getId());
        hintDTO.setVisible(hint.getVisible());
        hintDTO.setDescription(hint.getDescription());
        hintDTO.setChallenge(hint.getChallenge() == null ? null : hint.getChallenge().getId());
        return hintDTO;
    }

    private Hint mapToEntity(final HintDTO hintDTO, final Hint hint) {
        hint.setVisible(hintDTO.getVisible());
        hint.setDescription(hintDTO.getDescription());
        final Challenge challenge = hintDTO.getChallenge() == null ? null : challengeRepository.findById(hintDTO.getChallenge())
                .orElseThrow(() -> new NotFoundException("challenge not found"));
        hint.setChallenge(challenge);
        return hint;
    }

}
