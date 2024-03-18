package dev.codebusters.code_busters.rest;

import dev.codebusters.code_busters.model.CityDTO;
import dev.codebusters.code_busters.service.CityService;
import dev.codebusters.code_busters.util.ReferencedException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityResource {

    private final CityService cityService;

    public CityResource(final CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("{country_id}")
    public ResponseEntity<List<CityDTO>> getAllCitiesByCountryId(@PathVariable(name = "country_id") final Long countryId) {
        return ResponseEntity.ok(cityService.findByCountryId(countryId));
    }


}