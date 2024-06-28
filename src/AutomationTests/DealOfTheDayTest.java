package AutomationTests;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DealOfTheDayTest {

	private static WebDriver browser = new ChromeDriver();
	private static WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(10));

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", ".\\dependants\\chromedriver.exe");
		
		// if not maximized then the tabs on the site don't seem to load properly
		browser.manage().window().maximize();
        browser.get("https://www.gamenerdz.com/");
        
        // open the deal of the day
        WebElement dealTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(.,'Deal of the Day')]")));
        dealTab.click();
        
        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("store-pass-product-name")));
        String product = productName.getText().replace("(Deal of the Day)", "").stripTrailing();
        WebElement currentPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("store-pass-product-price")));
        WebElement originalPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("store-pass-product-msrp")));
        
        //WebElement stockCount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("store-pass-product-stock-area")));
        //WebElement seeMoreButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("store-pass-product-see-more")));
        
        // DOING MATH
        String currentPriceText = currentPrice.getText();
        String originalPriceText = originalPrice.getText();
        BigDecimal currentPriceValue = new BigDecimal(currentPriceText.replaceAll("\\$", ""));
        BigDecimal originalPriceValue =  new BigDecimal(originalPriceText.replaceAll("\\$", ""));
        BigDecimal savings = originalPriceValue.subtract(currentPriceValue);
        final BigDecimal hundred = new BigDecimal(100);
        final BigDecimal one = new BigDecimal(1);
        MathContext decimals = new MathContext(2);
        // i've realized how annoying working with BigDecimal can be
        int discount = (one.subtract(currentPriceValue.divide(originalPriceValue, decimals))).multiply(hundred).intValue();
        
        // output information of the on sale product to screen
        System.out.println(product + " is currently on sale.");
        System.out.println("It is currently " + currentPriceText + " from the base price of " + originalPriceText);
        System.out.println("It is on " + discount + "% discount; Thats a savings of $" + savings);
        
        final BigDecimal thirty = new BigDecimal(30);
        if (discount > 40 || (savings.compareTo(thirty) == 1)) {
        	System.out.println("What a great deal! Let's buy it!");
        	// if I return to this test add a section to add the item to the shopping cart or to a watchlist if out of stock
        } else if (discount > 20) {
        	System.out.println("Its not the best deal I've seen, but it may be reasonable. Lets see if this is interesting before we decide.");
        	// if I return to this test add a section to open the info page of the on sale item and if it meets certain criteria add to cart/watch
        } else {
        	System.out.println("Well thats barely on sale at all... Guess today was a bust...");
        }
        
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        browser.quit();
	}
}
