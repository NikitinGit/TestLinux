package com.example.testlinux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
/*        uses = {
                FederalDistrictMapper.class
        },*/
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventFileMapper {

/*    @Mapping(target = "event", ignore = true)
    EventFileDto toDto(EventFile entity);

    @Mapping(target = "event", ignore = true)
    EventFile toEntity(EventFileDto dto);

    List<EventFileDto> toDtoList(List<EventFile> entities);

    List<EventFile> toEntityList(List<EventFileDto> dtos);*/
}
