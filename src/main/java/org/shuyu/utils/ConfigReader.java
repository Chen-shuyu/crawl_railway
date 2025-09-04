package org.shuyu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("找不到 config.properties 檔案");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("讀取配置檔案失敗", e);
        }
    }

    public String getChromeDriverPath() {
        return properties.getProperty("webdriver.chrome.driver");
    }
}