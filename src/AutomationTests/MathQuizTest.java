package AutomationTests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MathQuizTest {

	
	private static WebDriver browser = new ChromeDriver();
	private static WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(10));
	private static JavascriptExecutor js = (JavascriptExecutor) browser;
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", ".\\dependants\\chromedriver.exe");
        browser.get("https://www.mathsisfun.com/mathtest.html");
        
        WebElement theme = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//html")));
        String currentTheme = theme.getAttribute("theme").toLowerCase();
        // who even still has a light mode anymore
        if (currentTheme != "dark") {
	        System.out.println("MY EYES! Switch that shit to dark mode.");
	        WebElement themeSlider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("themeSlider1")));
	        themeSlider.click();
		}

        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
        // need to switch the targeting to the iframe element otherwise nothing inside it will be interactable
        browser.switchTo().frame(browser.findElement(By.xpath("//*[@id='content']/iframe")));
        
        // default timer for the math test is 5 minutes which is a little long for a simple example
        // setting it for 1 minute instead
        Select timeDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("time"))));
        timeDropdown.selectByValue("1 Min");
        
       
        // randomize which math quiz type this test does
        int testNum = (int) (Math.random() * 5);
        String testType = "t" + testNum;
        WebElement testTypeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(testType)));
        testTypeButton.click();
        
        // lets go ahead and randomly pick one of the question subtypes as well to make it a little spicier
        String questionTypeButtonXpath = pickQuestionType(testNum);
        WebElement questionTypeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(questionTypeButtonXpath)));
        questionTypeButton.click();
        
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // answer the questions of the quiz
        // stop when the summary is visible
        Boolean ongoingTest = true;
        int count = 0;
        while (ongoingTest) {
        	answerQuestion(count);
        	count++;
        	try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        	if (browser.findElement(By.id("summary")).isDisplayed()) ongoingTest = false;
        }
        
        System.out.println("Test is complete! " + count + " questions were answered!");

        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
     
        browser.quit();
	}
	
	// DOING MATH
	private static void answerQuestion(int count) {
        
		// okay so there isn't actually any MATH happening here
		// we are directly looking at the page source variable to pull the answer
        Object answer = js.executeScript("return window.my.currEqun.answer");
        
        System.out.println("Question " + (count+1) + " : Answer " + answer.toString());
        
        //wait until the answer field is ready
        //we can't sendkeys directly to this element directly so the answer is being sent as an action instead
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("currA")));
        Actions actions = new Actions(browser);
        actions.sendKeys(answer.toString()).perform();
	}
	
	// method with the switch case to pick a random question subtype
	private static String pickQuestionType(int testNum) {
		int questionNum = 0;
		switch (testNum) {
		case 0:
			System.out.println("Addition has been selected");
			System.out.println("Selecting a random question type from Addition");
			questionNum = (int) ((Math.random() * 5) + 1);
			break;
		case 1:
			System.out.println("Subtraction has been selected");
			System.out.println("Selecting a random question type from Subtraction");
			questionNum = (int) ((Math.random() * 4) + 1);
			break;
		case 2:
			System.out.println("Multiplication has been selected");
			System.out.println("Selecting a random question type from Multiplication");
			questionNum = (int) ((Math.random() * 24) + 1);
			break;
		case 3:
			System.out.println("Multiplication x 12 has been selected");
			System.out.println("Selecting a random question type from Multiplication");
			questionNum = (int) ((Math.random() * 23) + 1);
			break;
		case 4:
			System.out.println("Multiplication x 15 has been selected");
			System.out.println("Selecting a random question type from Multiplication");
			questionNum = (int) ((Math.random() * 9) + 1);
			break;
		case 5:
			System.out.println("Division has been selected");
			System.out.println("Selecting a random question type from Division");
			questionNum = (int) ((Math.random() * 9) + 1);
			break;
		}

		String questionType = "c" + testNum;
		String questionTypeXpath = "//*/div[@id='" + questionType + "']/button[" + questionNum + "]";
		return questionTypeXpath;
	}
}
