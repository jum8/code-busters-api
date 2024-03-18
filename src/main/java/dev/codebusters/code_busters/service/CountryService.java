package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.AppUser;
import dev.codebusters.code_busters.domain.City;
import dev.codebusters.code_busters.domain.Country;
import dev.codebusters.code_busters.model.CountryDTO;
import dev.codebusters.code_busters.repos.CountryRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    public List<CountryDTO> findAll() {
        final List<Country> countries = countryRepository.findAll(Sort.by("id"));
        return countries.stream()
                .map(country -> mapToDTO(country, new CountryDTO()))
                .toList();
    }


    private CountryDTO mapToDTO(final Country country, final CountryDTO countryDTO) {
        countryDTO.setId(country.getId());
        countryDTO.setCountryName(country.getCountryName());
        countryDTO.setCountryCode(country.getCountryCode());
        return countryDTO;
    }

}
