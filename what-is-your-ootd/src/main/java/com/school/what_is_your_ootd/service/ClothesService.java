package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.dto.ClothingItemDto;

import java.util.List;

public interface ClothesService {
    boolean addClothingItem(String username, ClothingItemDto clothingItemDto);
    List<ClothingItemDto> findAllByUsername(String username);
    boolean deleteClothingItem(String username, Long itemId);
}
