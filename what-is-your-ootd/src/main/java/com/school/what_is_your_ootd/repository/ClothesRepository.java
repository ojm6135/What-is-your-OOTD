package com.school.what_is_your_ootd.repository;

import com.school.what_is_your_ootd.domain.ClothingItem;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ClothesRepository extends ListCrudRepository<ClothingItem, Long> {
    List<ClothingItem> findAllByUserId(Long userId);
    @NativeQuery(value = """
                SELECT *
                  FROM CLOTHES
                 WHERE USER_ID = ?1
                   AND (
                           FOR_SPRING = ?2
                        OR FOR_SUMMER = ?3
                        OR FOR_FALL   = ?4
                        OR FOR_WINTER = ?5
                   )
            """)
    List<ClothingItem> findAllByUserIdAndSeason(Long userId,
                                                boolean forSpring, boolean forSummer,
                                                boolean forFall, boolean forWinter);
}
