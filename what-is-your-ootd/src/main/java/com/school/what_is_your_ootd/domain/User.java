package com.school.what_is_your_ootd.domain;

import com.school.what_is_your_ootd.vo.Location;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "gender", nullable = false)
    private char gender;
    @Column(name = "location", nullable = false)
    @Embedded
    private Location location;

    public User(String username, String password, String email, char gender, Location location) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.location = location;
    }
}

