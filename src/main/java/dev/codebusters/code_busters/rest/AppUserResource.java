package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.AppUserDTO;
import dev.codebusters.code_busters.model.UserRankingDTO;
import dev.codebusters.code_busters.service.AppUserService;
import dev.codebusters.code_busters.util.ReferencedException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserResource {

    private final AppUserService appUserService;

    public AppUserResource(final AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllAppUsers() {
        return ResponseEntity.ok(appUserService.findAll());
    }

    @GetMapping("ranking")
    public ResponseEntity<List<UserRankingDTO>> getAllAppUsersSortedByPoints(
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        return ResponseEntity.ok(appUserService.findAllSortedByPoints(limit));
    }


    @PreAuthorize("hasRole('ADMIN') or (!hasRole('ADMIN') and #id == principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(appUserService.get(id));
    }

    @PreAuthorize("hasRole('ADMIN') or (!hasRole('ADMIN') and #id == principal.id)")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAppUser(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AppUserDTO appUserDTO) {
        appUserService.update(id, appUserDTO);
        return ResponseEntity.ok(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAppUser(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = appUserService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        appUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
