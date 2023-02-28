package com.tma.coffeehouse.Unit;

import com.tma.coffeehouse.Unit.DTO.UnitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/unit")
@RequiredArgsConstructor
public class UnitController {
    private final UnitService unitService;
    @GetMapping
    public List<UnitDTO> findAll() {
        return unitService.findAll();
    }
    @GetMapping("/{id}")
    public UnitDTO findById(@PathVariable long id){
        return unitService.findById(id);
    }
    @PostMapping
    public UnitDTO insert(@RequestBody Unit unit){
        return unitService.insert(unit);
    }
    @PutMapping("/{id}")
    public UnitDTO update(@PathVariable long id, @RequestBody Unit newUnit){
        return unitService.update(id, newUnit);
    }
    @DeleteMapping("/{id}")
    public UnitDTO delete(@PathVariable long id){
        return unitService.delete(id);
    }

}
