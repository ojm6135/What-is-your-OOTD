package com.school.what_is_your_ootd.domain;

import com.school.what_is_your_ootd.dto.ClothingItemDto;
import com.school.what_is_your_ootd.vo.ClothesType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Clothes")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ClothingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "type", nullable = false)
    @Enumerated
    private ClothesType type;
    @Column(name = "detail", nullable = false)
    private String detail;
    @Column(name = "color", nullable = false)
    private String color;
    @Column(name = "for_spring", nullable = false)
    private boolean forSpring;
    @Column(name = "for_summer", nullable = false)
    private boolean forSummer;
    @Column(name = "for_fall", nullable = false)
    private boolean forFall;
    @Column(name = "for_winter", nullable = false)
    private boolean forWinter;

    public ClothingItem(Long userId, ClothingItemDto dto) {
        this.userId = userId;
        this.type = dto.getType();
        this.detail = dto.getDetail();
        this.color = dto.getColor();
        this.forSpring = dto.isForSpring();
        this.forSummer = dto.isForSummer();
        this.forFall = dto.isForFall();
        this.forWinter = dto.isForWinter();
    }
}
