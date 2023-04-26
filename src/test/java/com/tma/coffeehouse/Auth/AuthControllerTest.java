package com.tma.coffeehouse.Auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.coffeehouse.Order.Order;
import com.tma.coffeehouse.User.Gender;
import com.tma.coffeehouse.auth.AuthenticateRequest;
import com.tma.coffeehouse.auth.AuthenticateResponse;
import com.tma.coffeehouse.auth.RegisterRequest;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void registerNormally() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RegisterRequest(
                        "Trần Gia Huy", "tghuy", "123456789", "myemail@gmail.com",
                        null, Gender.MALE, "093123456"
                )));
        MvcResult result =  mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus() == 200).isTrue();
        String json = result.getResponse().getContentAsString();
        TypeReference<AuthenticateResponse> mapType = new TypeReference<>() {
        };
        AuthenticateResponse authenticateResponse = objectMapper.readValue(json, mapType);
        assertThat(!Objects.equals(authenticateResponse.getToken(), "")).isTrue();
        assertThat(authenticateResponse.getUserName()).isEqualTo("tghuy");
    }

    @Test
    public void registerWithUsernameTaken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // Register username
        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RegisterRequest(
                        "Trần Gia Huy", "tghuy", "123456789", "myemail@gmail.com",
                        null, Gender.MALE, "093123456"
                )));
        MvcResult result =  mockMvc.perform(request).andReturn();

        // Register again with same username

        RequestBuilder secondRequest = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RegisterRequest(
                        "Trần Gia Huy", "tghuy", "123456789", "myemail@gmail.com",
                        null, Gender.MALE, "093123456"
                )));
        MvcResult result2 =  mockMvc.perform(secondRequest).andReturn();
        assertThat(result2.getResponse().getStatus()).isEqualTo(409);
        String json2 = result2.getResponse().getContentAsString();
        TypeReference<Map<String, String>> mapType2 = new TypeReference<>() {
        };
        Map<String, String> error = objectMapper.readValue(json2, mapType2);
        assertThat(error.get("message").startsWith("Username")).isTrue();
    }
    @Test
    public void testSignInWithCorrectUserNameAndPassword() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register
        RequestBuilder secondRequest = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RegisterRequest(
                        "Trần Gia Huy", "tghuy", "123456789", "myemail@gmail.com",
                        null, Gender.MALE, "093123456"
                )));
        MvcResult result2 =  mockMvc.perform(secondRequest).andReturn();

        // Login
        TypeReference<AuthenticateResponse> responseType = new TypeReference<>() {
        };
        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType("application/json")
                .content(
                        objectMapper.writeValueAsString(new AuthenticateRequest("tghuy", "123456789"))
                );
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        AuthenticateResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), responseType);
        assertThat(!response.getToken().equals("")).isTrue();
        assertThat(response.getUserName().equals("tghuy")).isTrue();
    }

    @Test
    public void testLoginWithCorrectUsernameAndWrongPassword() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register
        RequestBuilder secondRequest = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RegisterRequest(
                        "Trần Gia Huy", "tghuy", "123456789", "myemail@gmail.com",
                        null, Gender.MALE, "093123456"
                )));
        MvcResult result2 =  mockMvc.perform(secondRequest).andReturn();

        // Login
        TypeReference<Map<String, String>> responseType = new TypeReference<>() {
        };
        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType("application/json")
                .content(
                        objectMapper.writeValueAsString(new AuthenticateRequest("tghuy", "12345678"))
                );
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(409);
        Map<String, String> response = objectMapper.readValue(result.getResponse().getContentAsString(), responseType);
        assertThat(!response.get("message").equals("")).isTrue();
    }

    @Test
    public void testLoginWithWrongUsername() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register
        RequestBuilder secondRequest = MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RegisterRequest(
                        "Trần Gia Huy", "tghuy", "123456789", "myemail@gmail.com",
                        null, Gender.MALE, "093123456"
                )));
        MvcResult result2 =  mockMvc.perform(secondRequest).andReturn();

        // Login
        TypeReference<Map<String, String>> responseType = new TypeReference<>() {
        };
        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType("application/json")
                .content(
                        objectMapper.writeValueAsString(new AuthenticateRequest("tghu", "123456789"))
                );
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(409);
        Map<String, String> response = objectMapper.readValue(result.getResponse().getContentAsString(), responseType);
        assertThat(!response.get("message").equals("")).isTrue();
    }
}
