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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ToppingService {
    private final ToppingRepository toppingRepository;
    private final ProductRepository productRepository;
    private final ToppingMapper toppingMapper;
    private final AddToppingMapper addToppingMapper;
    List<ToppingDTO> findAll(){
        return toppingMapper.modelsToDTOS(toppingRepository.findAll());
    }
    Page<Topping> findAll (Long productID, String name, Integer pageNo, Integer pageSize, String sortBy, Boolean reverse){
        // Pagination nad Queries
        if (pageNo == -1) return toppingRepository.findAll(
                PageRequest.of(0, Integer.MAX_VALUE,
                        Sort.by(reverse? Sort.Direction.DESC : Sort.Direction. ASC, sortBy))
        );
        Pageable pageRequest = PageRequest.of(pageNo, pageSize,
                Sort.by(reverse? Sort.Direction.DESC : Sort.Direction. ASC, sortBy));
        if (productID == 0 && Objects.equals(name, "")){
            return toppingRepository.findAll(pageRequest);
        }else if (Objects.equals(name, "")){
            return toppingRepository.findByToppingOfProduct_Id(productID, pageRequest);
        }
        else{
            return toppingRepository.findByNameContaining(name, pageRequest);
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
    public ToppingDTO update(Long id, AddToppingDTO newTopping){
        Topping current = toppingRepository.findById(id).orElseThrow(() -> new CustomException("Không tìm thấy topping với mã " + id, HttpStatus.NOT_FOUND));
        current.setName(newTopping.getName());
        current.setPrice(newTopping.getPrice());
        return toppingMapper.modelTODto(toppingRepository.save(current));
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    public ToppingDTO delete(Long id){
        Topping current = toppingRepository.findById(id).orElseThrow(() -> new CustomException("Không tìm thấy topping với mã " + id, HttpStatus.NOT_FOUND));
        for(Product product: current.getToppingOfProduct()){
            product.removeTopping(current);
            productRepository.save(product);
        }
        toppingRepository.delete(current);
        return toppingMapper.modelTODto(current);
    }

    ToppingDTO findOne(Long id) {
        return toppingMapper.modelTODto(
            toppingRepository.findById(id).orElseThrow(() -> new CustomException("Không tìm thấy topping với mã " + id, HttpStatus.NOT_FOUND))
        );
    }
}
