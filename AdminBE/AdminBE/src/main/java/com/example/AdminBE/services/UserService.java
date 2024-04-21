package com.example.AdminBE.services;

import com.example.AdminBE.components.JwtTokenUtil;
import com.example.AdminBE.models.dtos.UserDTO;
import com.example.AdminBE.entities.Role;
import com.example.AdminBE.entities.User;
import com.example.AdminBE.exceptions.DataNotFoundException;
import com.example.AdminBE.models.mapper.UserMapper;
import com.example.AdminBE.repositories.RoleRepository;


import com.example.AdminBE.repositories.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Builder
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        //register_user
        String phoneNumber = userDTO.getPhoneNumber();
        //kiem tra sdt
        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        //convert userDTO --> user
        User newUser = User.builder().fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .password(userDTO.getPassword())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId()).build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        //kiem tra neu co accountId, khong yeu cau password
        if(userDTO.getFacebookAccountId()==0&userDTO.getGoogleAccountId()==0){
            String password =userDTO.getPassword();
            String encoderPassword = passwordEncoder.encode(password);
            newUser.setPassword(encoderPassword);
        }
        return userRepository.save(newUser);
    }
    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optionalUser= userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phone number/password");
        }

        //return optionalUser.get();//muon tra Jwt-token?
        User existingUser = optionalUser.get();
        //check password
        if(existingUser.getFacebookAccountId()==0&existingUser.getGoogleAccountId()==0){
            if(!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number/password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(
                        phoneNumber,password,
                        existingUser.getAuthorities());
        //authenticate with Java Spring Security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public List<UserDTO> getListUser() {
        List<UserDTO> result = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            result.add(UserMapper.toUserDTO(user));
        }
        return result;
    }
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return UserMapper.toUserDTO(user);
        }
        return null;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            // Cập nhật thông tin người dùng
            existingUser.setFullName(userDTO.getFullName());
            existingUser.setAddress(userDTO.getAddress());
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
            // Cập nhật các trường khác của người dùng nếu cần

            // Gọi phương thức save() của UserRepository để cập nhật thông tin người dùng
            userRepository.save(existingUser);

            return UserMapper.toUserDTO(existingUser);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
