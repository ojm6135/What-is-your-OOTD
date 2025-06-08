package com.school.what_is_your_ootd.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weather {
    private int tmp;
    private String pcp;
    private int reh;
    private int wsd;

    public Weather(int tmp, String pcp, int reh, int wsd) {
        this.tmp = tmp;
        this.pcp = pcp;
        this.reh = reh;
        this.wsd = wsd;
    }

    @Override
    public String toString() {
        return "기온: " + tmp + "°C" +
                "  강수량: " + pcp +
                "  습도: " + reh + "%" +
                ", 풍속: " + wsd +"m/s";
    }
}
