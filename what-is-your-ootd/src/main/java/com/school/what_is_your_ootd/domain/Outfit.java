package com.school.what_is_your_ootd.domain;

import com.school.what_is_your_ootd.dto.OutfitDto;
import com.school.what_is_your_ootd.vo.ClothingItemSnapshot;
import com.school.what_is_your_ootd.vo.Style;
import com.school.what_is_your_ootd.vo.Weather;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Outfits")
@Entity
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Embedded
    @ElementCollection
    private List<ClothingItemSnapshot> clothes = new ArrayList<>();
    @Column(name = "weather", nullable = false)
    @Embedded
    private Weather weather;
    @Column(name = "style", nullable = false)
    @Enumerated
    private Style style;
    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    public Outfit(Long userId, OutfitDto dto) {
        this.userId = userId;
        this.clothes = dto.getClothes();
        this.weather = dto.getWeather();
        this.style = dto.getStyle();
        this.isPublic = dto.isPublic();
    }
}
