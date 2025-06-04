package com.school.what_is_your_ootd.dto;

import com.school.what_is_your_ootd.domain.ClothingItem;
import com.school.what_is_your_ootd.vo.ClothesType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClothingItemDto {
    private Long id;
    private ClothesType type;
    private String detail;
    private String color;
    private boolean forSpring;
    private boolean forSummer;
    private boolean forFall;
    private boolean forWinter;

    public ClothingItemDto(ClothingItem entity) {
        this.id = entity.getId();
        this.type = entity.getType();
        this.detail = entity.getDetail();
        this.color = entity.getColor();
        this.forSpring = entity.isForSpring();
        this.forSummer = entity.isForSummer();
        this.forFall = entity.isForFall();
        this.forWinter = entity.isForWinter();
    }
}
