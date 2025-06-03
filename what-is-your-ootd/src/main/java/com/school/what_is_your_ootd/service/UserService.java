package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.form.UserRegistrationForm;

public interface UserService {
    boolean signUp(UserRegistrationForm form);
}