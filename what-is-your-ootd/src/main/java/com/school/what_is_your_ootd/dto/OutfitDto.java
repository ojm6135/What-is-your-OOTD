package com.school.what_is_your_ootd.dto;

import com.school.what_is_your_ootd.domain.Outfit;
import com.school.what_is_your_ootd.vo.Style;
import com.school.what_is_your_ootd.vo.Weather;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OutfitDto {
    private Long id;
    private List<ClothingItemDto> clothes;
    private List<Long> itemList;
    private Weather weather;
    private Style style;
    private boolean isPublic;

    public OutfitDto(Outfit entity, List<ClothingItemDto> clothes) {
        this.clothes = clothes;
        this.id = entity.getId();
        this.itemList = entity.getItemList();
        this.weather = entity.getWeather();
        this.style = entity.getStyle();
        this.isPublic = entity.isPublic();
    }
}
