package com.tma.coffeehouse.Topping;

import com.tma.coffeehouse.Topping.DTO.AddToppingDTO;
import com.tma.coffeehouse.Topping.DTO.ToppingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/topping")
@RequiredArgsConstructor
public class ToppingController {
    private final ToppingService toppingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ToppingDTO insert(@RequestBody AddToppingDTO addToppingDTO) {
        return toppingService.insert(addToppingDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToppingDTO> findAll(@RequestParam(required = false, defaultValue = "0") Long productID){
        return toppingService.findAll(productID);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToppingDTO findOne (@PathVariable Long id){
        return toppingService.findOne(id);
    }
}
