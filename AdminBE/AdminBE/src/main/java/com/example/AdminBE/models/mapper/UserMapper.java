package com.example.AdminBE.models.mapper;

import com.example.AdminBE.entities.User;
import com.example.AdminBE.models.dtos.UserDTO;

public class UserMapper {
    public static UserDTO toUserDTO(User user){
        UserDTO tmp = new UserDTO();
        tmp.setId(user.getId());
        tmp.setFullName(user.getFullName());
        tmp.setAddress(user.getAddress());
        tmp.setPhoneNumber(user.getPhoneNumber());
        tmp.setDateOfBirth(user.getDateOfBirth());
        return tmp;
    }

}
