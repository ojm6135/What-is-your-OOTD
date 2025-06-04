package com.school.what_is_your_ootd.repository;

import com.school.what_is_your_ootd.domain.ClothingItem;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ClothesRepository extends ListCrudRepository<ClothingItem, Long> {
    List<ClothingItem> findAllByUserId(Long userId);
}
