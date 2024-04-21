package com.example.AdminBE.controllers;

import com.example.AdminBE.components.JwtTokenUtil;
import com.example.AdminBE.models.dtos.UserDTO;
import com.example.AdminBE.models.dtos.UserLoginDTO;
import com.example.AdminBE.entities.User;
import com.example.AdminBE.services.IUserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200/login")
@Getter
@Setter
@RestController //xu li yeu cau cua http den cac endpoint /api/v1/user/register and /api/v1/user/login
@RequestMapping ("api/v1/users")
@RequiredArgsConstructor
public class    UserController {
    private final IUserService userService;//xu li logic lien quan den nguoi dung
    private final JwtTokenUtil jwtTokenUtil;//xu li token jwt
    public class RegisterResponse{
        private String message;
        private String token;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    //xu li yeu cau cua post /register, no nhan 1 UserDTO tu request body
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result){
            try{
                if(result.hasErrors()){
                    List<String> errorMessages = result.getFieldErrors().stream()
                            .map(FieldError::getDefaultMessage).toList();
                    return ResponseEntity.badRequest().body(errorMessages);
                }
                if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                    return ResponseEntity.badRequest().body("Password does not match");
                }
                User user = userService.createUser(userDTO);
                String token = jwtTokenUtil.generateToken(user);

                RegisterResponse response = new RegisterResponse();
                response.setMessage("Register Successfully");
                response.setToken(token);
                return ResponseEntity.ok(response);
            }catch (Exception ex){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            }
    }

    public class LoginResponse{
        private String message;
        private String token;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    //nhan 1 UserLoginDTO tu request body, roi su dung userService de dang nhap
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        //kiem tra thong tin dang nhap va luu token
        try{
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            LoginResponse response = new LoginResponse();
            response.setMessage("Login successfully");
            response.setToken(token);
            //tra ve token trong response
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @GetMapping("/listUser")
    public ResponseEntity<List<UserDTO>> getListUser() {
        List<UserDTO> userList = userService.getListUser();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/listUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        UserDTO result  = userService.getUserById(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Sử dụng phương thức xóa người dùng từ UserListService
        userService.deleteUser(id);
        // Tạo một đối tượng Map để chứa thông điệp
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        // Trả về đối tượng ResponseEntity chứa đối tượng Map
        return ResponseEntity.ok(response);
    }

}
