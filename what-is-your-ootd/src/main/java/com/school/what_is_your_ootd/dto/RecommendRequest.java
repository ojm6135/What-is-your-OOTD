package com.school.what_is_your_ootd.dto;

import com.school.what_is_your_ootd.vo.Location;
import com.school.what_is_your_ootd.vo.Style;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendRequest {
    private Style style;
    private Location location;
}
