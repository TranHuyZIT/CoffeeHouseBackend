package com.tma.coffeehouse.Unit.Mapper;

import com.tma.coffeehouse.Unit.DTO.UnitDTO;
import com.tma.coffeehouse.Unit.Unit;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    Unit dtoTOModel (UnitDTO unitDTO);
    UnitDTO modelTODTO (Unit unit);
    List<UnitDTO> modelsTODTOS(List<Unit> units);
}
