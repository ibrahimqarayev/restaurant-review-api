package com.ibrahim.restaurant.mapper;

import com.ibrahim.restaurant.dto.photo.PhotoDto;
import com.ibrahim.restaurant.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDto toDto(Photo photo);
}
