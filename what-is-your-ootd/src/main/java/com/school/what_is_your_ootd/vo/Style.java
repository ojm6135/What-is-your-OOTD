package com.school.what_is_your_ootd.vo;

import lombok.Getter;

@Getter
public enum Style {
    CASUAL("캐주얼"),
    FORMAL("포멀"),
    SPORTY("스포츠"),
    STREET("스트릿");

    private String desc;

    Style(String desc) {
        this.desc = desc;
    }
}
