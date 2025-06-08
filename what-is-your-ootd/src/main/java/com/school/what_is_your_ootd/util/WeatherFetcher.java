package com.school.what_is_your_ootd.util;

import com.school.what_is_your_ootd.vo.Location;
import com.school.what_is_your_ootd.vo.Weather;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class WeatherFetcher {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_URL = dotenv.get("WEATHER_API_URL");
    private static final String API_KEY = dotenv.get("WEATHER_API_KEY");
    private static final String EXCEL_PATH = dotenv.get("EXCEL_LOCATION_PATH");
    private static final int LAT_COL = 0;
    private static final int LNG_COL = 1;
    private static final int X_COL = 2;
    private static final int Y_COL = 3;
    private static UriComponentsBuilder uriBuilder;

    public static Weather getWeather(Location location) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String baseDate = "" + now.getYear()
                + String.format("%2s", now.getMonthValue()).replace(' ', '0')
                + String.format("%2s", now.getDayOfMonth()).replace(' ', '0');
        String baseTime;
        int min = now.getMinute();

        // api 제공 시간 고려
        // 매 시간 30분 데이터 생성, 45분부터 제공
        // ex) base time이 15:40인 경우, 14:30으로 요청
        if (min < 45) {
            baseTime = String.format("%2s", now.getHour() - 1).replace(' ', '0');
        } else {
            baseTime = String.format("%2s", now.getHour()).replace(' ', '0');
        }

        baseTime += "30";

        int[] xy = getXY(location.getLatitude(), location.getLongitude());

        uriBuilder = UriComponentsBuilder
                .fromUriString(API_URL)
                .queryParam("serviceKey", API_KEY)
                .queryParam("numOfRows", "1")
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", xy[0])
                .queryParam("ny", xy[1]);

        return new Weather(fetchTMP(), fetchPCP(), fetchREH(), fetchWSD());
    }

    // 기온(섭씨)
    private static int fetchTMP() {
        ResponseEntity<String> response = RestClient.builder().baseUrl(API_URL).build()
                .get()
                .uri(uriBuilder.cloneBuilder()
                        .queryParam("pageNo", "25")
                        .build()
                        .toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        return Integer.parseInt(extractValue(jsonObject));
    }


    // 1시간 강수량(mm)
    // "강수없음", "1mm 미만", "실수값mm"(1,0mm 이상 30.0mm 미만), "30.0~50.0mm", "50.0mm이상"
    private static String fetchPCP() {
        ResponseEntity<String> response = RestClient.builder().baseUrl(API_URL).build()
                .get()
                .uri(uriBuilder.cloneBuilder()
                        .queryParam("pageNo", "13")
                        .build()
                        .toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        return extractValue(jsonObject);
    }

    // 습도(%)
    private static int fetchREH() {
        ResponseEntity<String> response = RestClient.builder().baseUrl(API_URL).build()
                .get()
                .uri(uriBuilder.cloneBuilder()
                        .queryParam("pageNo", "31")
                        .build()
                        .toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        return Integer.parseInt(extractValue(jsonObject));
    }

    // 풍속(m/s)
    private static int fetchWSD() {
        ResponseEntity<String> response = RestClient.builder().baseUrl(API_URL).build()
                .get()
                .uri(uriBuilder.cloneBuilder()
                        .queryParam("pageNo", "55")
                        .build()
                        .toUri())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        return Integer.parseInt(extractValue(jsonObject));
    }

    // location.xlsx에서 주어진 위도와 경도에 해당하는 x, y 값 리턴
    // 소숫점 아래 2자리까지 비교.
    // 일치하는 값이 없는 경우, 위도, 경도의 가장 근사한 값에 대한 x, y 리턴
    private static int[] getXY(double latitude, double longitude) throws IOException {
        ZipSecureFile.setMinInflateRatio(0.005);
        ClassPathResource resource = new ClassPathResource(EXCEL_PATH);
        InputStream inputStream = resource.getInputStream();
        XSSFWorkbook sheets = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = sheets.getSheetAt(0);

        BigDecimal lat = BigDecimal.valueOf(latitude);
        BigDecimal lng = BigDecimal.valueOf(longitude);
        BigDecimal roundedLat = lat.setScale(2, RoundingMode.HALF_UP);
        BigDecimal roundedLng = lng.setScale(2, RoundingMode.HALF_UP);

        int x, y;
        BigDecimal minError = BigDecimal.valueOf(Double.MAX_VALUE);
        Row best = null;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            if (row.getCell(0) == null) {
                break;
            }

            BigDecimal latValue = BigDecimal.valueOf(row.getCell(LAT_COL).getNumericCellValue());
            BigDecimal lngValue = BigDecimal.valueOf(row.getCell(LNG_COL).getNumericCellValue());
            BigDecimal roundedLatVal = latValue.setScale(2, RoundingMode.HALF_UP);
            BigDecimal roundedLngVal = lngValue.setScale(2, RoundingMode.HALF_UP);

            if (roundedLatVal.equals(roundedLat) && roundedLngVal.equals(roundedLng)) {
                best = row;
                break;
            }

            BigDecimal error = lat.subtract(latValue).abs()
                    .add(lng.subtract(lngValue).abs());

            if (error.compareTo(minError) < 0) {
                minError = error;
                best = row;
            }
        }

        x = (int) best.getCell(X_COL).getNumericCellValue();
        y = (int) best.getCell(Y_COL).getNumericCellValue();

        return new int[] {x, y};
    }

    private static String extractValue(JSONObject jsonObject) {
        return jsonObject.getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONArray("item")
                .getJSONObject(0)
                .getString("fcstValue");
    }
}
