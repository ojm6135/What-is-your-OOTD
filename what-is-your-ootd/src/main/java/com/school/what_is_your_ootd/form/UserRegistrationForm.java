package com.school.what_is_your_ootd.form;

import com.school.what_is_your_ootd.vo.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationForm {
    private String username;
    private String password;
    private String email;
    private char gender;
    private Location location;
}
