package ru.practicum.enw.controller.external;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.category.CategoryDto;
import ru.practicum.enw.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
@AllArgsConstructor
public class CategoriesPublicController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                    @RequestParam(required = false, defaultValue = "10") int size) {

        return categoryService.getCategoryListByParams(from, size);

    }

    @GetMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    CategoryDto getCategory(@PathVariable long id) throws NotFoundCustomException {

        return categoryService.getById(id);
    }


}
