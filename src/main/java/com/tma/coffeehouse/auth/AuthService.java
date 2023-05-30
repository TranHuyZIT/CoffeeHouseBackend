package com.tma.coffeehouse.auth;

import com.tma.coffeehouse.Customers.Customer;
import com.tma.coffeehouse.Customers.CustomerRepository;
import com.tma.coffeehouse.Customers.CustomerService;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.User.*;
import com.tma.coffeehouse.User.DTO.UserResponseDTO;
import com.tma.coffeehouse.Utils.CustomUtils;
import com.tma.coffeehouse.Utils.ImageService;
import com.tma.coffeehouse.Utils.MessageQueueUtils;
import com.tma.coffeehouse.config.JWTService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MessageQueueUtils queueUtils;
    private final ImageService imageService;
    private final CustomerRepository customerRepository;

    private final AuthenticationManager authenticationManager;
    @Transactional(rollbackOn = {Throwable.class, Exception.class})
    public AuthenticateResponse register(RegisterRequest request){
        String requestUsername = request.getUserName();
        Optional<User> existUser = userRepository.findByUserName(requestUsername);
        if (existUser.isPresent()) throw new CustomException("Username đã tồn tại", HttpStatus.CONFLICT);
        return this.createCustomer(request);
    }

    public AuthenticateResponse authenticate(AuthenticateRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(()->new CustomException("Tài khoản " + request.getUserName()+ " không tồn tại", HttpStatus.NOT_FOUND));
        String token = jwtService.signToken(user);
        return AuthenticateResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .gender(user.getGender())
                .phone(user.getPhone())
                .name(user.getName())
                .role(user.getRole())
                .userName(user.getUsername())
                .build();
    }

    public UserResponseDTO getIdentity(String token){
        String userName = jwtService.extractUserName(token);
        User user = userRepository.findByUserName(userName).
                orElseThrow(() -> new CustomException("Không tìm thấy tài khoản với username là " + userName , HttpStatus.NOT_FOUND));
        System.out.println(user);
        UserResponseDTO userResponseDTO = userMapper.modelTODTO(user);
        userResponseDTO.setToken(token);
        return userResponseDTO;
    }
    public AuthenticateResponse createCustomer(RegisterRequest request){
        User user = User.builder()
                .name("")
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .phone((request.getPhone()))
                .gender(request.getGender())
                .build();
        User newUser = userRepository.save(user);
        Customer customer = Customer.builder()
                .image("6476301a730277022059f8b5")
                .address("")
                .user(newUser)
                .build();
        customerRepository.save(customer);
        String token = jwtService.signToken(user);
        if (user.getEmail() != null){
            queueUtils.pushEmailMessageQueue("Cám Ơn Khách Hàng Đăng Ký Dịch Vụ The Coffee House!",
                    user.getEmail(),
                    String.format("Cảm ơn %s đã đăng ký dịch vụ của The Coffee House.\n" +
                                    "Từ đây bạn có thể đăng ký đặt hàng tại quán với tài khoản %s!. \n",
                            user.getName(), user.getEmail()));
        }
        return AuthenticateResponse.builder()
                .token(token)
                .id(newUser.getId())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .phone(newUser.getPhone())
                .name(newUser.getName())
                .role(newUser.getRole())
                .userName(newUser.getUsername())
                .build();
    }
    public AuthenticateResponse createCustomer(RegisterRequest request, String pictureURL){
        User user = User.builder()
                .name("")
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .phone((request.getPhone()))
                .gender(request.getGender())
                .build();
        User newUser = userRepository.save(user);
        String image = imageService.insertImage("customer", pictureURL, request.getUserName());
        Customer customer = Customer.builder()
                .image(image)
                .address("")
                .user(newUser)
                .build();
        customerRepository.save(customer);
        String token = jwtService.signToken(user);
        if (user.getEmail() != null){
            queueUtils.pushEmailMessageQueue("Cám Ơn Khách Hàng Đăng Ký Dịch Vụ The Coffee House!",
                    user.getEmail(),
                    String.format("Cảm ơn %s đã đăng ký dịch vụ của The Coffee House.\n" +
                                    "Từ đây bạn có thể đăng ký đặt hàng tại quán với tài khoản %s!. \n",
                            user.getName(), user.getEmail()));
        }
        return AuthenticateResponse.builder()
                .token(token)
                .id(newUser.getId())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .phone(newUser.getPhone())
                .name(newUser.getName())
                .role(newUser.getRole())
                .userName(newUser.getUsername())
                .build();
    }
}
