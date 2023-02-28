package com.tma.coffeehouse.Topping;

import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Product.ProductRepository;
import com.tma.coffeehouse.Topping.DTO.AddToppingDTO;
import com.tma.coffeehouse.Topping.DTO.ToppingDTO;
import com.tma.coffeehouse.Topping.Mapper.AddToppingMapper;
import com.tma.coffeehouse.Topping.Mapper.ToppingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ToppingService {
    private final ToppingRepository toppingRepository;
    private final ProductRepository productRepository;
    private final ToppingMapper toppingMapper;
    private final AddToppingMapper addToppingMapper;
    List<ToppingDTO> findAll (Long productID){
        if (productID != 0){
            List<Topping> toppings =  toppingRepository.findByToppingOfProduct_Id(productID);
            return toppingMapper.modelsToDTOS(toppings);
        }else{
            List<Topping> toppings = toppingRepository.findAll();
            return toppingMapper.modelsToDTOS(toppings);
        }
    }
    List<Topping> insertMany (List<Topping> toppings){
        return toppingRepository.saveAll(toppings);
    }
    @Transactional(rollbackOn = {CustomException.class, Throwable.class, Exception.class})
    ToppingDTO insert(AddToppingDTO addToppingDTO){
        Topping topping = addToppingMapper.dtoTOModel(addToppingDTO);
        Topping saved =  toppingRepository.save(topping);
        Set<Product> products = new HashSet<>(topping.getToppingOfProduct());
        for(Product product:products ){
            product.addTopping(saved);
            productRepository.save(product);
        }
        return toppingMapper.modelTODto(saved);
    }
    public Set<Topping>  findManyByIDs (Long[] toppingsId){
        Set<Topping> toppings = new HashSet<>();
        for(long id: toppingsId){
            Topping topping = toppingRepository.findById(id)
                    .orElseThrow(()->new CustomException("Không tìm thấy topping có mã " + id, HttpStatus.NOT_FOUND));
           toppings.add(topping);
        }
        return toppings;
    }

    ToppingDTO findOne(Long id) {
        return toppingMapper.modelTODto(
            toppingRepository.findById(id).orElseThrow(() -> new CustomException("Không tìm thấy topping với mã " + id, HttpStatus.NOT_FOUND))
        );
    }
}
