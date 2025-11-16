package com.example.clothingandaccessoriesstore.map.liked;

import com.example.clothingandaccessoriesstore.dto.liked.LikedResponseDto;
import com.example.clothingandaccessoriesstore.entity.Liked;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LikedMapper {
LikedResponseDto toResponseDto(Liked liked);
List<LikedResponseDto> toResponseDtoList(List<Liked> likeds);
}
