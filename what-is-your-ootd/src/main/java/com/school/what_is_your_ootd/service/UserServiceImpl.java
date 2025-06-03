package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.domain.User;
import com.school.what_is_your_ootd.form.UserRegistrationForm;
import com.school.what_is_your_ootd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean signUp(UserRegistrationForm form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new NoSuchElementException();
        }

        if (userRepository.existsByEmail(form.getEmail())) {
            throw new NoSuchElementException();
        }

        User user = new User(
                form.getUsername(),
                passwordEncoder.encode(form.getPassword()),
                form.getEmail(),
                form.getGender(),
                form.getLocation()
        );

        return userRepository.save(user)
                .getUsername()
                .equals(user.getUsername());
    }
}
