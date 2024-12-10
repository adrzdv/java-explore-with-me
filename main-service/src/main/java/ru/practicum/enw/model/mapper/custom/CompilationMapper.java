package ru.practicum.enw.model.mapper.custom;

import ru.practicum.enw.model.entity.Compilation;
import ru.practicum.enw.model.compilation.CompilationDto;

public class CompilationMapper {

    public static CompilationDto fromEntityToDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toShortDto)
                        .toList())
                .build();
    }
}
