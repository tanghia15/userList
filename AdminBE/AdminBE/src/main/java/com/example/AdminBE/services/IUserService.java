package com.example.AdminBE.services;

import com.example.AdminBE.models.dtos.UserDTO;
import com.example.AdminBE.entities.User;
import com.example.AdminBE.exceptions.DataNotFoundException;

import java.util.List;

public interface IUserService {

    User createUser(UserDTO userDTO) throws DataNotFoundException;

    String login(String phoneNumber, String password) throws Exception;

    List<UserDTO> getListUser();

    UserDTO getUserById(Long id);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}
