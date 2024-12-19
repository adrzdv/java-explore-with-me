package ru.practicum.enw.model.mapper.mapstruct;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.enw.model.entity.Category;
import ru.practicum.enw.model.entity.Event;
import ru.practicum.enw.model.event.UpdateEventAdminRequest;
import ru.practicum.enw.model.event.UpdateEventUserRequest;

@Mapper(componentModel = "spring")
public interface EventMapperMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFullEventDtoFromNewEventDto(UpdateEventUserRequest updEvent, @MappingTarget Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFullEventDtoFromUpdateEventAdminRequest(UpdateEventAdminRequest updEvent, @MappingTarget Event event);

    default Category map(long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

}