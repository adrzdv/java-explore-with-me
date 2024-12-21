package ru.practicum.enw.controller.external;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.compilation.CompilationDto;
import ru.practicum.enw.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping(value = "/compilations")
@AllArgsConstructor
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") int from,
                                                @RequestParam(required = false, defaultValue = "10") int size) {

        return compilationService.getCompilations(pinned, size, from);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable long id) throws NotFoundCustomException {

        return compilationService.getCompilationById(id);
    }
}
