package com.school.what_is_your_ootd.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.what_is_your_ootd.dto.ClothingItemDto;
import com.school.what_is_your_ootd.dto.OutfitDto;
import com.school.what_is_your_ootd.vo.Location;
import com.school.what_is_your_ootd.vo.Style;
import com.school.what_is_your_ootd.vo.Weather;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OutfitRecommender {
    public static OutfitDto recommend(Style style, Location location) throws IOException {
        Weather weather = WeatherFetcher.getWeather(location);
        String prompt = buildPrompt(style, weather);
        Optional<String> response = OpenAIFetcher.getResponse(prompt);

        if (response.isEmpty()) {
            throw new IllegalStateException("OpenAI 응답이 존재하지 않음.");
        }

        ObjectMapper om = new ObjectMapper();
        ClothingItemDto[] clothingItemDtos = om.readValue(response.get(), ClothingItemDto[].class);
        List<ClothingItemDto> items = Arrays.stream(clothingItemDtos).toList();

        OutfitDto outfitDto = new OutfitDto();
        outfitDto.setClothes(items);
        outfitDto.setWeather(weather);
        outfitDto.setStyle(style);

        return outfitDto;
    }

    public static OutfitDto recommend(Style style, Weather weather, List<ClothingItemDto> clothes) throws IOException {
        String prompt = buildPrompt(style, weather, clothes);
        Optional<String> response = OpenAIFetcher.getResponse(prompt);

        if (response.isEmpty()) {
            throw new IllegalStateException("OpenAI 응답이 존재하지 않음.");
        }

        ObjectMapper om = new ObjectMapper();
        ClothingItemDto[] clothingItemDtos = om.readValue(response.get(), ClothingItemDto[].class);
        List<ClothingItemDto> items = Arrays.stream(clothingItemDtos).toList();

        OutfitDto outfitDto = new OutfitDto();
        outfitDto.setClothes(items);
        outfitDto.setWeather(weather);
        outfitDto.setStyle(style);

        return outfitDto;
    }

    private static String buildPrompt(Style style, Weather weather) {
        ObjectMapper mapper = new ObjectMapper();

        StringBuilder sb = new StringBuilder();
        sb.append("Based on the following input, recommend a suitable outfit.\n\n");
        sb.append(String.format("- Style: %s\n", style));
        sb.append(String.format("- Temperature: %d°C\n", weather.getTmp()));
        sb.append(String.format("- Humidity: %d%%\n", weather.getReh()));
        sb.append(String.format("- Wind speed: %d m/s\n", weather.getWsd()));
        sb.append(String.format("- Precipitation: %s\n\n", weather.getPcp()));

        sb.append("Please recommend only one outfit.\n\n");

        sb.append("- Use English for type (e.g., OUTER, TOP, BOTTOM, SHOES).\n");
        sb.append("- Use Korean for detail and color.\n");
        sb.append("- Return only the JSON array without any additional explanation.\n");
        sb.append("- Important: Do not include any markdown or code block markers such as ```json. Only return raw JSON text.\n");
        sb.append("- Each item in the array must include **type, detail, and color** without exception.\n");
        sb.append("- The order of items in the array must be: OUTER, TOP, BOTTOM, SHOES. If any type is missing, maintain the order and skip the missing one.\n");

        return sb.toString();
    }

    private static String buildPrompt(Style style, Weather weather, List<ClothingItemDto> clothes) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        StringBuilder sb = new StringBuilder();
        sb.append("Based on the following input, recommend a suitable outfit by selecting from the user's existing clothing items.\n\n");
        sb.append(String.format("- Style: %s\n", style));
        sb.append(String.format("- Temperature: %d°C\n", weather.getTmp()));
        sb.append(String.format("- Humidity: %d%%\n", weather.getReh()));
        sb.append(String.format("- Wind speed: %d m/s\n", weather.getWsd()));
        sb.append(String.format("- Precipitation: %s\n\n", weather.getPcp()));

        sb.append("User's clothing items (choose from this list only):\n");
        sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(clothes));

        sb.append("Please recommend only one outfit.\n\n");

        sb.append("- Return only the JSON array without any additional explanation.\n");
        sb.append("- Important: Do not include any markdown or code block markers such as ```json. Only return raw JSON text.\n");
        sb.append("- Respond with a JSON array containing the full data of the selected clothing items as they were provided in the input.\n");
        sb.append("- The order of items in the array must be: OUTER, TOP, BOTTOM, SHOES. If any type is missing, maintain the order and skip the missing one.\n");

        return sb.toString();
    }
}
