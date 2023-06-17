package com.tma.coffeehouse.Order;

import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Customers.CustomerRepository;
import com.tma.coffeehouse.Order.DTO.FullOrderDTO;
import com.tma.coffeehouse.Order.Mapper.FullOrderMapper;
import com.tma.coffeehouse.OrderDetails.DTO.AddOrderDetailDTO;
import com.tma.coffeehouse.OrderDetails.OrderDetailRepository;
import com.tma.coffeehouse.OrderDetails.OrderDetailService;
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
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FullOrderMapperTest {
    @Autowired
    FullOrderMapper underTest;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ToppingRepository toppingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    OrderDetailService orderDetailService;
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
    @BeforeEach
    public void setup(){
        // Create User
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
    }
    @Test
    void loadedDatabase (){
        assertThat(productRepository.findAll()).hasSize(4);
        assertThat(unitRepository.findAll()).hasSize(2);
        assertThat(toppingRepository.findAll()).hasSize(3);
        assertThat(userRepository.findAll()).hasSize(1);
        assertThat(customerRepository.findAll()).hasSize(1);
    }
    @Test
    void shouldMapFromAnOrderToFullOrderWithDetail(){
        Order order = orderRepository.save(
                new Order(null, "", CustomUtils.convertStringToDate("20-4-2023"), "", null, OrderStatus.RECEIVED
                        ,100000L, 2, customer, null, null)
        );
        orderDetailService.insert(new AddOrderDetailDTO(product1En.getId(), unit1.getId(), 1,
                new Long[]{topping1.getId(), topping2.getId()}, order.getId()));
        orderDetailService.insert(new AddOrderDetailDTO(product2En.getId(), unit1.getId(), 1,
                new Long[]{topping1.getId()}, order.getId()));
        FullOrderDTO fullOrderDTO = underTest.modelTODto(order);
        assertThat(fullOrderDTO.getDetails()).anyMatch(
                detail -> detail.getProduct().equals(product1En) &&
                        detail.getToppings().containsAll(Arrays.asList(topping1, topping2))
        ).anyMatch(
                detail -> detail.getProduct().equals(product2En) && detail.getToppings().contains(topping1)
        );
    }

}
