package org.shuyu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(BookingAutomation.class);

    public static void main( String[] args ) {

        BookingAutomation automation = null;

        try {

            automation = new BookingAutomation();
            automation.executeBookingProcess();

            // 保持瀏覽器開啟，讓您檢查結果
            logger.info("訂位流程完成，瀏覽器將在 30 秒後關閉，請檢查結果...");
            Thread.sleep(30000);

        } catch (Exception e) {
            logger.error("程式執行失敗: {}", e.getMessage(), e);
        } finally {
            if (automation != null) {
                automation.close();
            }
        }
    }
}
