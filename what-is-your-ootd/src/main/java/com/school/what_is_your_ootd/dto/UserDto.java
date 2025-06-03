package com.school.what_is_your_ootd.dto;

import com.school.what_is_your_ootd.domain.User;
import com.school.what_is_your_ootd.vo.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private char gender;
    private Location location;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.location = user.getLocation();
    }
}