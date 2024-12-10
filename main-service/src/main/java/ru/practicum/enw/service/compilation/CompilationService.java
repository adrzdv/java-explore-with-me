package ru.practicum.enw.service.compilation;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.compilation.CompilationDto;
import ru.practicum.enw.model.compilation.NewCompilationDto;
import ru.practicum.enw.model.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto addNew(NewCompilationDto newCompilation) throws NotFoundCustomException;

    void delete(long id);

    CompilationDto update(long id, UpdateCompilationRequest updCompilation) throws NotFoundCustomException;

    List<CompilationDto> getCompilations(Boolean pinned, int size, int from);

    CompilationDto getCompilationById(long id) throws NotFoundCustomException;
}
