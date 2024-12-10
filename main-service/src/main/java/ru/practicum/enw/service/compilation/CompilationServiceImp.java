package ru.practicum.enw.service.compilation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enw.exceptions.ConflictCustomException;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.entity.Compilation;
import ru.practicum.enw.model.entity.Event;
import ru.practicum.enw.model.compilation.CompilationDto;
import ru.practicum.enw.model.compilation.NewCompilationDto;
import ru.practicum.enw.model.compilation.UpdateCompilationRequest;
import ru.practicum.enw.model.mapper.custom.CompilationMapper;
import ru.practicum.enw.model.mapper.mapstruct.CompilationMapperMapStruct;
import ru.practicum.enw.repo.CompilationRepo;
import ru.practicum.enw.repo.EventRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class CompilationServiceImp implements CompilationService {

    private final CompilationRepo compilationRepo;
    private final EventRepo eventRepo;
    private final CompilationMapperMapStruct compilationMapStruct;

    @Override
    @Transactional
    public CompilationDto addNew(NewCompilationDto newCompilation) throws ConflictCustomException {

        List<Event> events = eventRepo.findByIdIn(newCompilation.getEvents());

        if (events.isEmpty()) {
            throw new ConflictCustomException("One or all events in list not found");
        }

        Compilation compilation = Compilation.builder()
                .title(newCompilation.getTitle())
                .pinned(newCompilation.getPinned())
                .events(events)
                .build();

        return CompilationMapper.fromEntityToDto(compilationRepo.save(compilation));
    }

    @Override
    @Transactional
    public void delete(long id) {

        compilationRepo.deleteById(id);
    }

    @Override
    @Transactional
    public CompilationDto update(long id, UpdateCompilationRequest updCompilation) throws NotFoundCustomException {

        Compilation compilation = compilationRepo.findById(id)
                .orElseThrow(() -> new NotFoundCustomException("Error: Compilation with id=" + id + " not found"));

        compilationMapStruct.updateCompilationFromCompilationRequestToEntity(updCompilation, compilation);

        if (updCompilation.getEvents() != null) {

            List<Event> events = eventRepo.findByIdIn(updCompilation.getEvents());
            compilation.setEvents(events);

        }

        return CompilationMapper.fromEntityToDto(compilationRepo.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int size, int from) {
        return compilationRepo.getCompilationsByParams(pinned, size, from).stream()
                .map(CompilationMapper::fromEntityToDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(long id) throws NotFoundCustomException {
        return CompilationMapper.fromEntityToDto(compilationRepo.findById(id)
                .orElseThrow(() -> new NotFoundCustomException("Error: Compilation with id=" + id + " not found")));
    }
}
