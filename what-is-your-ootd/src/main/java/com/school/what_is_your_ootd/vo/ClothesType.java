package com.school.what_is_your_ootd.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum ClothesType {
    OUTER("외투"),
    TOP("상의"),
    BOTTOM("하의"),
    SHOES("신발");
    
    private String desc;

    ClothesType(String desc) {
        this.desc = desc;
    }
}
