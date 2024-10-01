package com.bosorio.app.user_microservice.service;

import com.bosorio.app.user_microservice.DTO.CreateUserDTO;
import com.bosorio.app.user_microservice.DTO.LoginUserDTO;
import com.bosorio.app.user_microservice.DTO.UpdatePasswordDTO;
import com.bosorio.app.user_microservice.DTO.UserDTO;

import java.util.List;

public interface IUserService {

    void create(CreateUserDTO userDTO);
    String login(LoginUserDTO userDTO);
    List<UserDTO> getUsers();
    UserDTO getUserById(Long id);
    void update(CreateUserDTO createUserDTO, Long id);
    void updatePassword(UpdatePasswordDTO userDTO, Long id);
    void delete(Long id);

}
