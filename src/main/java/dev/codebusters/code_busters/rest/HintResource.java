package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.HintDTO;
import dev.codebusters.code_busters.service.HintService;
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
@RequestMapping(value = "/hints", produces = MediaType.APPLICATION_JSON_VALUE)
public class HintResource {

    private final HintService hintService;

    public HintResource(final HintService hintService) {
        this.hintService = hintService;
    }

    @GetMapping
    public ResponseEntity<List<HintDTO>> getAllHints() {
        return ResponseEntity.ok(hintService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HintDTO> getHint(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(hintService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createHint(@RequestBody @Valid final HintDTO hintDTO) {
        final Long createdId = hintService.create(hintDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateHint(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final HintDTO hintDTO) {
        hintService.update(id, hintDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteHint(@PathVariable(name = "id") final Long id) {
        hintService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
