package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.ChallengeDTO;
import dev.codebusters.code_busters.service.ChallengeService;
import dev.codebusters.code_busters.util.ReferencedException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/challenges", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChallengeResource {

    private final ChallengeService challengeService;

    public ChallengeResource(final ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {
        return ResponseEntity.ok(challengeService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDTO> getChallenge(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(challengeService.get(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createChallenge(
            @RequestBody @Valid final ChallengeDTO challengeDTO) {
        final Long createdId = challengeService.create(challengeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateChallenge(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ChallengeDTO challengeDTO) {
        challengeService.update(id, challengeDTO);
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
