package com.tma.User;

import com.tma.User.DTO.UserListResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("admin/account")
    public Page<UserListResponseDTO> getAllUsers(
            @RequestParam(name="name", defaultValue = "") String name,
            @RequestParam(defaultValue = "0", name="pageNo") Integer pageNo,
            @RequestParam(defaultValue = "10", name="pageSize") Integer pageSize,
            @RequestParam(defaultValue = "createdAt", name="sortBy") String sortBy,
            @RequestParam(defaultValue = "true", name="reverse") boolean reverse
    ){

        return userService.getAll(name,pageNo - 1, pageSize, sortBy, reverse);
    }
    @PutMapping("user/account/{id}")
    public UserListResponseDTO update(@PathVariable Long id, @RequestBody User newUser){
        return userService.update(id, newUser);
    }
    @DeleteMapping("admin/account/{id}")
    public UserListResponseDTO delete(@PathVariable Long id){
        return userService.delete(id);
    }
}
