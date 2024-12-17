package ru.practicum.enw.service.compilation;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.compilation.CompilationDto;
import ru.practicum.enw.model.compilation.NewCompilationDto;
import ru.practicum.enw.model.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    /**
     * Add a new compilation
     *
     * @param newCompilation compilation for add
     * @return CompilationDTO object
     * @throws NotFoundCustomException
     */
    CompilationDto addNew(NewCompilationDto newCompilation) throws NotFoundCustomException;

    /**
     * Delete an existing compilation by ID
     *
     * @param id identification number
     */
    void delete(long id);

    /**
     * Update an existing compilation
     *
     * @param id             identification number of compilation
     * @param updCompilation Compilation for update
     * @return CompilationDto
     * @throws NotFoundCustomException
     */
    CompilationDto update(long id, UpdateCompilationRequest updCompilation) throws NotFoundCustomException;

    /**
     * Get an existing compilation having parameters
     *
     * @param pinned true/false if compilation pined
     * @param size   the number of list size
     * @param from   the number of skipping records
     * @return
     */
    List<CompilationDto> getCompilations(Boolean pinned, int size, int from);

    /**
     * Get an existing compilation by ID
     *
     * @param id identification number
     * @return CompilationDTO
     * @throws NotFoundCustomException
     */
    CompilationDto getCompilationById(long id) throws NotFoundCustomException;
}
