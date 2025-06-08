package com.school.what_is_your_ootd.service;

import com.school.what_is_your_ootd.dto.ClothingItemDto;
import com.school.what_is_your_ootd.dto.OutfitDto;
import com.school.what_is_your_ootd.dto.RecommendRequest;
import com.school.what_is_your_ootd.repository.OutfitRepository;
import com.school.what_is_your_ootd.security.CustomUserDetails;
import com.school.what_is_your_ootd.util.OutfitRecommender;
import com.school.what_is_your_ootd.util.WeatherFetcher;
import com.school.what_is_your_ootd.vo.Location;
import com.school.what_is_your_ootd.vo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class OutfitServiceImpl implements OutfitService {
    private OutfitRepository outfitRepository;
    private UserService userService;
    private ClothesService clothesService;

    @Autowired
    public OutfitServiceImpl(OutfitRepository outfitRepository,
                             UserService userService,
                             ClothesService clothesService) {
        this.outfitRepository = outfitRepository;
        this.userService = userService;
        this.clothesService = clothesService;
    }

    @Override
    public Optional<OutfitDto> recommendOutfit(RecommendRequest request) {
        if (request.getStyle() == null) {
            return Optional.empty();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            // 비회원
            if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
                if (request.getLocation() == null) {
                    return Optional.empty();
                }

                OutfitDto result = OutfitRecommender.recommend(
                        request.getStyle(),
                        request.getLocation()
                );

                return Optional.of(result);
            }

            // 회원
            CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
            Location location = userService.findByUsername(principal.getUsername()).get().getLocation();
            Weather weather = WeatherFetcher.getWeather(location);
            int tmp = weather.getTmp();
            boolean forSpring = false;
            boolean forSummer = false;
            boolean forFall = false;
            boolean forWinter = false;

            // 기온에 적합한 옷 가져오기
            if (tmp >= 23) {  // 여름
                forSummer = true;
            } else if (tmp >= 10 && tmp < 20) {  // 봄, 가을
                forSpring = true;
                forFall = true;
            } else if (tmp < 10) {  // 겨울
                forWinter = true;
            }

            List<ClothingItemDto> items = clothesService.findAllByUsernameAndSeason(
                    principal.getUsername(), forSpring, forSummer, forFall, forWinter
            );

            if (items.size() < 3) {  // 추천을 위한 옷이 부족한 경우
                return Optional.empty();
            }

            OutfitDto result = OutfitRecommender.recommend(
                    request.getStyle(),
                    weather,
                    items
            );

            return Optional.of(result);
        } catch (IOException | IllegalStateException e) {
            return Optional.empty();
        }
    }
}
