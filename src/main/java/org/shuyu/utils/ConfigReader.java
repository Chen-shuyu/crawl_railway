package org.shuyu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

            if (input == null) {
                throw new RuntimeException("找不到 config.properties 檔案");
            }
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("讀取配置檔案失敗", e);
        }
    }

    public String getChromeDriverPath() {
        return properties.getProperty("webdriver.chrome.driver");
    }
    public String getUserId() {
        return properties.getProperty("booking.info.userId");
    }
    public String getDeparture() {
        return properties.getProperty("booking.info.departure");
    }
    public String getArrival() {
        return properties.getProperty("booking.info.arrival");
    }
    public String getSeatCount() {
        return properties.getProperty("booking.info.seatCount");
    }
    public String getDate() {
        return properties.getProperty("booking.info.date");
    }
    public String getTrainNo() {
        return properties.getProperty("booking.info.trainNo");
    }
    public String getGoogleAccount() {
        return properties.getProperty("google.login.account");
    }
    public String getGooglePassword() {
        return properties.getProperty("google.login.password");
    }
}