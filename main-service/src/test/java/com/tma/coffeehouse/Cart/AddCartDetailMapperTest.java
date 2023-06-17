package com.tma.coffeehouse.Cart;

import com.tma.coffeehouse.Cart.Cart;
import com.tma.coffeehouse.Cart.CartRepository;
import com.tma.coffeehouse.CartDetails.CartDetail;
import com.tma.coffeehouse.CartDetails.DTO.AddCartDetailDTO;
import com.tma.coffeehouse.CartDetails.Mapper.AddCartDetailMapper;
import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Customers.CustomerRepository;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Product.ProductRepository;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Topping.ToppingRepository;
import com.tma.coffeehouse.Unit.Unit;
import com.tma.coffeehouse.Unit.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddCartDetailMapperTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UnitRepository unitRepository;
    @Mock
    private ToppingRepository toppingRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CustomerRepository customerRepository;

    private AddCartDetailMapper underTest;
    @BeforeEach
    void setUp() {
        underTest = new AddCartDetailMapper(productRepository, unitRepository, toppingRepository, cartRepository, customerRepository);
    }

    @Test
    void canConvertAddCartDetailDtoToCartDetail() {
        // Given
        AddCartDetailDTO.AddCartDetailDTOBuilder addCartDetailDTO = AddCartDetailDTO.builder();
        addCartDetailDTO.note("");
        addCartDetailDTO.soluong(1);
        addCartDetailDTO.customerId(3L);
        addCartDetailDTO.productId(27L);
        addCartDetailDTO.toppingIds(new Long[]{1L, 2L});
        addCartDetailDTO.unitId(1L);
        // When
        // Define repo behaviours
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(new Product()));
        when(unitRepository.findById(any(Long.class))).thenReturn(Optional.of(new Unit()));
        when(cartRepository.findByCustomerId(any(Long.class))).thenReturn(new Cart());
        when(toppingRepository.findById(any(Long.class))).thenReturn(Optional.of(new Topping()));

        CartDetail cartDetail = underTest.dtoTOModel(addCartDetailDTO.build());
        // Then
        assertThat(cartDetail.getNote()).isNotEqualTo(null);
        assertThat(cartDetail.getNote()).isNotEqualTo(null);
        assertThat(cartDetail.getProduct()).isNotEqualTo(null);
        assertThat(cartDetail.getUnit()).isNotEqualTo(null);
        assertThat(cartDetail.getToppings()).isNotEmpty();
    }
}