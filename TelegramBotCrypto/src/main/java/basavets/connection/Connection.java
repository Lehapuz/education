package basavets.connection;

import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Connection {

    private String URL = "https://waves.exchange/trading/spot/BTC-WXG_USDT-WXG";


    @SneakyThrows
    public String getTextCurrency() {
        String text = "";
        System.setProperty("webdriver.gecko.driver", "D:\\jawa\\Education\\TelegramBotCrypto\\selenium\\geckodriver.exe");
        WebDriver webDriver = new FirefoxDriver();
        webDriver.get(URL);
        Thread.sleep(10000);
        List<WebElement> elements = webDriver.findElements(By.id("id-react"));
        for (WebElement webElement : elements) {
            text = webElement.getText().substring(webElement.getText()
                    .indexOf("BTC-WXG/\n" + "USDT-WXG"), webElement.getText().indexOf("$"));
            System.out.println(text + "found it");
        }
        webDriver.quit();
        return text;
    }


    @SneakyThrows
    public double getCurrency(String text) {
        text = text.substring(17);
        text = text.replaceAll("\\,", "");
        System.out.println(text);
        return Double.parseDouble(text);
    }


    @SneakyThrows
    public BigDecimal changeBitcoin(String messageText, String currency) {
        double amount = Double.parseDouble(messageText);
        System.out.println("Считаю");
        return BigDecimal.valueOf(getCurrency(currency) * amount);
    }


    public BigDecimal changeUSDT(String messageText, String currency) {
        double amount = Double.parseDouble(messageText);
        System.out.println("Считаю");
        return BigDecimal.valueOf(amount / getCurrency(currency));
    }
}
