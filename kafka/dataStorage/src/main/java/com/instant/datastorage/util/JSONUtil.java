package com.instant.datastorage.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by nijie on 2016/8/1.
 */
public class JSONUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String,Object> jsonStrToMap(String json) throws IOException {
        return mapper.readValue(json,Map.class);
    }

    public static String mapToJsonStr(Map<String,Object> map) throws IOException {
        return mapper.writeValueAsString(map);
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"numY\":0,\"ww\":1.0,\"numX\":0,\"rain\":0.0,\"thund\":0.0,\"latitude\":30.015,\"forecastModel\":\"INCA\",\"t2m\":30.7,\"v10\":-4.9,\"u10\":1.2,\"forecastTime\":1472107800000,\"startTime\":1472107200000,\"timeOfValidity\":1,\"longitude\":116.015}";
        Map<String,Object> map = mapper.readValue(json,Map.class);
        System.out.println(map);
        System.out.println(mapToJsonStr(map));
    }

    public static void test() throws IOException {
        String json = "{\"SSEDirectionTotal\":27.6,\"SWDirectionTotal\":12.6,\"ENEDirectionMaxSpeed\":4.9,\"WDirectionRates\":4.0,\n" +
                "\"maxSnowDeepDay\":0,\"ESEDirectionTotal\":23.0,\"snowDays\":-9999.0,\"WDirectionMaxSpeed\":2.7,\"EDirectionRates\":4.0,\n" +
                "\"tempEarly\":26.1,\"speedExMax\":15.6,\"rainMaxAmountStartDay\":1122652800000,\"waterAvgMinDay\":1124467200000,\"NEDirectionAvgSpeed\":2.8,\n" +
                "\"rainPentadSixth\":138.7,\"ESEDirectionAppearTimes\":15.0,\"WDirectionTotal\":6.3,\"frozenSoilDepthMax\":-9999.0,\"shallowtemp20cm\":26.2,\n" +
                "\"floorTempExMin\":14.3,\"EvaporationBig\":-9999.0,\"SSWDirectionAvgSpeed\":1.4,\"maxSnowPress\":-9999.0,\"SDirectionTotal\":17.0,\n" +
                "\"sunShineEarly\":29.2,\"tempPentadThird\":30.3,\"WDirectionAppearTimes\":5.0,\"NNEDirectionTotal\":30.3,\"rainPentadThird\":0.1,\n" +
                "\"tempLate\":22.8,\"WSWDirectionRates\":2.0,\"SWDirectionRates\":4.0,\"cloudLowAvg81100\":1.0,\"ENEDirectionAppearTimes\":12.0,\n" +
                "\"EDirectionAvgSpeed\":1.7,\"rainPentadSecond\":35.7,\"rainMaxAmountEndDay\":1122998400000,\"speedExMaxDirection\":-1002.0,\n" +
                "\"cloudLowAvg\":3.1,\"NNWDirectionMaxSpeed\":4.6,\"tempAvg\":25.4,\"hazeDays\":-9999.0,\"rainPentadFourth\":25.6,\"tempPentadFifth\":22.3,\n" +
                "\"grassTempExMin\":-9999.0,\"sandStormDays\":-9999.0,\"SSEDirectionAvgSpeed\":2.0,\"NEDirectionAppearTimes\":14.0,\"shallowtemp10cm\":26.4,\n" +
                "\"WNWEDirectionTotal\":10.7,\"rainLevel1500\":-9999.0,\"ENEDirectionRates\":10.0,\"rainLevel10\":14.0,\"pressureAvgMax\":1005.9,\n" +
                "\"sunShineMid\":75.2,\"frozenSoilDepthMaxDay\":0,\"rainLevel500\":1.0,\"rainPentadFifth\":22.1,\"lightningDays\":-9999.0,\n" +
                "\"rainZeroEndDay\":1124208000000,\"WDirectionAvgSpeed\":1.3,\"speedExMaxDay\":1123344000000,\"NEDirectionTotal\":38.8,\n" +
                "\"SEDirectionTotal\":7.3,\"SDirectionMaxSpeed\":4ays\":-9999.0,\"cloudLowAvg2080\":5.0,\"SSWDirectionAppearTimes\":5.0,\n" +
                "\"calmWindRate\":7.0,\"rainLevel1\":3.0,\"floortempAvg\":2.9,\"EDirectionMaxSpeed\":3.2,\"NDirectionMaxSpeed\":2.1,\"cloudTotalAvg\":4.6,\n" +
                "\"tornadoDays\":-9999.0,\"grassTempMax\":-9999.0,\"windDriectionMaximunRate\":1.5,\"wireIcingTemp\":-9999.0,\n" +
                "\"wireIcingNorthSouthWeight\":-9999.0,\"sunShineRate\":31.0,\"speedAvgMaxDirection\":-1014.0,\"rainLevel100\":1.0,\n" +
                "\"NNEDirectionMaxSpeed\":5.0,\"tempPentadFirst\":3.1,\"WNWEDirectionMaxSpeed\":6.0,\"speedAvgMax\":9.0,\"waterAvgMin\":1.3,\n" +
                "\"NNWDirectionRates\":6.0,\"maxSnowDeep\":-9999.0,\"waterAvgMaxDay\":1165420800000,\"SWDirectionMaxSpeed\":2.5,\"SEDirectionAppearTimes\":9.0,\n" +
                "\"EvaporationSmall\":31.6,\"cloudTotalAvg81100\":10.0,\"EDirectionAppearTimes\":8.0,\"tempPentadSixth\":1.5,\"ENEDirectionAvgSpeed\":1.1,\n" +
                "\"NWDirectionTotal\":0.8,\"pressureExMax\":1035.7,\"stationNo\":\"58026\",\"rainMid\":-9999.0,\"shallowtemp40cm\":-9999.0,\"huimidityAvg\":71.0,\n" +
                "\"rainEarly\":21.4,\"NWDirectionAppearTimes\":1.0,\"SEDirectionAvgSpeed\":1.4,\"grassTempAvg\":-9999.0,\"rainZeroMaxAmountDays\":22.0,\n" +
                "\"speedAvgMaxDay\":1166198400000,\"throundStormDays\":-9999.0,\"SEDirectionMaxSpeed\":3.1,\"WNWEDirectionAvgSpeed\":2.7,\n" +
                "\"floorTempExMinDay\":1167321600000,\"wireIcingEastWestDiameter\":-9999.0,\"WSWDirectionMaxSpeed\":2.7,\"rainDayMax\":18.8,\n" +
                "\"huimidityMin\":21.0,\"ESEDirectionMaxSpeed\":2.3,\"NNWDirectionTotal\":14.1,\"rainLevel50\":1.0,\"mistDays\":24.0,\n" +
                "\"calmWindTimes\":9.0,\"wireIcingSpeed\":-9999.0,\"sunShineGreat60\":9.0,\"tempAvgMin\":-0.7,\"SSWDirectionMaxSpeed\":1.3,\n" +
                "\"WSWDirectionTotal\":7.1,\"huimidityMinDay\":1166284800000,\"NEDirectionRates\":15.0,\"WNWEDirectionAppearTimes\":9.0,\"squallDays\":-9999.0,\n" +
                "\"windDriectionMaximun\":-1002.0,\"ENEDirectionTotal\":16.7,\"wireIcingSymobl\":-9999.0,\"wireIcingEastWestThickness\":-9999.0,\n" +
                "\"sunShineLate\":24.6,\"wireIcingEastWestWeight\":-9999.0,\"NWDirectionAvgSpeed\":0.8,\"windDriectionSecimun\":-9999.0,\n" +
                "\"auroraDays\":-9999.0,\"NDirectionTotal\":5.6,\"blowingSandDays\":-9999.0,\"fogDays\":2.0,\"freezeDays\":20.0,\n" +
                "\"floorTempNegativeDays\":27.0,\"frostDays\":22.0,\"hailDays\":-9999.0,\"rainDays\":4.0,\"tempAvgMax\":7.6,\"floorTempExMax\":18.2,\n" +
                "\"wireIcingDirection\":-9999.0,\"rainLevel250\":-9999.0,\"DeepGeothermal8cm\":-9999.0,\"DeepGeothermal16cm\":-9999.0,\n" +
                "\"NWDirectionMaxSpeed\":0.8,\"waterAvgMax\":9.5,\"NNWDirectionAppearTimes\":8.0,\"shallowtemp15cm\":5.2,\"dustWindDays\":-9999.0,\n" +
                "\"rain\":24.2,\"SSWDirectionTotal\":4.8,\"SDirectionAppearTimes\":6.0,\"rainZeroStartDay\":1165593600000,\"SWDirectionAppearTimes\":6.0,\n" +
                "\"tempMid\":2.9,\"windDriectionSecimunRate\":-9999.0,\"grassTempExMinDay\":0,\"floorTempExMaxDay\":1166976000000,\n" +
                "\"rainDayMaxDay\":1165420800000,\"tempExMax\":12.6,\"waterAvg\":5.4,\"wireIcingEastWestDay\":0,\"grassTempExMax\":-9999.0,\n" +
                "\"NNWDirectionAvgSpeed\":1.8,\"pressureExMin\":1017.4,\"NWDirectionRates\":1.0,\"SSEDirectionMaxSpeed\":1.6,\n" +
                "\"grassTempLess0Days\":-9999.0,\"pressureExMinDay\":1164902400000,\"tempPentadSecond\":2.8,\"latitude\":34.3,\n" +
                "\"NDirectionAppearTimes\":4.0,\"rainMaxAmountDays\":2.0,\"DeepGeothermal32cm\":-9999.0,\"SSEDirectionRates\":2.0,\n" +
                "\"dustDays\":-9999.0,\"smokeDays\":-9999.0,\"SSWDirectionRates\":4.0,\"longitude\":117.95,\"accumulatedSnowDays\":-9999.0,\n" +
                "\"wireIcingNorthSouthDiameter\":-9999.0,\"sunShineLess20\":14.0,\"SEDirectionRates\":7.0,\"floorTempAvgMin\":-1.5,\n" +
                "\"floorTempAvgMax\":11.9,\"tempExMaxDay\":1166803200000,\"EDirectionTotal\":10.7,\"SSEDirectionAppearTimes\":3.0,\n" +
                "\"ESEDirectionRates\":6.0,\"seaLevelPressureAvg\":-9999.0,\"tempExMinDay\":1167321600000,\"WNWEDirectionRates\":7.0,\n" +
                "\"shallowtemp5cm\":3.5,\"pressureAvgMin\":1024.1,\"cloudTotalAvg2080\":10.0,\"NNEDirectionAvgSpeed\":3.1,\n" +
                "\"wireIcingNorthSouthDay\":0,\"rainMaxAmount\":21.4,\"WSWDirectionAppearTimes\":4.0}";
        Map<String,Object> objectMap = JSONUtil.jsonStrToMap(json);
        objectMap.put("monitorTime", DateValueFormat.formatTimeToString(objectMap.get("monitorTime")));
        System.out.println(objectMap.get("monitorTime"));
    }
}
