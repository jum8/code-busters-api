package dev.codebusters.code_busters.rest;

import com.fasterxml.jackson.annotation.JsonView;
import dev.codebusters.code_busters.model.*;
import dev.codebusters.code_busters.service.ChallengeService;
import dev.codebusters.code_busters.util.ReferencedException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/challenges", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChallengeResource {

    private final ChallengeService challengeService;

    public ChallengeResource(final ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ChallengeSummaryDTO>> getAllChallenges() {
        return ResponseEntity.ok(challengeService.findAll());
    }

    @GetMapping("/exposed")
    public ResponseEntity<List<ChallengeSummaryDTO>> getExposedChallenges(
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        return ResponseEntity.ok(challengeService.findExposedChallengesByCategoryId(categoryId));
    }

    @GetMapping("/exposed/popular")
    public ResponseEntity<List<ChallengeSummaryDTO>> getMostPopularExposedChallenges(
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        return ResponseEntity.ok(challengeService.findMostPopularExposedChallenges(limit));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ChallengeSummaryDTO>> searchChallenges(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "exposed", required = false) Boolean exposed,
            @RequestParam(name = "level", required = false) ChallengeLevel level,
            @RequestParam(name = "premium", required = false) Boolean premium,
            @RequestParam(name = "subscription", required = false) @Parameter(schema = @Schema(allowableValues = {"FREE", "PAID"})) String subscription) {
        List<ChallengeSummaryDTO> challenges = challengeService.searchChallenges(categoryId, exposed, level, premium, subscription);
        return ResponseEntity.ok(challenges);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'PREMIUM') or (hasRole('USER') and @challengeService.isChallengeNotPremium(#id))")
    @GetMapping("/{id}")
    @JsonView({ChallengeView.GetById.class})
    public ResponseEntity<ChallengeUpdateDTO> getChallenge(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(challengeService.get(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/admin")
    @JsonView({ChallengeView.GetByIdAdmin.class})
    public ResponseEntity<ChallengeUpdateDTO> getChallengeAdmin(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(challengeService.get(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createChallenge(
            @RequestBody @Valid final ChallengeCreationDTO challengeCreationDTO) {
        final Long createdId = challengeService.create(challengeCreationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateChallenge(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ChallengeUpdateDTO challengeUpdateDTO) {
        challengeService.update(id, challengeUpdateDTO);
        return ResponseEntity.ok(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/exposition/{id}")
    public ResponseEntity<Long> updateChallengeExposition(@PathVariable(name = "id") final Long id,
            @RequestBody final Boolean exposed) {
        challengeService.updateExposition(id, exposed);
        return ResponseEntity.ok(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteChallenge(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = challengeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        challengeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
