package org.shuyu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BookingAutomation {

    private static final Logger logger = LoggerFactory.getLogger(BookingAutomation.class);

    private static final String USER_ID = "C121383301";
    private static final String DEPARTURE = "臺北";
    private static final String ARRIVAL = "臺中";
    private static final Integer SEAT_COUNT = 2;
    private static final String DATE = "20250928";
    private static final String TRAIN_NO = "136";

    private static final String BASE_URL = "https://tip.railway.gov.tw/tra-tip-web/tip/tip001/tip121/query";


    private WebDriver driver;
    private WebDriverWait wait;

    public BookingAutomation() {
        initializeDriver();
    }

    /**
     * 初始化 WebDriver
     */
    private void initializeDriver() {
        try {
            logger.info("開始初始化 WebDriver...");

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36");
            options.addArguments("--disable-web-security");
            options.addArguments("--disable-features=VizDisplayCompositor");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, 20);

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.manage().window().maximize();

            logger.info("WebDriver 初始化完成");

        } catch (Exception e) {
            logger.error("WebDriver 初始化失敗: {}", e.getMessage(), e);
            throw new RuntimeException("WebDriver 初始化失敗", e);
        }
    }


    /**
     * 關閉瀏覽器
     */
    public void close() {
        if (driver != null) {
            logger.info("正在關閉瀏覽器...");
            driver.quit();
            logger.info("瀏覽器已關閉");
        }
    }

    /**
     * 執行完整的訂票流程
     */
    public void executeBookingProcess() {
        try {
            // 步驟 1: 開啟首頁並登入
            loginToWebsite();

            // 步驟 2: 填寫訂票資訊
            fillBookingInformation();

            // 步驟 3: 點選 我不是機器人
            clickReCaptchaButton();

            // 步驟 4: 點選 訂票
            clickBookingButton();

            logger.info("訂票流程執行完成");

        } catch (Exception e) {
            logger.error("訂票流程執行失敗: {}", e.getMessage(), e);
        }
    }



    /**
     * 登入網站
     */
    private void loginToWebsite() throws Exception {
        logger.info("開啟訂購頁面...");
        // 開啟首頁
        driver.get(BASE_URL);
        Thread.sleep(3000);
    }

    /**
     * 填寫訂票資訊
     */
    private void fillBookingInformation()  throws Exception {
        logger.info("開始填寫訂票資訊...");

        // 填寫身分證字號
        fillUserId();

        // 選擇出發站
        selectDeparture();

        // 選擇抵達站
        selectArrival();

        // 選擇坐票數
        selectSeat();

        // 選擇日期
        selectDate();

        // 選擇車次
        fillTrainNo();

        logger.info("訂票資訊填寫完成");
    }

    // 選擇車次
    private void fillTrainNo() {
    }

    // 選擇日期
    private void selectDate() {
    }

    // 選擇坐票數
    private void selectSeat() {
    }

    // 選擇抵達站
    private void selectArrival() {
    }

    // 選擇出發站
    private void selectDeparture() {
    }

    // 填寫身分證字號
    private void fillUserId() {
    }

    /**
     * 點選 我不是機器人
     */
    private void clickReCaptchaButton() {
    }

    /**
     * 點選 訂票
     */
    private void clickBookingButton() {
    }

}
