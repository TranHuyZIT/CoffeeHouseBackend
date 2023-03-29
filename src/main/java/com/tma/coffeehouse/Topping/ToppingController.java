package com.tma.coffeehouse.Topping;

import com.tma.coffeehouse.Topping.DTO.AddToppingDTO;
import com.tma.coffeehouse.Topping.DTO.ToppingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<Topping> findAll(@RequestParam(required = false, defaultValue = "0", name = "productId") Long productId ,
                                 @RequestParam(name="name", defaultValue = "") String name,
                                 @RequestParam(defaultValue = "0", name="pageNo", required = false) Integer pageNo,
                                 @RequestParam(defaultValue = "1000", name="pageSize", required= false ) Integer pageSize,
                                 @RequestParam(defaultValue = "createdAt", name="sortBy") String sortBy,
                                 @RequestParam(defaultValue = "true", name="reverse") boolean reverse ){
        return toppingService.findAll(productId, name, pageNo - 1, pageSize, sortBy, reverse);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToppingDTO findOne (@PathVariable Long id){
        return toppingService.findOne(id);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToppingDTO update(@PathVariable Long id, @RequestBody AddToppingDTO addToppingDTO){
        return toppingService.update(id, addToppingDTO);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToppingDTO delete(@PathVariable Long id){
        return toppingService.delete(id);
    }
}
