package com.tma.coffeehouse.Cart;

import com.tma.coffeehouse.Cart.DTO.CheckOutInfoDTO;
import com.tma.coffeehouse.Cart.DTO.GetFullCartDTO;
import com.tma.coffeehouse.CartDetails.CartDetail;
import com.tma.coffeehouse.CartDetails.CartDetailRepository;
import com.tma.coffeehouse.CartDetails.DTO.DetailOfCartDTO;
import com.tma.coffeehouse.CartDetails.Mapper.DetailOfCartMapper;
import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Customers.CustomerRepository;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.Order.OrderStatus;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.Product.ProductRepository;
import com.tma.coffeehouse.Topping.Topping;
import com.tma.coffeehouse.Topping.ToppingRepository;
import com.tma.coffeehouse.Unit.Unit;
import com.tma.coffeehouse.Unit.UnitRepository;
import com.tma.coffeehouse.User.Gender;
import com.tma.coffeehouse.User.Role;
import com.tma.coffeehouse.User.User;
import com.tma.coffeehouse.User.UserRepository;
import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.Voucher.Voucher;
import com.tma.coffeehouse.Voucher.VoucherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartServiceTest {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    CartDetailRepository cartDetailRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ToppingRepository toppingRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    CartService underTest;
    @Autowired
    DetailOfCartMapper detailOfCartMapper;
    @Autowired
    VoucherRepository voucherRepository;
    private User user;
    private Customer customer;
    private Topping topping1;
    private Topping topping2;
    private Topping topping3;
    private Unit unit1;
    private Unit unit2;
    private Product product1En;
    private Product product2En;
    private Product product3En;
    private Product product4En;
    private Cart newCart;
    private CartDetail cartDetail1;
    private CartDetail cartDetail2;
    private GetFullCartDTO getFullCartDTO;


    @BeforeEach
    public void setup(){
        // CREATE USER
         user = userRepository.save(new User(null, "Nguyễn Văn A", "nguyenvana", "123456789",
                "", Gender.MALE, "", Role.ADMIN, null, null ));
         customer = customerRepository.save(new Customer(null,"","", user, null, null));

        // CREATE TOPPING
         topping1 = toppingRepository.save(
                new Topping(null, new HashSet<>(), "", 10000, null, null)
        );
         topping2 = toppingRepository.save(
                new Topping(null, new HashSet<>(), "", 15000, null, null)
        );
         topping3 = toppingRepository.save(
                new Topping(null, new HashSet<>(), "", 20000, null, null)
        );
        // CREATE UNIT
         unit1 = unitRepository.save(
                new Unit(null, 10000L, "")
        );
         unit2 = unitRepository.save(
                new Unit(null, 20000L, "")
        );

        // CREATE PRODUCT
        Product.ProductBuilder productBuilder = Product.builder();
         product1En = productRepository.save(productBuilder.productCategory(null)
                .name("Cà phê").productToppings(new HashSet<>(Arrays.asList(topping1, topping2))).image("").description("").price(20000).build());
         product2En = productRepository.save(productBuilder.productCategory(null)
                .name("Trà ô long").productToppings(new HashSet<>(List.of((topping1)))).image("").description("").price(25000).build());
         product3En = productRepository.save(productBuilder.productCategory(null)
                .name("Sữa chua").productToppings(new HashSet<>(List.of(topping3))).image("").description("").price(30000).build());
         product4En = productRepository.save(productBuilder.productCategory(null)
                .name("Cà phê sữa").productToppings(new HashSet<>()).image("").description("").price(15000).build());

        // CREATE CART
         newCart = cartRepository.save(new Cart(null, "", customer, null,null, null));
         cartDetail1 = cartDetailRepository.save(
                new CartDetail(null, product1En, newCart, unit1, new HashSet<>(Arrays.asList(topping1, topping2)), "",
                        1, 0L)
        );
         cartDetail2 = cartDetailRepository.save(
                new CartDetail(null, product2En, newCart, unit1, new HashSet<>(Collections.singletonList(topping1)), "",
                        1, 0L)
        );
         Set<DetailOfCartDTO> detailOfCartDTOS = detailOfCartMapper.modelsTODTOS(
                 new HashSet<>(Arrays.asList(cartDetail1, cartDetail2))
         );
         getFullCartDTO =  new GetFullCartDTO(newCart.getId(), "", customer, 0L, 0L, null, null, null, detailOfCartDTOS);
    }
    @Test
    void testLoadedDatabase(){
         List<Cart> carts = cartRepository.findAll();
         assertThat(carts).isNotEmpty();
    }
    @Test
    void testLoadedDatabase2(){
        List<Cart> carts = cartRepository.findAll();
        List<CartDetail> details = cartDetailRepository.findAll();
        List<Product> products = productRepository.findAll();
        System.out.println(products);
        assertThat(carts).hasSize(1);
        assertThat(details).hasSize(2);
        assertThat(products).hasSize(4);

    }
    @Test
    void calculateCartTotalShouldCalculateCorrectlyWithoutVoucher_Test1(){
        Long[] result = underTest.calculateCartTotal(getFullCartDTO);

        assertThat(result[0]).isEqualTo(100000L);
        assertThat(result[1]).isEqualTo(0);
    }
    @Test
    void calculateCartTotalShouldCalculateCorrectlyWithoutVoucher_Test2(){
        CartDetail cartDetailNew = cartDetailRepository.save(
                new CartDetail(null, product2En, newCart, unit1, new HashSet<>(), "",
                        1, 0L)
        );
        getFullCartDTO.getDetails().add(detailOfCartMapper.modelTODto(cartDetailNew));
        Long[] result = underTest.calculateCartTotal(getFullCartDTO);

        assertThat(result[0]).isEqualTo(135000L);
        assertThat(result[1]).isEqualTo(0);
    }
    @Test
    void calculateCartTotalShouldCalculateCorrectlyWithoutVoucher_Test3(){
        CartDetail cartDetailNew = cartDetailRepository.save(
                new CartDetail(null, product2En, newCart, unit1, new HashSet<>(), "",
                        1, 0L)
        );
        CartDetail cartDetailNew2 = cartDetailRepository.save(
                new CartDetail(null, product3En, newCart, unit1, new HashSet<>(), "",
                        2, 0L)
        );
        getFullCartDTO.getDetails().add(detailOfCartMapper.modelTODto(cartDetailNew));
        getFullCartDTO.getDetails().add(detailOfCartMapper.modelTODto(cartDetailNew2));
        Long[] result = underTest.calculateCartTotal(getFullCartDTO);

        assertThat(result[0]).isEqualTo(215000L);
        assertThat(result[1]).isEqualTo(0);
    }
    @Test
    void calculateCartTotalShouldCalculateCorrectlyWithoutVoucher_Test4(){
        CartDetail cartDetailNew = cartDetailRepository.save(
                new CartDetail(null, product2En, newCart, unit1, new HashSet<>(), "",
                        1, 0L)
        );
        CartDetail cartDetailNew2 = cartDetailRepository.save(
                new CartDetail(null, product3En, newCart, unit1, new HashSet<>(Arrays.asList(topping1, topping2)), "",
                        2, 0L)
        );
        getFullCartDTO.getDetails().add(detailOfCartMapper.modelTODto(cartDetailNew));
        getFullCartDTO.getDetails().add(detailOfCartMapper.modelTODto(cartDetailNew2));
        Long[] result = underTest.calculateCartTotal(getFullCartDTO);

        assertThat(result[0]).isEqualTo(265000L);
        assertThat(result[1]).isEqualTo(0);
    }
    @Test
    void calculateCartTotalShouldCalculateTotalWithoutVoucherWhenVoucherInvalid_Test1(){
        Voucher voucher = voucherRepository.save(
                new Voucher(null, (float)0.5, 0, 0, new Date(), "", new Date(), 15000L, 0L,
                        new HashSet<>(Arrays.asList(product1En, product2En)),null, null )
        );
        getFullCartDTO.setVoucher(voucher);
        Long [] result = underTest.calculateCartTotal(getFullCartDTO);
        assertThat(result[0]).isEqualTo(100000L);
        assertThat(result[1]).isEqualTo(0);
    }
    @Test
    void calculateCartTotalShouldCalculateCorrectlyWithVoucher_Test2(){
        Voucher voucher = voucherRepository.save(
                new Voucher(null, (float)0.5, 10, 5,
                        CustomUtils.convertStringToDate("10-04-2023"), "",
                        CustomUtils.convertStringToDate("30-12-2023"),
                        15000L, 0L,
                        new HashSet<>(Arrays.asList(product3En)),null, null )
        );
        getFullCartDTO.setVoucher(voucher);
       Long [] result = underTest.calculateCartTotal(getFullCartDTO);
        assertThat(result[0]).isEqualTo(100000L);
        assertThat(result[1]).isEqualTo(0L);
    }
    @Test
    void calculateCartTotalShouldCalculateCorrectlyWithVoucherMaxDisCount_Test3(){
        Voucher voucher = voucherRepository.save(
                new Voucher(null, (float)0.5, 10, 5,
                        CustomUtils.convertStringToDate("10-04-2023"), "",
                        CustomUtils.convertStringToDate("30-12-2023"),
                        15000L, 0L,
                        new HashSet<>(Arrays.asList(product1En)),null, null )
        );
        getFullCartDTO.setVoucher(voucher);
        Long [] result = underTest.calculateCartTotal(getFullCartDTO);
        assertThat(result[0]).isEqualTo(85000L);
        assertThat(result[1]).isEqualTo(15000L);
    }
    @Test
    void calculateCartTotalShouldCalculateCorrectlyWithVoucher_Test4(){
        Voucher voucher = voucherRepository.save(
                new Voucher(null, (float)0.5, 10, 5,
                        CustomUtils.convertStringToDate("10-04-2023"), "",
                        CustomUtils.convertStringToDate("30-12-2023"),
                        50000L, 0L,
                        new HashSet<>(Arrays.asList(product1En)),null, null )
        );
        getFullCartDTO.setVoucher(voucher);
        Long [] result = underTest.calculateCartTotal(getFullCartDTO);
        assertThat(result[0]).isEqualTo(100000L - 55000L / 2);
        assertThat(result[1]).isEqualTo(55000L / 2);
    }
    @Test
    void checkoutCartShouldReturnNewOrder(){
        CheckOutInfoDTO checkOutInfoDTO = CheckOutInfoDTO.builder()
                .address("").note("").deliveryTime(CustomUtils.convertStringToDate("19-04-2023"))
                .voucherId(null).build();
        Order newOrder = underTest.checkOutCart(customer.getId(),checkOutInfoDTO);
        assertThat(newOrder).hasFieldOrPropertyWithValue("tongtien", 100000L);
        assertThat(newOrder).hasFieldOrPropertyWithValue("tongsl", 2);
        assertThat(newOrder).hasFieldOrPropertyWithValue("status", OrderStatus.RECEIVED);
    }
    @Test
    void checkoutCartWithVoucherShouldReturnNewOrderWithVoucher(){
        Voucher voucher = voucherRepository.save(
                new Voucher(null, (float)0.5, 10, 5,
                        CustomUtils.convertStringToDate("10-04-2023"), "",
                        CustomUtils.convertStringToDate("30-12-2023"),
                        15000L, 0L,
                        new HashSet<>(Arrays.asList(product1En)),null, null )
        );
        CheckOutInfoDTO checkOutInfoDTO = CheckOutInfoDTO.builder()
                .address("").note("").deliveryTime(CustomUtils.convertStringToDate("19-04-2023"))
                .voucherId(voucher.getId()).build();
        Order newOrder = underTest.checkOutCart(customer.getId(),checkOutInfoDTO);
        Optional<Voucher> voucherAfterCheckout = voucherRepository.findById(voucher.getId());
        assertThat(newOrder).hasFieldOrPropertyWithValue("tongtien", 85000L);
        assertThat(newOrder).hasFieldOrPropertyWithValue("tongsl", 2);
        assertThat(newOrder).hasFieldOrPropertyWithValue("status", OrderStatus.RECEIVED);
        assertThat(newOrder).hasFieldOrPropertyWithValue("customer", customer);
        assertThat(newOrder).hasFieldOrPropertyWithValue("voucher", voucher);
        assertThat(voucherAfterCheckout.get().getRemainingNumber()).isEqualTo(4);
    }
}