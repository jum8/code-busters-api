package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.SubmissionDTO;
import dev.codebusters.code_busters.service.ChallengeService;
import dev.codebusters.code_busters.service.SubmissionService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/submissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubmissionResource {

    private final SubmissionService submissionService;
    private final ChallengeService challengeService;

    public SubmissionResource(final SubmissionService submissionService, ChallengeService challengeService) {
        this.submissionService = submissionService;
        this.challengeService = challengeService;
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SubmissionDTO>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.findAll());
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(submissionService.get(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PREMIUM') or (hasRole('USER') and @challengeService.isChallengeNotPremium(#submissionDTO.getChallenge()))")
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSubmission(
            @RequestBody @Valid final SubmissionDTO submissionDTO) {
        final Long createdId = submissionService.create(submissionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSubmission(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SubmissionDTO submissionDTO) {
        submissionService.update(id, submissionDTO);
        return ResponseEntity.ok(id);
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSubmission(@PathVariable(name = "id") final Long id) {
        submissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
