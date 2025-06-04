package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.form.UserRegistrationForm;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean signUp(UserRegistrationForm form);
}