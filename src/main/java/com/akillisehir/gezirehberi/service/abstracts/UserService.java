package com.akillisehir.gezirehberi.service.abstracts;

import com.akillisehir.gezirehberi.entity.User;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String email);
}