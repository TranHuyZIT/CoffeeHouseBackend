package com.tma.User.Mapper;

import com.tma.User.DTO.UserListResponseDTO;
import com.tma.User.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserListResponseMapper {
    UserListResponseDTO modelToDTO(User user);
    Set<UserListResponseDTO> modelsTODTOS(List<User> users);
}
