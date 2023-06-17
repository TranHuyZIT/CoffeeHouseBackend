package com.tma.coffeehouse.User;


import com.tma.coffeehouse.User.DTO.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userName", expression = "java(user.getUsername())")
    UserResponseDTO modelTODTO(User user);


}
