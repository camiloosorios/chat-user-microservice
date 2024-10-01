package com.bosorio.app.user_microservice.service.impl;

import com.bosorio.app.user_microservice.DTO.CreateUserDTO;
import com.bosorio.app.user_microservice.DTO.LoginUserDTO;
import com.bosorio.app.user_microservice.DTO.UpdatePasswordDTO;
import com.bosorio.app.user_microservice.DTO.UserDTO;
import com.bosorio.app.user_microservice.Exception.BadRequestException;
import com.bosorio.app.user_microservice.Exception.InternalServerErrorException;
import com.bosorio.app.user_microservice.Exception.NotFoundException;
import com.bosorio.app.user_microservice.entity.User;
import com.bosorio.app.user_microservice.repository.IUserRepository;
import com.bosorio.app.user_microservice.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(IUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public void create(CreateUserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirmation())) {
            throw new BadRequestException("Passwords do not match");
        }
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = User.builder()
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(encodedPassword)
                .build();
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error saving user in database");
        }
    }

    @Override
    public String login(LoginUserDTO userDTO) {
        User user = userRepository.findUserByEmail(userDTO.getEmail())
                .orElseThrow(() -> new BadRequestException("Email or password are incorrect"));
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException("Email or password are incorrect");
        }
        return jwtService.generateToken(user.getId().toString());
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        users.forEach(user -> {
            usersDTO.add(UserDTO.builder()
                   .id(user.getId())
                   .name(user.getName())
                   .username(user.getUsername())
                   .email(user.getEmail())
                   .build());
        });
        return usersDTO;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User does not exist"));
        return UserDTO.builder()
                .id(id)
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public void update(CreateUserDTO userDTO, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirmation())) {
            throw new BadRequestException("Passwords do not match");
        }
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error updating user in database");
        }
    }

    @Override
    public void updatePassword(UpdatePasswordDTO userDTO, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        if (!passwordEncoder.matches(userDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        String encodedPassword = passwordEncoder.encode(userDTO.getNewPassword());
        user.setPassword(encodedPassword);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error updating user password in database");
        }
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error deleting user in database");
        }
    }
}
