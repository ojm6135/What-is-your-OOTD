package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.dto.OutfitDto;
import com.school.what_is_your_ootd.dto.RecommendRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OutfitService {
    Optional<OutfitDto> recommendOutfit(RecommendRequest request);
    boolean save(String username, OutfitDto outfitDto);
    Page<OutfitDto> findAllByUserId(String username, Pageable pageable);
}
