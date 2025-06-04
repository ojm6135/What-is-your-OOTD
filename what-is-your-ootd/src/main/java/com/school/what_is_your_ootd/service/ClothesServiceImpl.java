package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.domain.ClothingItem;
import com.school.what_is_your_ootd.dto.ClothingItemDto;
import com.school.what_is_your_ootd.repository.ClothesRepository;
import com.school.what_is_your_ootd.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ClothesServiceImpl implements ClothesService {
    private ClothesRepository clothesRepository;

    @Autowired
    public ClothesServiceImpl(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
    }

    @PreAuthorize("#username == authentication.name")
    @Override
    public boolean addClothingItem(@P("username") String username, ClothingItemDto clothingItemDto) {
        if (!clothingItemDto.isForSpring() && !clothingItemDto.isForSummer()
                && !clothingItemDto.isForFall() && !clothingItemDto.isForWinter()) {
            return false;
        }

        if (clothingItemDto.getType() == null
                    || clothingItemDto.getDetail() == null
                || clothingItemDto.getColor() == null) {
            return false;
        }

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = principal.getUserId();

        ClothingItem clothingItem = new ClothingItem(userId, clothingItemDto);
        ClothingItem save = clothesRepository.save(clothingItem);

        return save.getUserId().equals(userId);
    }

    @PreAuthorize("#username == authentication.name")
    @Override
    public List<ClothingItemDto> findAllByUsername(@P("username") String username) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = principal.getUserId();

        return clothesRepository.findAllByUserId(userId)
                .stream()
                .map(ClothingItemDto::new)
                .collect(Collectors.toList());
    }
}
