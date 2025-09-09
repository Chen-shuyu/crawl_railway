package org.shuyu.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class GoogleLogin {

    private WebDriver driver;
    private WebDriverWait wait;
    private String account;
    private String password;

    public GoogleLogin(WebDriver driver, WebDriverWait wait , String account, String password) {
        this.driver = driver;
        this.wait = wait;
        this.account = account;
        this.password = password;
    }

    public void login(){

        try {

            // 前往 Google 登入頁面
            driver.get("https://accounts.google.com/signin");

            // 等待並填入電子郵件
            WebElement emailInput = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("identifierId"))
            );
            emailInput.sendKeys(account);

            // 點擊下一步按鈕
            WebElement nextButton = driver.findElement(By.id("identifierNext"));
            nextButton.click();

            // 等待密碼輸入框出現並填入密碼
            WebElement passwordInput = wait.until(
                    ExpectedConditions.elementToBeClickable(By.name("Passwd"))
            );
            passwordInput.sendKeys(password);

            // 點擊密碼下一步按鈕
            WebElement passwordNext = driver.findElement(By.id("passwordNext"));
            passwordNext.click();

            // 等待登入完成，檢查是否導向到 Google 帳戶頁面
            wait.until(ExpectedConditions.urlContains("myaccount.google.com"));

            System.out.println("登入成功！");
//            return true;

        } catch (Exception e) {
            System.out.println("登入失敗: " + e.getMessage());
            e.printStackTrace();
//            return false;
        }
    }


}
