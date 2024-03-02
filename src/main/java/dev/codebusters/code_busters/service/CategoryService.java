package dev.codebusters.code_busters.service;

import dev.codebusters.code_busters.domain.Category;
import dev.codebusters.code_busters.domain.Challenge;
import dev.codebusters.code_busters.model.CategoryManipulationDTO;
import dev.codebusters.code_busters.model.CategoryDTO;
import dev.codebusters.code_busters.repos.CategoryRepository;
import dev.codebusters.code_busters.repos.ChallengeRepository;
import dev.codebusters.code_busters.util.NotFoundException;
import dev.codebusters.code_busters.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ChallengeRepository challengeRepository;

    public CategoryService(final CategoryRepository categoryRepository,
            final ChallengeRepository challengeRepository) {
        this.categoryRepository = categoryRepository;
        this.challengeRepository = challengeRepository;
    }

    public List<CategoryDTO> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public List<CategoryDTO> findExposedCategories() {
        final List<Category> categories = categoryRepository.findByExposedTrue();
        return categories.stream()
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> mapToDTO(category, new CategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoryManipulationDTO categoryManipulationDTO) {
        final Category category = new Category();
        mapToEntity(categoryManipulationDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryManipulationDTO categoryManipulationDTO) {
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoryManipulationDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setAdded(category.getAdded());
        categoryDTO.setTitle(category.getTitle());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setExposed(category.getExposed());
        return categoryDTO;
    }

    private Category mapToEntity(final CategoryManipulationDTO categoryManipulationDTO, final Category category) {
        category.setTitle(categoryManipulationDTO.getTitle());
        category.setDescription(categoryManipulationDTO.getDescription());
        category.setExposed(categoryManipulationDTO.getExposed());
        return category;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Category category = categoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Challenge categoryChallenge = challengeRepository.findFirstByCategory(category);
        if (categoryChallenge != null) {
            referencedWarning.setKey("category.challenge.category.referenced");
            referencedWarning.addParam(categoryChallenge.getId());
            return referencedWarning;
        }
        return null;
    }

}
