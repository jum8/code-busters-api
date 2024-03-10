package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.domain.Hint;
import dev.codebusters.code_busters.model.ChallengeLevel;
import dev.codebusters.code_busters.model.ChallengeSummaryDTO;
import dev.codebusters.code_busters.model.HintDTO;
import dev.codebusters.code_busters.service.HintService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/hints", produces = MediaType.APPLICATION_JSON_VALUE)
public class HintResource {

    private final HintService hintService;

    public HintResource(final HintService hintService) {
        this.hintService = hintService;
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<HintDTO>> getAllHints() {
        return ResponseEntity.ok(hintService.findAll());
    }

    @GetMapping("/get-one")
    public ResponseEntity<HintDTO> searchChallenges(
            @RequestParam(name = "challengeId") Long challengeId,
            @RequestParam(name = "order") Integer order) {
        return ResponseEntity.ok(hintService.getByChallengeIdAndOrder(challengeId, order));
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<HintDTO> getHint(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(hintService.get(id));
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createHint(@RequestBody @Valid final HintDTO hintDTO) {
        final Long createdId = hintService.create(hintDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateHint(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final HintDTO hintDTO) {
        hintService.update(id, hintDTO);
        return ResponseEntity.ok(id);
    }

    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteHint(@PathVariable(name = "id") final Long id) {
        hintService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
