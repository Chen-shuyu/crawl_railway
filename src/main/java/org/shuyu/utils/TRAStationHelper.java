package org.shuyu.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;

public class TRAStationHelper {
    private static Map<String, String> stationMap = new HashMap<>();
    private static boolean isInitialized = false;

    private static void initializeStationMap() throws Exception {
        if (isInitialized) return;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://tdx.transportdata.tw/api/basic/v3/Rail/TRA/Station?$format=json");
        request.setHeader("Accept", "application/json");
        request.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36");

        CloseableHttpResponse response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        JsonNode stations = root.get("Stations"); // 取得 Stations 陣列

        for (JsonNode station : stations) {
            String stationId = station.get("StationID").asText();
            String stationName = station.get("StationName").get("Zh_tw").asText();
            stationMap.put(stationName, stationId);
        }

        response.close();
        client.close();
        isInitialized = true;
        System.out.println("台鐵站名對照表初始化完成，共載入 " + stationMap.size() + " 個車站");
    }

    public static String buildStationString(String departureStation, String arrivalStation) throws Exception {
        initializeStationMap();

        String depId = stationMap.get(departureStation);
        String arrId = stationMap.get(arrivalStation);

        if (depId == null || arrId == null) {
            throw new IllegalArgumentException("找不到車站資訊");
        }

        return depId + "-" + departureStation + " >> " + arrId + "-" + arrivalStation;
    }
}