package com.example.testlinux.mapper;

import com.example.testlinux.domain.Event;
import com.example.testlinux.dto.EventDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {
                EventFileMapper.class
        },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(source = "sportId", target = "n1")
    EventDto toDto(Event event);

    @InheritInverseConfiguration
    Event toEntity(EventDto dto);

/*    Event toModel(EventView view);

    List<Event> toModelList(List<EventView> views);

    List<EventDto> toDtoList(List<Event> events);

    List<Event> toEntityList(List<EventDto> dtos);*/

}
