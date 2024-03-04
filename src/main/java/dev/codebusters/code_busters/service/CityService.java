package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.City;
import dev.codebusters.code_busters.model.CityDTO;
import dev.codebusters.code_busters.repos.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CityService {

    private final CityRepository cityRepository;


    public CityService(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;

    }

    public List<CityDTO> findByCountryId(Long countryId) {
        final List<City> cities = cityRepository.findByCountryId(countryId);
        return cities.stream()
                .map(city -> mapToDTO(city, new CityDTO()))
                .toList();
    }


    private CityDTO mapToDTO(final City city, final CityDTO cityDTO) {
        cityDTO.setId(city.getId());
        cityDTO.setCityName(city.getCityName());
        cityDTO.setCityCode(city.getCityCode());
        cityDTO.setCountry(city.getCountry() == null ? null : city.getCountry().getId());
        return cityDTO;
    }


}
