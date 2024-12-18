package ru.practicum.enw.service.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enw.exceptions.ConflictCustomException;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.category.CategoryDto;
import ru.practicum.enw.model.category.NewCategoryDto;
import ru.practicum.enw.model.entity.Category;
import ru.practicum.enw.model.mapper.custom.CategoryMapper;
import ru.practicum.enw.repo.CategoryRepo;
import ru.practicum.enw.repo.EventRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepo repo;
    private final EventRepo eventRepo;

    @Override
    @Transactional
    public CategoryDto add(NewCategoryDto categoryDto) {

        Category category = CategoryMapper.fromNewToEntity(categoryDto);


        return CategoryMapper.toDtoFromEntity(repo.save(category));
    }

    @Override
    @Transactional
    public void delete(long id) throws NotFoundCustomException {
        if (repo.findById(id).isEmpty()) {
            throw new NotFoundCustomException("Category with id=" + id + " not found");
        }

        if (!eventRepo.findByCategoryId(id).isEmpty()) {
            throw new ConflictCustomException("Category with id=" + id + " not available");
        }

        repo.deleteById(id);
    }

    @Override
    @Transactional
    public CategoryDto update(long id, NewCategoryDto newCategoryDto) throws NotFoundCustomException {
        if (repo.findById(id).isEmpty()) {
            throw new NotFoundCustomException("Category with id=" + id + " not found");
        }

        Category category = CategoryMapper.toDtoFromNewCategoryDtoWithId(id, newCategoryDto);

        return CategoryMapper.toDtoFromEntity(repo.save(category));
    }

    @Override
    public CategoryDto getById(long id) throws NotFoundCustomException {

        return CategoryMapper.toDtoFromEntity(repo.findById(id)
                .orElseThrow(() -> new NotFoundCustomException("Category with id=" + id + " not found")));
    }

    @Override
    public List<CategoryDto> getCategoryListByParams(int from, int size) {

        return repo.getCategoriesByParams(from, size).stream()
                .map(CategoryMapper::toDtoFromEntity)
                .toList();
    }


}
