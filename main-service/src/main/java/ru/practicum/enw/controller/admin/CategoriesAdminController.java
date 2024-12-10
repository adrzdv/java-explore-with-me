package ru.practicum.enw.controller.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.category.CategoryDto;
import ru.practicum.enw.model.category.NewCategoryDto;
import ru.practicum.enw.service.category.CategoryService;

@RestController
@RequestMapping(value = "/admin/categories")
@AllArgsConstructor
public class CategoriesAdminController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto add(@Valid @RequestBody NewCategoryDto category) {

        return service.add(category);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    CategoryDto update(@PathVariable long id,
                       @RequestBody NewCategoryDto newCategoryDto) throws NotFoundCustomException {

        return service.update(id, newCategoryDto);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable long id) throws NotFoundCustomException {

        service.delete(id);
    }

}
