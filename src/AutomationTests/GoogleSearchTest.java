package AutomationTests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class GoogleSearchTest {
	
	private static String searchValue = "Armored Core";
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", ".\\dependants\\chromedriver.exe");
		WebDriver browser = new ChromeDriver();
        browser.get("https://www.google.com/");

        //the google page opened in new browser has By.id("input") but if you open google via google.com that tag is missing
        WebElement searchbar = browser.findElement(By.className("gLFyf"));
        
        searchbar.sendKeys(searchValue);
        searchbar.submit();
        
        WebElement searchBody = browser.findElement(By.id("rcnt"));
        String searchText = searchBody.getText().toLowerCase();
        
        int count = 0;
        String searchValueLower = searchValue.toLowerCase();
        //count how many times Armored Core appears in the search results
        while (searchText.contains(searchValueLower)){
            count++;
            searchText = searchText.substring(searchText.indexOf(searchValueLower) + searchValueLower.length());
        }
        System.out.println("\"" + searchValue + "\" appears in the search results " + count + " times");
        
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        browser.quit();
	}
	
}

