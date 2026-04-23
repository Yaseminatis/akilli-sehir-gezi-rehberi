package com.akillisehir.gezirehberi.service.concretes;

import com.akillisehir.gezirehberi.entity.User;
import com.akillisehir.gezirehberi.repository.UserRepository;
import com.akillisehir.gezirehberi.service.abstracts.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}