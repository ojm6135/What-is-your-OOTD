package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.dto.OutfitDto;
import com.school.what_is_your_ootd.dto.RecommendRequest;

import java.util.Optional;

public interface OutfitService {
    Optional<OutfitDto> recommendOutfit(RecommendRequest request);
}
