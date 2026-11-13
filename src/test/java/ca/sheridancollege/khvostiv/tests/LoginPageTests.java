package ca.sheridancollege.khvostiv.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginPageTests {

	static WebDriver browser;

	@BeforeAll
	static void setUpDriver() {
		browser = new ChromeDriver();
		browser.get("http://localhost:50000/login");
	}

	// Login Page Testing
	@Test
	public void testPageLoads() {
		// Verify email field exists
		WebElement username = browser.findElement(By.id("email"));
		assertTrue(username.isDisplayed());

		// Verify password field exists
		WebElement password = browser.findElement(By.id("password"));
		assertTrue(password.isDisplayed());

		// Verify login button exists and is enabled
		WebElement loginButton = browser.findElement(By.cssSelector("input[type='submit']"));
		assertTrue(loginButton.isDisplayed());
		assertTrue(loginButton.isEnabled());

	}

	 // Test 2: Verify validation appears when password is empty
    @Test
    public void testEmptyPasswordShowsValidation() {

        browser.get("http://localhost:50000/login");

        // Enter an email
        WebElement email = browser.findElement(By.id("email"));
        email.sendKeys("test@example.com");

        // Leave password empty
        WebElement password = browser.findElement(By.id("password"));

        // Click login
        WebElement loginButton =
                browser.findElement(By.cssSelector("input[type='submit']"));

        loginButton.click();

        // Get browser validation message
        String validationMessage =
                password.getDomProperty("validationMessage");

        System.out.println("Validation message: " + validationMessage);

        // Verify that validation occurred
        assertTrue(validationMessage != null && !validationMessage.isBlank());
    }

    
    
	@AfterAll
	static void stopDriver() {
		if (browser != null) {
			browser.quit();
		}
	}
	

}
