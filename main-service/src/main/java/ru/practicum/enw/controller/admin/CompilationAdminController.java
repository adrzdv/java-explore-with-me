package ru.practicum.enw.controller.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.compilation.CompilationDto;
import ru.practicum.enw.model.compilation.NewCompilationDto;
import ru.practicum.enw.model.compilation.UpdateCompilationRequest;
import ru.practicum.enw.service.compilation.CompilationService;

@RestController
@RequestMapping(value = "/admin/compilations")
@AllArgsConstructor
public class CompilationAdminController {

    private final CompilationService compilationService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilation)
            throws NotFoundCustomException {

        return compilationService.addNew(compilation);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long id) {

        compilationService.delete(id);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable long id,
                                            @Valid @RequestBody UpdateCompilationRequest compilation)
            throws NotFoundCustomException {

        return compilationService.update(id, compilation);
    }

}
