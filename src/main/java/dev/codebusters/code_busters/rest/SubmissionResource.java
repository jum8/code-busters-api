package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.SubmissionDTO;
import dev.codebusters.code_busters.service.SubmissionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/submissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubmissionResource {

    private final SubmissionService submissionService;

    public SubmissionResource(final SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    public ResponseEntity<List<SubmissionDTO>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(submissionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSubmission(
            @RequestBody @Valid final SubmissionDTO submissionDTO) {
        final Long createdId = submissionService.create(submissionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSubmission(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SubmissionDTO submissionDTO) {
        submissionService.update(id, submissionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSubmission(@PathVariable(name = "id") final Long id) {
        submissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
