package com.tma.coffeehouse.CartDetails.Mapper;

import com.tma.coffeehouse.CartDetails.CartDetail;
import com.tma.coffeehouse.CartDetails.DTO.DetailOfCartDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface DetailOfCartMapper {
    DetailOfCartDTO modelTODto(CartDetail cartDetail);
    Set<DetailOfCartDTO> modelsTODTOS(Set<CartDetail> cartDetails);
}
