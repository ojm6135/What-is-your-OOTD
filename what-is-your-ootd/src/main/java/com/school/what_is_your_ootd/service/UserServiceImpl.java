package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.domain.User;
import com.school.what_is_your_ootd.dto.UserDto;
import com.school.what_is_your_ootd.form.UserRegistrationForm;
import com.school.what_is_your_ootd.repository.UserRepository;
import com.school.what_is_your_ootd.security.CustomUserDetails;
import com.school.what_is_your_ootd.vo.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
            return false;
        }

        if (userRepository.existsByEmail(form.getEmail())) {
            return false;
        }

        if (!Location.validateRange(form.getLocation())) {
            return false;
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

    @PreAuthorize("#username == authentication.name")
    @Override
    public Optional<UserDto> findByUsername(@P("username") String username) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = principal.getUserId();
        Optional<User> user = userRepository.findById(userId);

        return user.map(UserDto::new);
    }

    @PreAuthorize("#username == authentication.name")
    @Override
    public boolean updateInfo(@P("username") String username, Location location) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = principal.getUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }

        if (!Location.validateRange(location)) {
            return false;
        }

        User user = userOptional.get();
        user.setLocation(location);
        userRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        return new CustomUserDetails(user);
    }
}
