package com.tma.coffeehouse.User.Mapper;

import com.tma.coffeehouse.User.DTO.UserListResponseDTO;
import com.tma.coffeehouse.User.User;
import com.tma.coffeehouse.Voucher.DTO.VoucherDTO;
import com.tma.coffeehouse.Voucher.Voucher;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserListResponseMapper {
    UserListResponseDTO modelToDTO(User user);
    Set<UserListResponseDTO> modelsTODTOS(List<User> users);
}
