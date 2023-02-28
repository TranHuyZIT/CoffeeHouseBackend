package com.tma.coffeehouse.CartDetails.Mapper;

import com.tma.coffeehouse.CartDetails.CartDetail;
import com.tma.coffeehouse.CartDetails.DTO.AddCartDetailDTO;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Product.ProductRepository;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Topping.ToppingRepository;
import com.tma.coffeehouse.Unit.Unit;
import com.tma.coffeehouse.Unit.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AddCartDetailMapper {
     private final ProductRepository productRepository;
     private final UnitRepository unitRepository;
     private final ToppingRepository toppingRepository;
     public CartDetail dtoTOModel(AddCartDetailDTO addCartDTO) {
          if ( addCartDTO == null ) {
               return null;
          }

          CartDetail.CartDetailBuilder cartDetail = CartDetail.builder();
          cartDetail.soluong(addCartDTO.getSoluong());
          cartDetail.note(addCartDTO.getNote());
          Product product = productRepository.findById(addCartDTO.getProductId())
                  .orElseThrow(()->new CustomException("Không tìm thấy sản phẩm với mã " + addCartDTO.getProductId(), HttpStatus.NOT_FOUND));
          Unit unit = unitRepository.findById(addCartDTO.getUnitId())
                  .orElseThrow(()->new CustomException("Không tìm thấy đơn vị có mã là " + addCartDTO.getUnitId(), HttpStatus.NOT_FOUND));
          Set<Topping> toppings = new HashSet<>();
          for(Long toppingId: addCartDTO.getToppingIds()){
               Topping topping = toppingRepository.findById(toppingId).orElseThrow(() -> new CustomException("Không tìm thấy topping có mã" + toppingId, HttpStatus.NOT_FOUND));
               toppings.add(topping);
          }
          cartDetail.unit(unit);
          cartDetail.product(product);
          cartDetail.toppings(toppings);

          return cartDetail.build();
     }
}
