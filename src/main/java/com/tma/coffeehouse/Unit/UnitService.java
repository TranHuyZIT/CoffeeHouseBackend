package com.tma.coffeehouse.Unit;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Unit.DTO.UnitDTO;
import com.tma.coffeehouse.Unit.Mapper.UnitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;
    public List<UnitDTO> findAll(){
        return unitMapper.modelsTODTOS(unitRepository.findAll());
    }
    public UnitDTO findById(long id) {
        return unitMapper.modelTODTO(
                unitRepository.findById(id)
                        .orElseThrow(()->new CustomException("Không tìm thấy đơn vị với mã " + id, HttpStatus.NOT_FOUND))
        );
    }
    public UnitDTO insert(Unit unit) {
        Unit saved = unitRepository.save(unit);
        return unitMapper.modelTODTO(saved);
    }
    public UnitDTO update(long id, Unit newUnit){
        Unit currentUnit = unitRepository.findById(id).orElseThrow(
                () -> new CustomException("Không tìm thấy đơn vị với mã " + id, HttpStatus.NOT_FOUND));
         currentUnit.setName(newUnit.getName());
         currentUnit.setPrice(newUnit.getPrice());
         Unit saved = unitRepository.save(currentUnit);
         return unitMapper.modelTODTO(
                 saved
         );
    }
    public UnitDTO delete(long id){
        Unit currentUnit = unitRepository.findById(id).orElseThrow(
                () -> new CustomException("Không tìm thấy đơn vị với mã " + id, HttpStatus.NOT_FOUND));
        unitRepository.deleteById(id);
        return  unitMapper.modelTODTO(
                currentUnit
        );
    }
}
