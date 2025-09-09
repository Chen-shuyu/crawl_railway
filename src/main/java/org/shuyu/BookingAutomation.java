package org.shuyu;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.shuyu.utils.ConfigReader;
import org.shuyu.utils.GoogleLogin;
import org.shuyu.utils.TRAStationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookingAutomation {

    private static final Logger logger = LoggerFactory.getLogger(BookingAutomation.class);
    private static final ConfigReader config = new ConfigReader();

    private static final String USER_ID = config.getUserId();
    private static final String DEPARTURE = config.getDeparture();
    private static final String ARRIVAL = config.getArrival();
    private static final String SEAT_COUNT = config.getSeatCount();
    private static final String BOOKING_DATE = config.getDate();
    private static final String TRAIN_NO = config.getTrainNo();
    private static final String googleAccount = config.getGoogleAccount();
    private static final String googlePassword = config.getGooglePassword();


    private static final String BASE_URL = "https://tip.railway.gov.tw/tra-tip-web/tip/tip001/tip121/query";

    private WebDriver driver;
    private WebDriverWait wait;

    public BookingAutomation() {
        initializeDriver();

        GoogleLogin googleLogin = new GoogleLogin(driver, wait,googleAccount, googlePassword );
        googleLogin.login();
    }

    /**
     * 初始化 WebDriver
     */
    private void initializeDriver() {
        try {
            logger.info("開始初始化 WebDriver...");

//            WebDriverManager.chromedriver().setup();
            String driverPath = config.getChromeDriverPath();
            System.setProperty("webdriver.chrome.driver", driverPath);

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

            // 步驟 2: 點選 我不是機器人
            clickReCaptchaButton();

            // 步驟 3: 填寫訂票資訊
            fillBookingInformation();

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
        Thread.sleep(1000);
    }

    /**
     * 填寫訂票資訊
     */
    private void fillBookingInformation() throws Exception {
        logger.info("開始填寫訂票資訊...");
        String stationString = TRAStationHelper.buildStationString(DEPARTURE, ARRIVAL);
        String depStation = stationString.split(" >> ")[0];
        String arrStation = stationString.split(" >> ")[1];

        // 填寫身分證字號
        fillUserId();

        // 選擇出發站
        selectDeparture(depStation);

        // 選擇抵達站
        selectArrival(arrStation);

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
        logger.info("選擇車次");
        WebElement trainNoInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("trainNoList1")
                )
        );
        trainNoInput.clear();
        trainNoInput.sendKeys(TRAIN_NO);
    }

    // 選擇日期
    private void selectDate() {
        logger.info("選擇日期");

        String dateFormat = formatDateWithValidation(BOOKING_DATE);

        WebElement dateInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("rideDate1")
                )
        );
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = '"+dateFormat+"'; arguments[0].dispatchEvent(new Event('change'));", dateInput);
    }

    // 日期轉換: YYYYMMDD >> YYYY/MM/DD
    public static String formatDateWithValidation(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    // 選擇坐票數
    private void selectSeat() {
        logger.info("選擇坐票數");
        WebElement seatInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("normalQty")
                )
        );
        seatInput.clear();
        seatInput.sendKeys(SEAT_COUNT);
    }

    // 選擇抵達站
    private void selectArrival(String arrStation) {
        logger.info("選擇抵達站");
        WebElement endStationInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("endStation")
                )
        );
        endStationInput.clear();
        endStationInput.sendKeys(arrStation);
    }

    // 選擇出發站
    private void selectDeparture(String depStation) {
        logger.info("選擇出發站");
        WebElement startStationInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("startStation")
                )
        );
        startStationInput.clear();
        startStationInput.sendKeys(depStation);
    }

    // 填寫身分證字號
    private void fillUserId() {
        logger.info("填入身分證字號");
        WebElement idInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("pid")
                )
        );
        idInput.clear();
        idInput.sendKeys(USER_ID);
    }

    /**
     * 點選 我不是機器人
     */
    private void clickReCaptchaButton() throws InterruptedException {
        // 找到所有 iframe
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));

        driver.switchTo().defaultContent(); // 回主頁面
        driver.switchTo().frame(iframes.get(0)); // 切到第 0 個 iframe

        WebDriverWait wait = new WebDriverWait(driver, 20);
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("recaptcha-anchor")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", checkbox);
        // 模擬人類行為：移動 → 停頓 → 點擊
//        Actions actions = new Actions(driver);
//        actions.moveToElement(checkbox).pause(1000).click().perform();
//        Thread.sleep(1000);
//        WebElement audioButton = wait.until(
//                ExpectedConditions.elementToBeClickable(
//                        By.xpath("//button[@id='recaptcha-audio-button']")
//                )
//        );

//        if (!audioButton.isEnabled()) {
//            System.out.println("audioButton");
//
//        }
//        WebElement audioElement = wait.until(driver -> {
//            WebElement element = driver.findElement(By.id("audio-source"));  // //audio[@id='audio-source']
//            String src = element.getAttribute("src");
//            return (src != null && !src.isEmpty()) ? element : null;
//        });
//
//        String srcUrl = audioElement.getAttribute("src");
//        System.out.println("等待後找到 src: " + srcUrl);

        // audio-source >>SRC
// 切回主畫面
        driver.switchTo().defaultContent();


    }

    /**
     * 點選 訂票
     */
    private void clickBookingButton() {
    }

}
