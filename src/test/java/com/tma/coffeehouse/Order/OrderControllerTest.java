package com.tma.coffeehouse.Order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.coffeehouse.User.Gender;
import com.tma.coffeehouse.User.Role;
import com.tma.coffeehouse.auth.AuthService;
import com.tma.coffeehouse.auth.AuthenticateResponse;
import com.tma.coffeehouse.auth.RegisterRequest;
import com.tma.coffeehouse.config.Serializer.PageModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.swing.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    private AuthenticateResponse auth;

    @BeforeEach
    public void setup(){
        AuthenticateResponse response = authService.register(new RegisterRequest(
                "Trần Gia Huy", "tghuy", "123456789", "myemail@gmail.com",
                null, Gender.MALE, "093123456"
        ));
        this.auth = response;
    }
    @Test
    void testGetAllOrder() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/user/order")
                .header("Authorization",
                        "Bearer " +
                                auth.getToken());
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new PageModule());
        TypeReference<Page<Order>> mapType = new TypeReference<>() {
        };
        Page<Order> result = objectMapper.readValue(json, mapType);
        assertThat(result.getContent()).isEmpty();
    }
//    void testUpdateOrder
}
