package com.raxrot.back.service.impl;

import com.raxrot.back.dto.AuthResponse;
import com.raxrot.back.dto.UserRequest;
import com.raxrot.back.dto.UserResponse;
import com.raxrot.back.entity.User;
import com.raxrot.back.enums.Role;
import com.raxrot.back.exception.ApiException;
import com.raxrot.back.repository.UserRepository;
import com.raxrot.back.security.JwtUtils;
import com.raxrot.back.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtUtils jwtUtils, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthResponse register(UserRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            throw new ApiException("Username is already in use");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.USER);

        User savedUser = userRepository.save(newUser);

        String token = jwtUtils.generateToken(savedUser.getUsername());
        UserResponse userResponse = modelMapper.map(savedUser, UserResponse.class);

        AuthResponse authResponse = new AuthResponse(token, userResponse);
        return authResponse;
    }

    @Override
    public AuthResponse login(UserRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Wrong password");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        AuthResponse authResponse = new AuthResponse(token, userResponse);
        return authResponse;
    }

    @Override
    public User getCurrentLoggedUser() {
       String username= SecurityContextHolder.getContext().getAuthentication().getName();
       Optional<User> user = userRepository.findByUsername(username);
       if (user.isPresent()) {
           return user.get();
       }
       throw new ApiException("User not found");
    }
}
