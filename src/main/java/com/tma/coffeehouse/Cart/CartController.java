package com.tma.coffeehouse.Cart;

import com.tma.coffeehouse.Cart.DTO.GetFullCartDTO;
import com.tma.coffeehouse.CartDetails.CartDetail;
import com.tma.coffeehouse.CartDetails.DTO.AddCartDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public GetFullCartDTO findone (@PathVariable Long customerId){
        return cartService.findOne(customerId);
    }
    @PostMapping("/detail")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDetail insertCartDetail(@RequestBody AddCartDetailDTO detail){
        return cartService.insertCartDetail(detail);
    }
    @DeleteMapping("/detail/{id}")
    public CartDetail deleteCartDetail(@PathVariable Long id){
        return cartService.deleteCartDetail(id);
    }
}
