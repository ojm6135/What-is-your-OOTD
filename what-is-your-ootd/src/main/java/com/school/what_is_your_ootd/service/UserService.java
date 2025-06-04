package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.dto.UserDto;
import com.school.what_is_your_ootd.form.UserRegistrationForm;
import com.school.what_is_your_ootd.vo.Location;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    boolean signUp(UserRegistrationForm form);
    Optional<UserDto> findByUsername(String username);
    boolean updateInfo(String username, Location location);
}