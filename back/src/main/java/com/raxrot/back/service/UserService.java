package com.raxrot.back.service;

import com.raxrot.back.dto.AuthResponse;
import com.raxrot.back.dto.UserRequest;
import com.raxrot.back.entity.User;

public interface UserService {
    AuthResponse register(UserRequest request);
    AuthResponse login(UserRequest request);
    User getCurrentLoggedUser();
}
