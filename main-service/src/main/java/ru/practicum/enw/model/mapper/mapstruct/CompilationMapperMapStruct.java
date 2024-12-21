package ru.practicum.enw.model.mapper.mapstruct;

import org.mapstruct.*;
import ru.practicum.enw.model.entity.Compilation;
import ru.practicum.enw.model.compilation.UpdateCompilationRequest;

@Mapper(componentModel = "spring")
public interface CompilationMapperMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    void updateCompilationFromCompilationRequestToEntity(UpdateCompilationRequest updComp,
                                                         @MappingTarget Compilation compilation);

}
