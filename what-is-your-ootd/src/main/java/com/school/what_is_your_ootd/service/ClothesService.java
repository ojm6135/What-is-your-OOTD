package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.dto.ClothingItemDto;

import java.util.List;

public interface ClothesService {
    boolean addClothingItem(String username, ClothingItemDto clothingItemDto);
    List<ClothingItemDto> findAllById(List<Long> itemList);
    List<ClothingItemDto> findAllByUsername(String username);
    List<ClothingItemDto> findAllByUsernameAndSeason(String username,
                                                     boolean forSpring, boolean forSummer,
                                                     boolean forFall, boolean forWinter);
    boolean deleteClothingItem(String username, Long itemId);
}
