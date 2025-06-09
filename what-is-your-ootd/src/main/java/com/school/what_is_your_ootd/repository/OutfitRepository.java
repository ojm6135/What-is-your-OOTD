package com.school.what_is_your_ootd.repository;

import com.school.what_is_your_ootd.domain.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {
}
